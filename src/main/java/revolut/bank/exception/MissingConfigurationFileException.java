package revolut.bank.exception;

/**
 * todo
 */
public class MissingConfigurationFileException extends RuntimeException {

    public MissingConfigurationFileException() {
    }

    public MissingConfigurationFileException(String message) {
        super(message);
    }

    public MissingConfigurationFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
