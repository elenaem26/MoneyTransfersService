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
import revolut.bank.model.generated.tables.records.PersonRecord;


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
public class Person extends TableImpl<PersonRecord> {

    private static final long serialVersionUID = -2140392082;

    /**
     * The reference instance of <code>PUBLIC.PERSON</code>
     */
    public static final Person PERSON = new Person();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PersonRecord> getRecordType() {
        return PersonRecord.class;
    }

    /**
     * The column <code>PUBLIC.PERSON.PARTY_ID</code>.
     */
    public final TableField<PersonRecord, Long> PARTY_ID = createField("PARTY_ID", org.jooq.impl.SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>PUBLIC.PERSON.FIRST_NAME</code>.
     */
    public final TableField<PersonRecord, String> FIRST_NAME = createField("FIRST_NAME", org.jooq.impl.SQLDataType.VARCHAR(1000).nullable(false), this, "");

    /**
     * The column <code>PUBLIC.PERSON.LAST_NAME</code>.
     */
    public final TableField<PersonRecord, String> LAST_NAME = createField("LAST_NAME", org.jooq.impl.SQLDataType.VARCHAR(1000).nullable(false), this, "");

    /**
     * The column <code>PUBLIC.PERSON.MIDDLE_NAME</code>.
     */
    public final TableField<PersonRecord, String> MIDDLE_NAME = createField("MIDDLE_NAME", org.jooq.impl.SQLDataType.VARCHAR(1000), this, "");

    /**
     * The column <code>PUBLIC.PERSON.BIRTH_DATE</code>.
     */
    public final TableField<PersonRecord, Date> BIRTH_DATE = createField("BIRTH_DATE", org.jooq.impl.SQLDataType.DATE.nullable(false), this, "");

    /**
     * The column <code>PUBLIC.PERSON.ADDRESS</code>.
     */
    public final TableField<PersonRecord, String> ADDRESS = createField("ADDRESS", org.jooq.impl.SQLDataType.VARCHAR(1000).nullable(false), this, "");

    /**
     * Create a <code>PUBLIC.PERSON</code> table reference
     */
    public Person() {
        this(DSL.name("PERSON"), null);
    }

    /**
     * Create an aliased <code>PUBLIC.PERSON</code> table reference
     */
    public Person(String alias) {
        this(DSL.name(alias), PERSON);
    }

    /**
     * Create an aliased <code>PUBLIC.PERSON</code> table reference
     */
    public Person(Name alias) {
        this(alias, PERSON);
    }

    private Person(Name alias, Table<PersonRecord> aliased) {
        this(alias, aliased, null);
    }

    private Person(Name alias, Table<PersonRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> Person(Table<O> child, ForeignKey<O, PersonRecord> key) {
        super(child, key, PERSON);
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
        return Arrays.<Index>asList(Indexes.PRIMARY_KEY_8);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<PersonRecord, Long> getIdentity() {
        return Keys.IDENTITY_PERSON;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<PersonRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_8;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<PersonRecord>> getKeys() {
        return Arrays.<UniqueKey<PersonRecord>>asList(Keys.CONSTRAINT_8);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<PersonRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<PersonRecord, ?>>asList(Keys.PERSON_FK0);
    }

    public Party party() {
        return new Party(this, Keys.PERSON_FK0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Person as(String alias) {
        return new Person(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Person as(Name alias) {
        return new Person(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Person rename(String name) {
        return new Person(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Person rename(Name name) {
        return new Person(name, null);
    }
}
