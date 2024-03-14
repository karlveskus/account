package com.tuum.account.messaging.event;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
public class BalanceUpdated extends TuumEvent {
    private UUID id;
    private BigDecimal amount;
}
