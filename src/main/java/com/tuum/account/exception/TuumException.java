package com.tuum.account.exception;

import com.tuum.account.dto.enumeration.ErrorCode;

public class TuumException extends RuntimeException {

    private final ErrorCode errorCode;

    public TuumException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public TuumException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
