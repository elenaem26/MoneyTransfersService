package revolut.bank.service;

import revolut.bank.dto.AccountDto;
import revolut.bank.dto.AccountsDto;
import revolut.bank.exception.EntityNotFoundException;
import revolut.bank.h2.DSLContextFactory;
import revolut.bank.utils.DateUtils;
import com.google.inject.Inject;
import org.h2.util.StringUtils;
import revolut.bank.model.generated.tables.records.AccountRecord;

import java.util.List;

import static revolut.bank.model.generated.tables.Account.ACCOUNT;

public class AccountService {

    @Inject
    private DSLContextFactory dslContextFactory;

    public AccountDto getById(String number) {
        if (StringUtils.isNullOrEmpty(number)) {
            throw new IllegalArgumentException("Number is null or empty");
        }
        AccountRecord record = dslContextFactory.getContext().selectFrom(ACCOUNT).where(ACCOUNT.NUMBER.eq(number)).fetchOne();
        if (record == null) {
            throw new EntityNotFoundException("Account is not found");
        }
        return mapAccount(record);
    }

    public AccountsDto getAllAccounts() {
        List<AccountDto> accounts = dslContextFactory.getContext().selectFrom(ACCOUNT).fetch(this::mapAccount);
        return AccountsDto.builder()
                .accounts(accounts)
                .total(accounts.size())
                .build();
    }

    public AccountsDto getAccountsForParty(Long partyId) {
        List<AccountDto> accounts = dslContextFactory.getContext().selectFrom(ACCOUNT).where(ACCOUNT.PARTY_ID.eq(partyId)).fetch(this::mapAccount);
        return AccountsDto.builder()
                .accounts(accounts)
                .total(accounts.size())
                .build();
    }

    private AccountDto mapAccount(AccountRecord record) {
        if (record == null) {
            return null;
        }
        return AccountDto.builder()
                .number(record.getNumber())
                .balance(record.getBalance())
                .createdDateTime(DateUtils.map(record.getCreatedDateIme()))
                .description(record.getDescription())
                .partyId(record.getPartyId())
                .build();
    }
}
