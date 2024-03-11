package com.tuum.account.dto;

import com.tuum.account.dto.enumeration.ErrorCode;
import lombok.Builder;

@Builder
public record ErrorMessageResponse(

    ErrorCode errorCode,

    Object data

) {}
