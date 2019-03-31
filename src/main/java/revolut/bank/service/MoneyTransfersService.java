package revolut.bank.service;

import revolut.bank.exception.MoneyTransferringException;
import revolut.bank.h2.DSLContextFactory;
import revolut.bank.model.TransferStatus;
import revolut.bank.utils.DateUtils;
import com.google.inject.Inject;
import org.apache.log4j.Logger;
import org.h2.util.StringUtils;
import org.jooq.*;
import revolut.bank.model.generated.tables.records.AccountRecord;
import revolut.bank.model.generated.tables.records.TransferRecord;
import org.jooq.impl.DSL;
import revolut.bank.dto.OperationType;
import revolut.bank.dto.TransferHistoriesDto;
import revolut.bank.dto.TransferHistoryDto;
import revolut.bank.dto.TransferInfoDto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static revolut.bank.model.generated.tables.Account.ACCOUNT;
import static revolut.bank.model.generated.tables.Transfer.TRANSFER;

public class MoneyTransfersService {

    @Inject
    private DSLContextFactory dslContextFactory;

    @Inject
    private MoneyTransferJob moneyTransferJob;

    private static final Logger log = Logger.getLogger(MoneyTransfersService.class);

    public TransferHistoriesDto getHistoryForAccount(String number, String fromDate, String toDate) {
        List<TransferHistoryDto> history = dslContextFactory.getContext()
                .selectFrom(TRANSFER)
                .where(getHistoryConditions(number, fromDate, toDate))
                .orderBy(TRANSFER.TIMESTAMP.desc())
                .fetch(transferRecord -> mapHistoryTransfer(transferRecord, number));
        return TransferHistoriesDto.builder()
                .history(history)
                .total(history.size())
                .build();
    }

    private List<Condition> getHistoryConditions(String number, String fromDate, String toDate) {
        List<Condition> conditions = new ArrayList<>();
        conditions.add(TRANSFER.FROM_ACCOUNT_ID.eq(number).or(TRANSFER.TO_ACCOUNT_ID.eq(number)));
        if (fromDate != null) {
            conditions.add(TRANSFER.TIMESTAMP.greaterOrEqual(DateUtils.convertDate(fromDate, true)));
        }
        if (toDate != null) {
            conditions.add(TRANSFER.TIMESTAMP.lessOrEqual(DateUtils.convertDate(toDate, false)));
        }
        return conditions;
    }

    /**
     * Methods creates new Transfer with INPROGRESS state
     * @param transferInfoDto
     */
    public void transfer(TransferInfoDto transferInfoDto) {
        validateTransferInfoDto(transferInfoDto);
        createTransfer(transferInfoDto);
    }

    /**
     * Method performs transfer - it withdraws and deposits money
     * and update transfer's state to DONE;
     * @param transferRecord
     */
    public void transferInJob(TransferRecord transferRecord) {
        //because of the deadlock possibility the block should be locked in a certain order
        log.info(String.format("Trying to transfer from %s to %s, amount = %d",
                transferRecord.getFromAccountId(), transferRecord.getToAccountId(), transferRecord.getAmount()));
        executeTransferQuery(transferRecord);
    }

    /**
     * Get all transfers which state is INPROGRESS
     * @return
     */
    public Collection<TransferRecord> getInprogressTransfers() {
        return dslContextFactory.getContext()
                .selectFrom(TRANSFER)
                .where(TRANSFER.STATE.eq(TransferStatus.INPROGRESS.name()))
                .orderBy(TRANSFER.TIMESTAMP.asc())
                .fetch(record -> record);
    }

    private void executeTransferQuery(TransferRecord transferRecord) {
        DSLContext context = dslContextFactory.getContext();
        try {
            context.transaction(configuration -> {
                withdrawMoney(transferRecord.getFromAccountId(), transferRecord.getAmount(), configuration);
                depositMoney(transferRecord.getToAccountId(), transferRecord.getAmount(), configuration);
                markTransferDone(transferRecord, configuration);
            });
            log.info(String.format("Transfer succeed: from %s to %s, amount = %d",
                    transferRecord.getFromAccountId(), transferRecord.getToAccountId(), transferRecord.getAmount()));
        } catch (Exception ex) {
            markTransferError(transferRecord);
            log.info(String.format("Transfer error: from %s to %s, amount = %d",
                    transferRecord.getFromAccountId(), transferRecord.getToAccountId(), transferRecord.getAmount()));
        }
    }

