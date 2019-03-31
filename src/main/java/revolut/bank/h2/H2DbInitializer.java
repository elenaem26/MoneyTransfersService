package revolut.bank.h2;

import revolut.bank.model.PartyType;
import com.google.inject.Inject;
import org.jooq.DSLContext;
import revolut.bank.model.generated.tables.records.PartyRecord;
import revolut.bank.model.generated.tables.records.PartyTypeRecord;
import org.jooq.impl.SQLDataType;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import static revolut.bank.model.generated.Tables.TRANSFER;
import static revolut.bank.model.generated.tables.Account.ACCOUNT;
import static revolut.bank.model.generated.tables.Organization.ORGANIZATION;
import static revolut.bank.model.generated.tables.Party.PARTY;
import static revolut.bank.model.generated.tables.PartyType.PARTY_TYPE;
import static revolut.bank.model.generated.tables.Person.PERSON;
import static org.jooq.impl.DSL.constraint;
public class H2DbInitializer {

    @Inject
    private DSLContextFactory dslContextFactory;

    public void initInMemoryDb() {
        initDbSchema();
        fillDb();
    }

    private void fillDb() {
        DSLContext context = dslContextFactory.getContext();
        Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
        PartyTypeRecord personType = context.insertInto(PARTY_TYPE, PARTY_TYPE.NAME)
                .values(PartyType.PERSON.name())
                .returning(PARTY_TYPE.ID)
                .fetchOne();
        PartyTypeRecord  organizationType  = context.insertInto(PARTY_TYPE, PARTY_TYPE.NAME)
                .values(PartyType.ORGANIZATION.name())
                .returning(PARTY_TYPE.ID)
                .fetchOne();

        PartyRecord party = context.insertInto(PARTY, PARTY.CREATED_DATE_TIME, PARTY.PARTY_TYPE_ID, PARTY.DESCRIPTION)//TODO может все таки создать отдельную таблицу с именем фамилией и тд?
                .values(currentTimestamp, personType.getId(), "Олег Иванов")
                .returning( PARTY.ID)
                .fetchOne();
        context.insertInto(PERSON, PERSON.PARTY_ID, PERSON.FIRST_NAME, PERSON.LAST_NAME, PERSON.MIDDLE_NAME, PERSON.ADDRESS, PERSON.BIRTH_DATE)
                .values(party.getId(), "Олег", "Иванов", "Григорьевич", "г. Воронеж, ул. Коммисаржевской 10", java.sql.Date.valueOf(LocalDate.of(1967, Month.SEPTEMBER, 22)))
                .execute();
        context.insertInto(ACCOUNT, ACCOUNT.NUMBER, ACCOUNT.PARTY_ID, ACCOUNT.BALANCE, ACCOUNT.CREATED_DATE_IME, ACCOUNT.DESCRIPTION)
                .values("123", party.getId(), 0L, currentTimestamp, "Сберегательный счет")
                .values("234", party.getId(), 0L, currentTimestamp, "Счет по карте")
                .execute();

        party = context.insertInto(PARTY, PARTY.CREATED_DATE_TIME, PARTY.PARTY_TYPE_ID, PARTY.DESCRIPTION)
                .values(currentTimestamp, personType.getId(), "Валентина Григорьева")
                .returning(PARTY.ID)
                .fetchOne();
        context.insertInto(PERSON, PERSON.PARTY_ID, PERSON.FIRST_NAME, PERSON.LAST_NAME, PERSON.MIDDLE_NAME, PERSON.ADDRESS, PERSON.BIRTH_DATE)
                .values(party.getId(), "Валентина", "Григорьева", "Ивановна", "г. Воронеж, ул. Коммисаржевской 11", java.sql.Date.valueOf(LocalDate.of(1977, Month.SEPTEMBER, 22)))
                .execute();
        context.insertInto(ACCOUNT, ACCOUNT.NUMBER, ACCOUNT.PARTY_ID, ACCOUNT.BALANCE, ACCOUNT.CREATED_DATE_IME, ACCOUNT.DESCRIPTION)
                .values("345", party.getId(), 10000L, currentTimestamp, "Сберегательный счет")
                .execute();

        party = context.insertInto(PARTY, PARTY.CREATED_DATE_TIME, PARTY.PARTY_TYPE_ID, PARTY.DESCRIPTION)
                .values(currentTimestamp, personType.getId(), "Игорь Николаев")
                .returning(PARTY.ID)
                .fetchOne();
        context.insertInto(PERSON, PERSON.PARTY_ID, PERSON.FIRST_NAME, PERSON.LAST_NAME, PERSON.MIDDLE_NAME, PERSON.ADDRESS, PERSON.BIRTH_DATE)
                .values(party.getId(), "Игорь", "Николаев", "Юрьевич", "г. Москва, ул. Коммисаржевской 11", java.sql.Date.valueOf(LocalDate.of(1960, Month.JANUARY, 17)))
                .execute();
        context.insertInto(ACCOUNT, ACCOUNT.NUMBER, ACCOUNT.PARTY_ID, ACCOUNT.BALANCE, ACCOUNT.CREATED_DATE_IME, ACCOUNT.DESCRIPTION)
                .values("456", party.getId(), 20220L, currentTimestamp, "Сберегательный счет 1")
                .values("567", party.getId(), 45660L, currentTimestamp, "Сберегательный счет 2")
                .values("678", party.getId(), 346110L, currentTimestamp, "Сберегательный счет 3")
                .values("789", party.getId(), 33930L, currentTimestamp, "Счет по карте")
                .execute();


        party = context.insertInto(PARTY, PARTY.CREATED_DATE_TIME, PARTY.PARTY_TYPE_ID, PARTY.DESCRIPTION)
                .values(currentTimestamp, organizationType.getId(), "ООО Газпром")
                .returning(PARTY.ID)
                .fetchOne();
        context.insertInto(ORGANIZATION, ORGANIZATION.PARTY_ID, ORGANIZATION.NAME, ORGANIZATION.CREATED_DATE)
                .values(party.getId(), "ООО Газпром", java.sql.Date.valueOf(LocalDate.of(1989, Month.AUGUST, 1)))
                .execute();
        context.insertInto(ACCOUNT, ACCOUNT.NUMBER, ACCOUNT.PARTY_ID, ACCOUNT.BALANCE, ACCOUNT.CREATED_DATE_IME, ACCOUNT.DESCRIPTION)
                .values("1234", party.getId(), 20000220L, currentTimestamp, "Сберегательный счет 1")
                .values("2345", party.getId(), 45660000L, currentTimestamp, "Сберегательный счет 2")
                .values("3465", party.getId(), 346110000L, currentTimestamp, "Сберегательный счет 3")
                .execute();
    }

