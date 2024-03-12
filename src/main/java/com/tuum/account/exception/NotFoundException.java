package com.tuum.account.exception;

import com.tuum.account.dto.enumeration.ErrorCode;
import org.springframework.http.HttpStatus;

public class NotFoundException extends TuumException {

    public NotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, HttpStatus.NOT_FOUND, message);
    }

}
