package com.tuum.account.domain;

import com.tuum.account.service.BigDecimalUtils;
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

    private Long availableAmountCents;

    private String currencyCode;

    public BigDecimal getAvailableAmount() {
        int scale = Currency.getInstance(currencyCode).getDefaultFractionDigits();

        return BigDecimal.valueOf(availableAmountCents, scale);
    }

    public void setAvailableAmount(BigDecimal amount) {
        this.availableAmountCents = BigDecimalUtils.convertBigDecimalToLong(amount);
    }

    public static class BalanceBuilder {
        public BalanceBuilder availableAmount(BigDecimal amount) {
            this.availableAmountCents = BigDecimalUtils.convertBigDecimalToLong(amount);

            return this;
        }
    }

}
