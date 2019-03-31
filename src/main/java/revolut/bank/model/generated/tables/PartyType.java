/*
 * This file is generated by jOOQ.
 */
package revolut.bank.model.generated.tables;


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
import revolut.bank.model.generated.tables.records.PartyTypeRecord;


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
public class PartyType extends TableImpl<PartyTypeRecord> {

    private static final long serialVersionUID = -125035636;

    /**
     * The reference instance of <code>PUBLIC.PARTY_TYPE</code>
     */
    public static final PartyType PARTY_TYPE = new PartyType();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PartyTypeRecord> getRecordType() {
        return PartyTypeRecord.class;
    }

    /**
     * The column <code>PUBLIC.PARTY_TYPE.ID</code>.
     */
    public final TableField<PartyTypeRecord, Long> ID = createField("ID", org.jooq.impl.SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>PUBLIC.PARTY_TYPE.NAME</code>.
     */
    public final TableField<PartyTypeRecord, String> NAME = createField("NAME", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false).identity(true), this, "");

    /**
     * Create a <code>PUBLIC.PARTY_TYPE</code> table reference
     */
    public PartyType() {
        this(DSL.name("PARTY_TYPE"), null);
    }

    /**
     * Create an aliased <code>PUBLIC.PARTY_TYPE</code> table reference
     */
    public PartyType(String alias) {
        this(DSL.name(alias), PARTY_TYPE);
    }

    /**
     * Create an aliased <code>PUBLIC.PARTY_TYPE</code> table reference
     */
    public PartyType(Name alias) {
        this(alias, PARTY_TYPE);
    }

    private PartyType(Name alias, Table<PartyTypeRecord> aliased) {
        this(alias, aliased, null);
    }

    private PartyType(Name alias, Table<PartyTypeRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> PartyType(Table<O> child, ForeignKey<O, PartyTypeRecord> key) {
        super(child, key, PARTY_TYPE);
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
        return Arrays.<Index>asList(Indexes.PRIMARY_KEY_3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<PartyTypeRecord, Long> getIdentity() {
        return Keys.IDENTITY_PARTY_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<PartyTypeRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_3;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<PartyTypeRecord>> getKeys() {
        return Arrays.<UniqueKey<PartyTypeRecord>>asList(Keys.CONSTRAINT_3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PartyType as(String alias) {
        return new PartyType(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PartyType as(Name alias) {
        return new PartyType(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public PartyType rename(String name) {
        return new PartyType(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public PartyType rename(Name name) {
        return new PartyType(name, null);
    }
}
