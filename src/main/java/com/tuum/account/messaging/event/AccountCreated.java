package com.tuum.account.messaging.event;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class AccountCreated extends TuumEvent {
    UUID accountId;
    UUID customerId;
    String country;
}
