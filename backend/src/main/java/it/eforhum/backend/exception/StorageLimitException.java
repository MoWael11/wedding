package it.eforhum.backend.exception;

public class StorageLimitException extends RuntimeException {

    public StorageLimitException(String message) {
        super(message);
    }
}
