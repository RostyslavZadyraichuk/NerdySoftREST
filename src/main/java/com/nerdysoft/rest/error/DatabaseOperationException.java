package com.nerdysoft.rest.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class DatabaseOperationException extends RuntimeException {

    public DatabaseOperationException(String message) {
        super("Database operation exception: " + message);
    }

}
