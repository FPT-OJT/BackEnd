package com.fpt.ojt.exceptions;

import java.io.Serial;

public class DuplicateException extends RuntimeException {
    public DuplicateException(String message) {
        super(message);
    }

    @Serial
    private static final long serialVersionUID = 1L;

}
