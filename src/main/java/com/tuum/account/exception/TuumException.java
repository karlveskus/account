package com.tuum.account.exception;

import com.tuum.account.dto.enumeration.ErrorCode;
import org.springframework.http.HttpStatus;

public abstract class TuumException extends RuntimeException {

    private final ErrorCode errorCode;
    private final HttpStatus status;

    public TuumException(ErrorCode errorCode, HttpStatus status, String message) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

}
