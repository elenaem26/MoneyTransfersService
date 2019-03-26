/*
 * This file is generated by jOOQ.
 */
package revolut.bank.model.generated;


import javax.annotation.Generated;

import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.Internal;

import revolut.bank.model.generated.tables.Account;
import revolut.bank.model.generated.tables.Organization;
import revolut.bank.model.generated.tables.Party;
import revolut.bank.model.generated.tables.PartyType;
import revolut.bank.model.generated.tables.Person;
import revolut.bank.model.generated.tables.Transfer;


/**
 * A class modelling indexes of tables of the <code>PUBLIC</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index FK_ACCOUNT_PARTY_INDEX_E = Indexes0.FK_ACCOUNT_PARTY_INDEX_E;
    public static final Index PRIMARY_KEY_E = Indexes0.PRIMARY_KEY_E;
    public static final Index PRIMARY_KEY_D = Indexes0.PRIMARY_KEY_D;
    public static final Index FK_PARTY_PARTY_TYPE_INDEX_4 = Indexes0.FK_PARTY_PARTY_TYPE_INDEX_4;
    public static final Index PRIMARY_KEY_4 = Indexes0.PRIMARY_KEY_4;
    public static final Index PRIMARY_KEY_3 = Indexes0.PRIMARY_KEY_3;
    public static final Index PRIMARY_KEY_8 = Indexes0.PRIMARY_KEY_8;
    public static final Index FK_TRANSFER_ACCOUNT_FROM_INDEX_7 = Indexes0.FK_TRANSFER_ACCOUNT_FROM_INDEX_7;
    public static final Index FK_TRANSFER_ACCOUNT_TO_INDEX_7 = Indexes0.FK_TRANSFER_ACCOUNT_TO_INDEX_7;
    public static final Index PRIMARY_KEY_7 = Indexes0.PRIMARY_KEY_7;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Indexes0 {
        public static Index FK_ACCOUNT_PARTY_INDEX_E = Internal.createIndex("FK_ACCOUNT_PARTY_INDEX_E", Account.ACCOUNT, new OrderField[] { Account.ACCOUNT.PARTY_ID }, false);
        public static Index PRIMARY_KEY_E = Internal.createIndex("PRIMARY_KEY_E", Account.ACCOUNT, new OrderField[] { Account.ACCOUNT.NUMBER }, true);
        public static Index PRIMARY_KEY_D = Internal.createIndex("PRIMARY_KEY_D", Organization.ORGANIZATION, new OrderField[] { Organization.ORGANIZATION.PARTY_ID }, true);
        public static Index FK_PARTY_PARTY_TYPE_INDEX_4 = Internal.createIndex("FK_PARTY_PARTY_TYPE_INDEX_4", Party.PARTY, new OrderField[] { Party.PARTY.PARTY_TYPE_ID }, false);
        public static Index PRIMARY_KEY_4 = Internal.createIndex("PRIMARY_KEY_4", Party.PARTY, new OrderField[] { Party.PARTY.ID }, true);
        public static Index PRIMARY_KEY_3 = Internal.createIndex("PRIMARY_KEY_3", PartyType.PARTY_TYPE, new OrderField[] { PartyType.PARTY_TYPE.ID }, true);
        public static Index PRIMARY_KEY_8 = Internal.createIndex("PRIMARY_KEY_8", Person.PERSON, new OrderField[] { Person.PERSON.PARTY_ID }, true);
        public static Index FK_TRANSFER_ACCOUNT_FROM_INDEX_7 = Internal.createIndex("FK_TRANSFER_ACCOUNT_FROM_INDEX_7", Transfer.TRANSFER, new OrderField[] { Transfer.TRANSFER.FROM_ACCOUNT_ID }, false);
        public static Index FK_TRANSFER_ACCOUNT_TO_INDEX_7 = Internal.createIndex("FK_TRANSFER_ACCOUNT_TO_INDEX_7", Transfer.TRANSFER, new OrderField[] { Transfer.TRANSFER.TO_ACCOUNT_ID }, false);
        public static Index PRIMARY_KEY_7 = Internal.createIndex("PRIMARY_KEY_7", Transfer.TRANSFER, new OrderField[] { Transfer.TRANSFER.ID }, true);
    }
}
