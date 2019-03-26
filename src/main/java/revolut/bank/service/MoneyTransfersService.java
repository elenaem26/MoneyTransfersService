package revolut.bank.service;

import revolut.bank.exception.MoneyTransferringException;
import revolut.bank.h2.DSLContextFactory;
import revolut.bank.utils.token.AccountTokenRegistry;
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
import java.util.List;

import static revolut.bank.model.generated.tables.Account.ACCOUNT;
import static revolut.bank.model.generated.tables.Transfer.TRANSFER;

public class MoneyTransfersService {

    @Inject
    private DSLContextFactory dslContextFactory;

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

    public void transfer(TransferInfoDto transferInfoDto) throws MoneyTransferringException, InterruptedException {
        //because of the deadlock possibility the block should be locked in a certain order
        String account1;
        String account2;
        if(transferInfoDto.getFromAccountNumber().compareTo(transferInfoDto.getToAccountNumber()) < 0) {
            account1 = transferInfoDto.getFromAccountNumber();
            account2 = transferInfoDto.getToAccountNumber();
        } else {
            account1 = transferInfoDto.getToAccountNumber();
            account2 = transferInfoDto.getFromAccountNumber();
        }

        try {
            synchronized (AccountTokenRegistry.register(account1)) {
                synchronized (AccountTokenRegistry.register(account2)) {
                    executeTransferQuery(transferInfoDto);
                }
            }
        } finally {
            AccountTokenRegistry.deregister(account1);
            AccountTokenRegistry.deregister(account2);
        }
    }

    private void executeTransferQuery(TransferInfoDto transferInfoDto) {
        validateTransferInfoDto(transferInfoDto);
        DSLContext context = dslContextFactory.getContext();
        context.transaction(configuration -> {
            withdrawMoney(transferInfoDto.getFromAccountNumber(), transferInfoDto.getAmount(), configuration);
            depositMoney(transferInfoDto.getToAccountNumber(), transferInfoDto.getAmount(), configuration);
            createTransfer(transferInfoDto, configuration);
        });
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

    private void depositMoney(String accountNumber, Long amount, Configuration configuration) throws MoneyTransferringException {
        AccountRecord record = DSL.using(configuration).selectFrom(ACCOUNT).where(ACCOUNT.NUMBER.eq(accountNumber)).fetchOne();
        if (record == null) {
            throw new IllegalArgumentException("Account not found");
        }
        log.warn(String.format("Deposit %d to account %s", amount, accountNumber));
        record.setBalance(record.getBalance() + amount);
        record.store();
    }

    private void createTransfer(TransferInfoDto transferInfoDto, Configuration configuration) {
        TransferRecord transfer = DSL.using(configuration).newRecord(TRANSFER);
        transfer.setFromAccountId(transferInfoDto.getFromAccountNumber());
        transfer.setToAccountId(transferInfoDto.getToAccountNumber());
        transfer.setAmount(transferInfoDto.getAmount());
        transfer.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        transfer.store();
    }

    private TransferHistoryDto mapHistoryTransfer(TransferRecord transferRecord, String currentAccountNumber) {
        return mapHistoryTransfer(transferRecord.getFromAccountId(), transferRecord.getToAccountId(), transferRecord.getTimestamp(), transferRecord.getAmount(), currentAccountNumber);
    }

    private TransferHistoryDto mapHistoryTransfer(String fromAccountId, String toAccountId, Timestamp timestamp, Long amount, String currentAccountNumber) {
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
                .build();
    }

}
