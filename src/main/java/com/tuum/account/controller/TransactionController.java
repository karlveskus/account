package com.tuum.account.controller;

import com.tuum.account.domain.Transaction;
import com.tuum.account.dto.CreateTransactionRequest;
import com.tuum.account.dto.TransactionDto;
import com.tuum.account.dto.TransactionResult;
import com.tuum.account.dto.TransactionsResponse;
import com.tuum.account.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/accounts/{accountId}/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResult> createTransaction(@PathVariable UUID accountId, @RequestBody @Valid CreateTransactionRequest request) {
        TransactionResult transactionResult = transactionService.createTransaction(accountId, request);

        return ResponseEntity.ok(transactionResult);
    }

    @GetMapping
    public ResponseEntity<TransactionsResponse> getTransactions(@PathVariable UUID accountId) {
        List<TransactionDto> transactions = transactionService.getTransactions(accountId).stream()
                .map(this::mapTransactionToDto)
                .collect(Collectors.toList());

        TransactionsResponse response = TransactionsResponse.builder()
                .transactions(transactions)
                .build();

        return ResponseEntity.ok(response);
    }

    private TransactionDto mapTransactionToDto(Transaction transaction) {
        return TransactionDto.builder()
                .accountId(transaction.getAccountId())
                .transactionID(transaction.getId())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrencyCode())
                .direction(transaction.getDirection())
                .description(transaction.getDescription())
                .build();
    }

}
