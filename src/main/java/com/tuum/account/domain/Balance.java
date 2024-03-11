package com.tuum.account.domain;

import lombok.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Balance {

    @Builder.Default
    private UUID id = UUID.randomUUID();

    private UUID accountId;

    private Long availableAmount;

    private String currencyCode;

    public BigDecimal getAvailableAmount() {
        int scale = Currency.getInstance(currencyCode).getDefaultFractionDigits();

        return BigDecimal.valueOf(availableAmount, scale);
    }

}
