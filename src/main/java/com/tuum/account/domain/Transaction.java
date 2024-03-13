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
public class Transaction {

    @Builder.Default
    private UUID id = UUID.randomUUID();

    private UUID accountId;

    private Long amountCents;

    private String currencyCode;

    private TransactionDirection direction;

    private String description;

    public BigDecimal getAmount() {
        int scale = Currency.getInstance(currencyCode).getDefaultFractionDigits();

        return BigDecimal.valueOf(amountCents, scale);
    }

    public void setAmount(BigDecimal amount) {
        this.amountCents = BigDecimalUtils.convertBigDecimalToLong(amount);
    }

    public static class TransactionBuilder {
        public Transaction.TransactionBuilder amount(BigDecimal amount) {
            this.amountCents = BigDecimalUtils.convertBigDecimalToLong(amount);

            return this;
        }
    }

}
