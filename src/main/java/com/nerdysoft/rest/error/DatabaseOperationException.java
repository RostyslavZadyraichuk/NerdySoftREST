package com.nerdysoft.rest.error;

public class DatabaseOperationException extends RuntimeException {

    public DatabaseOperationException(String message) {
        super("Database operation exception: " + message);
    }

}
