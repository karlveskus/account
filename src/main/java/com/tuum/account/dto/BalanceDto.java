package com.tuum.account.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record BalanceDto(
        UUID id,
        BigDecimal availableAmount,
        String currency
) {}