    private void validateTransferInfoDto(TransferInfoDto transferInfoDto) {
        if (transferInfoDto.getAmount() == null || transferInfoDto.getAmount() < 0) {
            throw new IllegalArgumentException("Amount is null or less than zero");
        }
        if (StringUtils.isNullOrEmpty(transferInfoDto.getFromAccountNumber())) {
            throw new IllegalArgumentException("From account is null or empty");
        }
        if (StringUtils.isNullOrEmpty(transferInfoDto.getToAccountNumber())) {
            throw new IllegalArgumentException("To account is null or empty");
        }
        if (transferInfoDto.getFromAccountNumber().equals(transferInfoDto.getToAccountNumber())) {
            throw new IllegalArgumentException("From account and to account are the same ");
        }
        Record1<Long> balance = dslContextFactory.getContext()
                .select(ACCOUNT.BALANCE)
                .from(ACCOUNT)
                .where(ACCOUNT.NUMBER.eq(transferInfoDto.getFromAccountNumber()))
                .fetchOne();
        if (balance == null) {
            throw new IllegalArgumentException("Account not found");
        }
        if (balance.value1() < transferInfoDto.getAmount()) {
            throw new IllegalArgumentException("Not enough money");
        }
    }

    private void withdrawMoney(String accountNumber, Long amount, Configuration configuration) throws MoneyTransferringException {
        AccountRecord record = DSL.using(configuration).selectFrom(ACCOUNT).where(ACCOUNT.NUMBER.eq(accountNumber)).fetchOne();
        if (record == null) {
            throw new IllegalArgumentException("Account not found");
        }
        if (record.getBalance() - amount < 0) {
            throw new MoneyTransferringException("Not enough money");
        }
        log.warn(String.format("Withdraw %d from account %s", amount, accountNumber));
        record.setBalance(record.getBalance() - amount);
        record.store();
    }

    private void depositMoney(String accountNumber, Long amount, Configuration configuration) {
        AccountRecord record = DSL.using(configuration).selectFrom(ACCOUNT).where(ACCOUNT.NUMBER.eq(accountNumber)).fetchOne();
        if (record == null) {
            throw new IllegalArgumentException("Account not found");
        }
        log.warn(String.format("Deposit %d to account %s", amount, accountNumber));
        record.setBalance(record.getBalance() + amount);
        record.store();
    }

    private void createTransfer(TransferInfoDto transferInfoDto) {
        TransferRecord transfer = dslContextFactory.getContext().newRecord(TRANSFER);
        transfer.setFromAccountId(transferInfoDto.getFromAccountNumber());
        transfer.setToAccountId(transferInfoDto.getToAccountNumber());
        transfer.setAmount(transferInfoDto.getAmount());
        transfer.setState(TransferStatus.INPROGRESS.name());
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        transfer.setTimestamp(now);
        transfer.store();
    }

    private void markTransferDone(TransferRecord record, Configuration configuration) {
        TransferRecord recordInTransacion = DSL.using(configuration)
                .selectFrom(TRANSFER)
                .where(TRANSFER.ID.eq(record.getId()))
                .fetchOne();
        recordInTransacion.setState(TransferStatus.DONE.name());
        recordInTransacion.store();
    }

    private void markTransferError(TransferRecord record) {
        dslContextFactory.getContext()
                .update(TRANSFER)
                .set(TRANSFER.STATE, TransferStatus.ERROR.name())
                .where(TRANSFER.ID.eq(record.getId()));
    }

    private TransferHistoryDto mapHistoryTransfer(TransferRecord transferRecord, String currentAccountNumber) {
        return mapHistoryTransfer(transferRecord.getFromAccountId(), transferRecord.getToAccountId(),
                transferRecord.getTimestamp(), transferRecord.getAmount(),
                transferRecord.getState(), currentAccountNumber);
    }

    private TransferHistoryDto mapHistoryTransfer(String fromAccountId, String toAccountId,
                                                  Timestamp timestamp, Long amount, String state, String currentAccountNumber) {
        OperationType operationType;
        String interactionAccountNumber;
        if (fromAccountId.equals(currentAccountNumber)) {
            interactionAccountNumber = toAccountId;
            operationType = OperationType.WITHDRAW;
        } else {
            interactionAccountNumber = fromAccountId;
            operationType = OperationType.DEPOSIT;
        }
        return TransferHistoryDto.builder()
                .userAccountNumber(currentAccountNumber)
                .interactionAccountNumber(interactionAccountNumber)
                .amount(amount)
                .operation(operationType.name())
                .timestamp(DateUtils.map(timestamp))
                .state(state)
                .build();
    }

}
