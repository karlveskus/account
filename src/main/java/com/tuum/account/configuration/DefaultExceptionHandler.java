package com.tuum.account.configuration;

import com.tuum.account.dto.ErrorMessageResponse;
import com.tuum.account.dto.enumeration.ErrorCode;
import com.tuum.account.exception.TuumException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { TuumException.class})
    protected ResponseEntity<ErrorMessageResponse> handleException(TuumException e) {
        log.info(e.getMessage(), e);

        ErrorMessageResponse response = ErrorMessageResponse.builder()
                .errorCode(e.getErrorCode())
                .build();

        return ResponseEntity
                .status(e.getStatus())
                .body(response);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.info(e.getMessage(), e);

        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorMessageResponse response = ErrorMessageResponse.builder()
                .errorCode(ErrorCode.INVALID_REQUEST)
                .data(errors)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<ErrorMessageResponse> handleException(Exception e) {
        log.error(e.getMessage(), e);

        ErrorMessageResponse response = ErrorMessageResponse.builder()
                .errorCode(ErrorCode.UNEXPECTED)
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

}
