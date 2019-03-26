package revolut.bank.exception;

/**
 * todo
 */
public class MoneyTransferringException extends Exception {

    public MoneyTransferringException() {
    }

    public MoneyTransferringException(String message) {
        super(message);
    }

    public MoneyTransferringException(String message, Throwable cause) {
        super(message, cause);
    }

    public MoneyTransferringException(Throwable cause) {
        super(cause);
    }
}
