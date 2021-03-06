/*
 * This file is generated by jOOQ.
 */
package revolut.bank.model.generated.tables.records;


import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.UpdatableRecordImpl;

import revolut.bank.model.generated.tables.Transfer;


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
public class TransferRecord extends UpdatableRecordImpl<TransferRecord> implements Record6<Long, String, String, Long, String, Timestamp> {

    private static final long serialVersionUID = -1001673777;

    /**
     * Setter for <code>PUBLIC.TRANSFER.ID</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>PUBLIC.TRANSFER.ID</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>PUBLIC.TRANSFER.FROM_ACCOUNT_ID</code>.
     */
    public void setFromAccountId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>PUBLIC.TRANSFER.FROM_ACCOUNT_ID</code>.
     */
    public String getFromAccountId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>PUBLIC.TRANSFER.TO_ACCOUNT_ID</code>.
     */
    public void setToAccountId(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>PUBLIC.TRANSFER.TO_ACCOUNT_ID</code>.
     */
    public String getToAccountId() {
        return (String) get(2);
    }

    /**
     * Setter for <code>PUBLIC.TRANSFER.AMOUNT</code>.
     */
    public void setAmount(Long value) {
        set(3, value);
    }

    /**
     * Getter for <code>PUBLIC.TRANSFER.AMOUNT</code>.
     */
    public Long getAmount() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>PUBLIC.TRANSFER.STATE</code>.
     */
    public void setState(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>PUBLIC.TRANSFER.STATE</code>.
     */
    public String getState() {
        return (String) get(4);
    }

    /**
     * Setter for <code>PUBLIC.TRANSFER.TIMESTAMP</code>.
     */
    public void setTimestamp(Timestamp value) {
        set(5, value);
    }

    /**
     * Getter for <code>PUBLIC.TRANSFER.TIMESTAMP</code>.
     */
    public Timestamp getTimestamp() {
        return (Timestamp) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<Long, String, String, Long, String, Timestamp> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<Long, String, String, Long, String, Timestamp> valuesRow() {
        return (Row6) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return Transfer.TRANSFER.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return Transfer.TRANSFER.FROM_ACCOUNT_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Transfer.TRANSFER.TO_ACCOUNT_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field4() {
        return Transfer.TRANSFER.AMOUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return Transfer.TRANSFER.STATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field6() {
        return Transfer.TRANSFER.TIMESTAMP;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component2() {
        return getFromAccountId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component3() {
        return getToAccountId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component4() {
        return getAmount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component5() {
        return getState();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp component6() {
        return getTimestamp();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getFromAccountId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getToAccountId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value4() {
        return getAmount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getState();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value6() {
        return getTimestamp();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TransferRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TransferRecord value2(String value) {
        setFromAccountId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TransferRecord value3(String value) {
        setToAccountId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TransferRecord value4(Long value) {
        setAmount(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TransferRecord value5(String value) {
        setState(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TransferRecord value6(Timestamp value) {
        setTimestamp(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TransferRecord values(Long value1, String value2, String value3, Long value4, String value5, Timestamp value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TransferRecord
     */
    public TransferRecord() {
        super(Transfer.TRANSFER);
    }

    /**
     * Create a detached, initialised TransferRecord
     */
    public TransferRecord(Long id, String fromAccountId, String toAccountId, Long amount, String state, Timestamp timestamp) {
        super(Transfer.TRANSFER);

        set(0, id);
        set(1, fromAccountId);
        set(2, toAccountId);
        set(3, amount);
        set(4, state);
        set(5, timestamp);
    }
}
