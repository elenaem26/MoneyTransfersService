/*
 * This file is generated by jOOQ.
 */
package revolut.bank.model.generated.tables;


import java.sql.Date;
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
import revolut.bank.model.generated.tables.records.OrganizationRecord;


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
public class Organization extends TableImpl<OrganizationRecord> {

    private static final long serialVersionUID = -1035949482;

    /**
     * The reference instance of <code>PUBLIC.ORGANIZATION</code>
     */
    public static final Organization ORGANIZATION = new Organization();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<OrganizationRecord> getRecordType() {
        return OrganizationRecord.class;
    }

    /**
     * The column <code>PUBLIC.ORGANIZATION.PARTY_ID</code>.
     */
    public final TableField<OrganizationRecord, Long> PARTY_ID = createField("PARTY_ID", org.jooq.impl.SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>PUBLIC.ORGANIZATION.NAME</code>.
     */
    public final TableField<OrganizationRecord, String> NAME = createField("NAME", org.jooq.impl.SQLDataType.VARCHAR(1000).nullable(false), this, "");

    /**
     * The column <code>PUBLIC.ORGANIZATION.CREATED_DATE</code>.
     */
    public final TableField<OrganizationRecord, Date> CREATED_DATE = createField("CREATED_DATE", org.jooq.impl.SQLDataType.DATE.nullable(false), this, "");

    /**
     * Create a <code>PUBLIC.ORGANIZATION</code> table reference
     */
    public Organization() {
        this(DSL.name("ORGANIZATION"), null);
    }

    /**
     * Create an aliased <code>PUBLIC.ORGANIZATION</code> table reference
     */
    public Organization(String alias) {
        this(DSL.name(alias), ORGANIZATION);
    }

    /**
     * Create an aliased <code>PUBLIC.ORGANIZATION</code> table reference
     */
    public Organization(Name alias) {
        this(alias, ORGANIZATION);
    }

    private Organization(Name alias, Table<OrganizationRecord> aliased) {
        this(alias, aliased, null);
    }

    private Organization(Name alias, Table<OrganizationRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> Organization(Table<O> child, ForeignKey<O, OrganizationRecord> key) {
        super(child, key, ORGANIZATION);
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
        return Arrays.<Index>asList(Indexes.PRIMARY_KEY_D);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<OrganizationRecord, Long> getIdentity() {
        return Keys.IDENTITY_ORGANIZATION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<OrganizationRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_D;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<OrganizationRecord>> getKeys() {
        return Arrays.<UniqueKey<OrganizationRecord>>asList(Keys.CONSTRAINT_D);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<OrganizationRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<OrganizationRecord, ?>>asList(Keys.ORGANIZATION_FK0);
    }

    public Party party() {
        return new Party(this, Keys.ORGANIZATION_FK0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Organization as(String alias) {
        return new Organization(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Organization as(Name alias) {
        return new Organization(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Organization rename(String name) {
        return new Organization(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Organization rename(Name name) {
        return new Organization(name, null);
    }
}
