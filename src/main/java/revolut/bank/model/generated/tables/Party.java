/*
 * This file is generated by jOOQ.
 */
package revolut.bank.model.generated.tables;


import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import revolut.bank.model.generated.Indexes;
import revolut.bank.model.generated.Keys;
import revolut.bank.model.generated.Public;
import revolut.bank.model.generated.tables.records.PartyRecord;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Party extends TableImpl<PartyRecord> {

    private static final long serialVersionUID = 206376663;

    /**
     * The reference instance of <code>PUBLIC.PARTY</code>
     */
    public static final Party PARTY = new Party();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PartyRecord> getRecordType() {
        return PartyRecord.class;
    }

    /**
     * The column <code>PUBLIC.PARTY.ID</code>.
     */
    public final TableField<PartyRecord, Long> ID = createField("ID", org.jooq.impl.SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>PUBLIC.PARTY.CREATED_DATE_TIME</code>.
     */
    public final TableField<PartyRecord, Timestamp> CREATED_DATE_TIME = createField("CREATED_DATE_TIME", org.jooq.impl.SQLDataType.TIMESTAMP.precision(10).nullable(false), this, "");

    /**
     * The column <code>PUBLIC.PARTY.DESCRIPTION</code>.
     */
    public final TableField<PartyRecord, String> DESCRIPTION = createField("DESCRIPTION", org.jooq.impl.SQLDataType.VARCHAR(1000), this, "");

    /**
     * The column <code>PUBLIC.PARTY.PARTY_TYPE_ID</code>.
     */
    public final TableField<PartyRecord, Long> PARTY_TYPE_ID = createField("PARTY_TYPE_ID", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * Create a <code>PUBLIC.PARTY</code> table reference
     */
    public Party() {
        this(DSL.name("PARTY"), null);
    }

    /**
     * Create an aliased <code>PUBLIC.PARTY</code> table reference
     */
    public Party(String alias) {
        this(DSL.name(alias), PARTY);
    }

    /**
     * Create an aliased <code>PUBLIC.PARTY</code> table reference
     */
    public Party(Name alias) {
        this(alias, PARTY);
    }

    private Party(Name alias, Table<PartyRecord> aliased) {
        this(alias, aliased, null);
    }

    private Party(Name alias, Table<PartyRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> Party(Table<O> child, ForeignKey<O, PartyRecord> key) {
        super(child, key, PARTY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.PARTY_FK0_INDEX_4, Indexes.PRIMARY_KEY_4);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<PartyRecord, Long> getIdentity() {
        return Keys.IDENTITY_PARTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<PartyRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_4;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<PartyRecord>> getKeys() {
        return Arrays.<UniqueKey<PartyRecord>>asList(Keys.CONSTRAINT_4);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<PartyRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<PartyRecord, ?>>asList(Keys.PARTY_FK0);
    }

    public PartyType partyType() {
        return new PartyType(this, Keys.PARTY_FK0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Party as(String alias) {
        return new Party(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Party as(Name alias) {
        return new Party(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Party rename(String name) {
        return new Party(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Party rename(Name name) {
        return new Party(name, null);
    }
}
