package com.tuum.account.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record CreateAccountRequest(
        @NotNull
        @ValidUUID(message = "invalid value")
        String customerId,

        @NotNull
        @Size(min = 3, message = "alpha-3 code required")
        @Size(max = 3, message = "alpha-3 code required")
        String country,

        @NotEmpty
        List<String> currencies
) {}
