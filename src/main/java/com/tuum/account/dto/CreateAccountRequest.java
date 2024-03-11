package com.tuum.account.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateAccountRequest(
        @NotNull
        @ValidUUID(message = "invalid format")
        String customerId,

        @NotNull
        String country
) {}
