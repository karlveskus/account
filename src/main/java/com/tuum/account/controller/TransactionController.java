package com.tuum.account.controller;

import com.tuum.account.dto.CreateTransactionRequest;
import com.tuum.account.dto.TransactionResult;
import com.tuum.account.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResult> createTransaction(@RequestBody @Valid CreateTransactionRequest request) {
        TransactionResult transactionResult = transactionService.createTransaction(request);

        return ResponseEntity.ok(transactionResult);
    }

}
