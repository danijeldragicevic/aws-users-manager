package org.example.exception;

public class AwsServiceException extends RuntimeException{

    public AwsServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
