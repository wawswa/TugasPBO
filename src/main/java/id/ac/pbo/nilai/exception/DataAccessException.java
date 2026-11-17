package id.ac.pbo.nilai.exception;

/**
 * Custom unchecked exception untuk kegagalan operasi basis data.
 */
public class DataAccessException extends RuntimeException {

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
