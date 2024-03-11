package com.tuum.account.domain;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Builder.Default
    private UUID id = UUID.randomUUID();

    private UUID customerId;

    private String country;

    private List<Balance> balances;

}
