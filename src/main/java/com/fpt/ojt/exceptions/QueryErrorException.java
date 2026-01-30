package com.fpt.ojt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class QueryErrorException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public QueryErrorException() {
        super("Query execution error!");
    }

    public QueryErrorException(final String message) {
        super(message);
    }
}
