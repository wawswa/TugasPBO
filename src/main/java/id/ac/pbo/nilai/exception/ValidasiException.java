package id.ac.pbo.nilai.exception;

/**
 * Custom checked exception untuk kesalahan validasi input bisnis.
 */
public class ValidasiException extends Exception {

    public ValidasiException(String message) {
        super(message);
    }

    public ValidasiException(String message, Throwable cause) {
        super(message, cause);
    }
}
