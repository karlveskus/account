package com.tuum.account.domain;

import lombok.*;

import java.math.BigDecimal;
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

    private BigDecimal amount;

    private TransactionDirection direction;

    private String description;

}
