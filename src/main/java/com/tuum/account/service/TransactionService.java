package com.tuum.account.service;

import com.tuum.account.domain.Account;
import com.tuum.account.domain.Balance;
import com.tuum.account.domain.Transaction;
import com.tuum.account.domain.TransactionDirection;
import com.tuum.account.dto.CreateTransactionRequest;
import com.tuum.account.dto.TransactionResult;
import com.tuum.account.dto.enumeration.ErrorCode;
import com.tuum.account.exception.BadRequestException;
import com.tuum.account.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionMapper transactionMapper;
    private final BalanceService balanceService;
    private final AccountService accountService;

    @Transactional
    public TransactionResult createTransaction(CreateTransactionRequest request) {
        Account account = accountService.getAccount(UUID.fromString(request.accountId()));
        Balance balance = balanceService.getBalanceByAccountIdAndCurrency(account.getId(), request.currency());

        validateTransactionAmount(request.amount());
        validateSufficientFunds(request.direction(), balance, request.amount());

        BigDecimal newBalance = calculateNewBalance(request, balance);

        updateBalance(balance, newBalance);

        Transaction transaction = composeTransaction(request);

        insertTransaction(transaction);

        return buildTransactionResult(request, newBalance, transaction.getId());
    }

    private void validateTransactionAmount(BigDecimal amount) {
        if (isNegativeAmount(amount)) {
            throw new BadRequestException(ErrorCode.INVALID_AMOUNT, "Negative amount not allowed");
        }
    }

    private void validateSufficientFunds(TransactionDirection direction, Balance balance, BigDecimal transactionAmount) {
        if (isInsufficientFunds(direction, balance, transactionAmount)) {
            throw new BadRequestException(ErrorCode.INSUFFICIENT_FUNDS, "Insufficient funds");
        }
    }

    private boolean isInsufficientFunds(TransactionDirection direction, Balance balance, BigDecimal transactionAmount) {
        return direction.equals(TransactionDirection.OUT) &&
                balance.getAvailableAmount().compareTo(transactionAmount) < 0;
    }

    private boolean isNegativeAmount(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }

    private BigDecimal calculateNewBalance(CreateTransactionRequest request, Balance balance) {
        if (request.direction().equals(TransactionDirection.IN)) {
            return balance.getAvailableAmount().add(request.amount());
        } else {
            return balance.getAvailableAmount().subtract(request.amount());
        }
    }

    private void updateBalance(Balance balance, BigDecimal newBalance) {
        balance.setAvailableAmount(newBalance);
        balanceService.updateBalance(balance);
    }

    private Transaction composeTransaction(CreateTransactionRequest request) {
        return Transaction.builder()
                .accountId(UUID.fromString(request.accountId()))
                .amount(request.amount())
                .direction(request.direction())
                .description(request.description())
                .build();
    }

    private void insertTransaction(Transaction transaction) {
        transactionMapper.insert(transaction);
    }

    private TransactionResult buildTransactionResult(CreateTransactionRequest request, BigDecimal newBalance, UUID transactionId) {
        return TransactionResult.builder()
                .accountId(UUID.fromString(request.accountId()))
                .transactionID(transactionId)
                .amount(request.amount())
                .currency(request.currency())
                .direction(request.direction())
                .description(request.description())
                .newBalance(newBalance)
                .build();
    }
}
