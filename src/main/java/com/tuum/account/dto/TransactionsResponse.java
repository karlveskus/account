package com.tuum.account.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record TransactionsResponse(

    List<TransactionDto> transactions

) { }
