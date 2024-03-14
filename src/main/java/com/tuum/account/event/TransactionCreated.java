package com.tuum.account.event;

import com.tuum.account.domain.TransactionDirection;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
public class TransactionCreated extends TuumEvent {
    private UUID balanceId;
    private UUID accountId;
    private UUID transactionID;
    private BigDecimal amount;
    private String currency;
    private TransactionDirection direction;
    private String description;
    private BigDecimal newBalance;
}
