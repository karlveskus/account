package com.tuum.account.dto;

import com.tuum.account.domain.TransactionDirection;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record TransactionDto(
        UUID accountId,
        UUID transactionID,
        BigDecimal amount,
        String currency,
        TransactionDirection direction,
        String description
) {
}
