package com.tuum.account.dto;

import com.tuum.account.domain.TransactionDirection;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record TransactionResult(
        UUID balanceId,
        UUID accountId,
        UUID transactionID,
        BigDecimal amount,
        String currency,
        TransactionDirection direction,
        String description,
        BigDecimal newBalance
) {
}
