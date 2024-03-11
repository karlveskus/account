package com.tuum.account.exception;

import com.tuum.account.dto.enumeration.ErrorCode;

public class BadRequestException extends TuumException {

    public BadRequestException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

}
