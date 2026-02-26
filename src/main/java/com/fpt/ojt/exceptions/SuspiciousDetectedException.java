package com.fpt.ojt.exceptions;

import java.io.Serial;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class SuspiciousDetectedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public SuspiciousDetectedException() {
        super("Suspicious activity detected!");
    }

    public SuspiciousDetectedException(final String message) {
        super(message);
    }
}
