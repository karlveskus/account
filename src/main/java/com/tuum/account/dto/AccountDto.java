package com.tuum.account.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AccountDto(
        UUID id,
        UUID customerId
) {}