    private void initDbSchema() {
        DSLContext context = dslContextFactory.getContext();
        context.execute("DROP ALL OBJECTS");
        context.createTableIfNotExists(PARTY_TYPE)
                .column(PARTY_TYPE.ID, SQLDataType.BIGINT.identity(true))
                .column(PARTY_TYPE.NAME, SQLDataType.VARCHAR(255).nullable(false))
                .constraints(
                        constraint("PK_PARTY_TYPE").primaryKey(PARTY_TYPE.ID))
                .execute();

        context.createTableIfNotExists(PARTY)
                .column(PARTY.ID, SQLDataType.BIGINT.identity(true))
                .column(PARTY.CREATED_DATE_TIME, SQLDataType.TIMESTAMP.nullable(false))
                .column(PARTY.DESCRIPTION, SQLDataType.VARCHAR(1000))
                .column(PARTY.PARTY_TYPE_ID, SQLDataType.BIGINT.nullable(false))
                .constraints(
                        constraint("PK_PARTY").primaryKey(PARTY.ID),
                        constraint("FK_PARTY_PARTY_TYPE").foreignKey(PARTY.PARTY_TYPE_ID).references(PARTY_TYPE, PARTY_TYPE.ID))
                .execute();

        context.createTableIfNotExists(PERSON)
                .column(PERSON.PARTY_ID, SQLDataType.BIGINT)
                .column(PERSON.FIRST_NAME, SQLDataType.VARCHAR(1000).nullable(false))
                .column(PERSON.LAST_NAME, SQLDataType.VARCHAR(1000).nullable(false))
                .column(PERSON.MIDDLE_NAME, SQLDataType.VARCHAR(1000))
                .column(PERSON.ADDRESS, SQLDataType.VARCHAR(1000).nullable(false))
                .column(PERSON.BIRTH_DATE, SQLDataType.DATE.nullable(false))
                .constraints(
                        constraint("PK_PERSON").primaryKey(PERSON.PARTY_ID),
                        constraint("FK_PERSON_PARTY").foreignKey(PERSON.PARTY_ID).references(PARTY, PARTY.ID))
                .execute();

        context.createTableIfNotExists(ORGANIZATION)
                .column(ORGANIZATION.PARTY_ID, SQLDataType.BIGINT)
                .column(ORGANIZATION.NAME, SQLDataType.VARCHAR(1000).nullable(false))
                .column(ORGANIZATION.CREATED_DATE, SQLDataType.DATE.nullable(false))
                .constraints(
                        constraint("PK_ORGANIZATION").primaryKey(ORGANIZATION.PARTY_ID),
                        constraint("FK_ORGANIZATION_PARTY").foreignKey(ORGANIZATION.PARTY_ID).references(PARTY, PARTY.ID))
                .execute();

        context.createTableIfNotExists(ACCOUNT)
                .column(ACCOUNT.NUMBER, SQLDataType.VARCHAR(255))
                .column(ACCOUNT.CREATED_DATE_IME, SQLDataType.TIMESTAMP.nullable(false))
                .column(ACCOUNT.DESCRIPTION, SQLDataType.VARCHAR(1000))
                .column(ACCOUNT.BALANCE, SQLDataType.BIGINT.nullable(false))
                .column(ACCOUNT.PARTY_ID, SQLDataType.BIGINT.nullable(false))
                .constraints(
                        constraint("PK_ACCOUNT").primaryKey(ACCOUNT.NUMBER),
                        constraint("FK_ACCOUNT_PARTY").foreignKey(ACCOUNT.PARTY_ID).references(PARTY, PARTY.ID),
                        constraint("ACCOUNT_BALANCE_POSITIVE").check(ACCOUNT.BALANCE.greaterOrEqual(0L)))
                .execute();

        context.createTableIfNotExists(TRANSFER)
                .column(TRANSFER.ID, SQLDataType.BIGINT.identity(true))
                .column(TRANSFER.FROM_ACCOUNT_ID, SQLDataType.VARCHAR(255).nullable(false))
                .column(TRANSFER.TO_ACCOUNT_ID, SQLDataType.VARCHAR(255).nullable(false))
                .column(TRANSFER.TIMESTAMP, SQLDataType.TIMESTAMP.nullable(false))
                .column(TRANSFER.STATE, SQLDataType.VARCHAR(50).nullable(false))
                .column(TRANSFER.AMOUNT, SQLDataType.BIGINT.nullable(false))
                .constraints(
                        constraint("PK_TRANSFER").primaryKey(TRANSFER.ID),
                        constraint("FK_TRANSFER_ACCOUNT_FROM").foreignKey(TRANSFER.FROM_ACCOUNT_ID).references(ACCOUNT, ACCOUNT.NUMBER),
                        constraint("FK_TRANSFER_ACCOUNT_TO").foreignKey(TRANSFER.TO_ACCOUNT_ID).references(ACCOUNT, ACCOUNT.NUMBER),
                        constraint("ACCOUNTS_DIFFERENT").check(TRANSFER.FROM_ACCOUNT_ID.notEqual(TRANSFER.TO_ACCOUNT_ID)),
                        constraint("TRANSFER_AMOUNT_POSITIVE").check(TRANSFER.AMOUNT.greaterOrEqual(0L)))
                .execute();
    }
}
