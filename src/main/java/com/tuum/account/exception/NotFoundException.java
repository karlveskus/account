package com.tuum.account.exception;

import com.tuum.account.dto.enumeration.ErrorCode;

public class NotFoundException extends TuumException {

    public NotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

}
