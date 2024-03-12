package com.tuum.account.exception;

import com.tuum.account.dto.enumeration.ErrorCode;
import org.springframework.http.HttpStatus;

public class BadRequestException extends TuumException {

    public BadRequestException(ErrorCode errorCode, String message) {
        super(errorCode, HttpStatus.BAD_REQUEST, message);
    }

}
