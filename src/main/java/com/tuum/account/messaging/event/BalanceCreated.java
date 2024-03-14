package com.tuum.account.messaging.event;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
public class BalanceCreated extends TuumEvent {
    private UUID id;
    private UUID accountId;
    private BigDecimal amount;
    private String currencyCode;
}
