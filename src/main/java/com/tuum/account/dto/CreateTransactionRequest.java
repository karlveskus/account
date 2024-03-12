package com.tuum.account.dto;

import com.tuum.account.domain.TransactionDirection;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CreateTransactionRequest(

        @NotNull
        @ValidUUID(message = "invalid value")
        String accountId,

        @NotNull
        BigDecimal amount,

        @NotEmpty
        String currency,

        @NotNull
        TransactionDirection direction,

        @NotEmpty
        String description

) {}
