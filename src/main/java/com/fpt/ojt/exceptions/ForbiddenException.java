package com.fpt.ojt.exceptions;

import java.io.Serial;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ForbiddenException() {
        super("Access denied!");
    }

    public ForbiddenException(final String message) {
        super(message);
    }
}
