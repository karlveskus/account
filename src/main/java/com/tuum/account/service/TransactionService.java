package com.tuum.account.service;

import com.tuum.account.domain.Account;
import com.tuum.account.domain.Balance;
import com.tuum.account.domain.Transaction;
import com.tuum.account.domain.TransactionDirection;
import com.tuum.account.dto.CreateTransactionRequest;
import com.tuum.account.dto.TransactionResult;
import com.tuum.account.dto.enumeration.ErrorCode;
import com.tuum.account.exception.BadRequestException;
import com.tuum.account.dao.TransactionDao;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionDao transactionDao;
    private final BalanceService balanceService;
    private final AccountService accountService;

    @Transactional
    @Retryable(retryFor = OptimisticLockingFailureException.class, backoff = @Backoff(delay = 100))
    public TransactionResult createTransaction(UUID accountId, CreateTransactionRequest request) {
        Account account = accountService.getAccount(accountId);
        Balance balance = balanceService.getBalanceByAccountIdAndCurrency(account.getId(), request.currency());

        validateTransactionAmount(request.amount());
        validateSufficientFunds(balance, request.amount(), request.direction());

        Transaction transaction = composeTransaction(accountId, request);
        insertTransaction(transaction);

        BigDecimal newBalance = calculateNewBalance(balance, request.amount(), request.direction());
        updateBalance(balance, newBalance);

        return buildTransactionResult(request, accountId, newBalance, transaction.getId());
    }

    public List<Transaction> getTransactions(UUID accountId) {
        Account account = accountService.getAccount(accountId);

        return transactionDao.getTransactionsByAccountId(account.getId());
    }

    private void validateTransactionAmount(BigDecimal amount) {
        if (isNegativeAmount(amount)) {
            throw new BadRequestException(ErrorCode.INVALID_AMOUNT, "Negative amount not allowed");
        }
    }

    private void validateSufficientFunds(Balance balance, BigDecimal transactionAmount, TransactionDirection direction) {
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

    private BigDecimal calculateNewBalance(Balance balance, BigDecimal transactionAmount, TransactionDirection direction) {
        if (direction.equals(TransactionDirection.IN)) {
            return balance.getAvailableAmount().add(transactionAmount);
        } else {
            return balance.getAvailableAmount().subtract(transactionAmount);
        }
    }

    private void updateBalance(Balance balance, BigDecimal newBalance) {
        balance.setAvailableAmount(newBalance);
        balanceService.updateBalance(balance);
    }

    private Transaction composeTransaction(UUID accountId, CreateTransactionRequest request) {
        return Transaction.builder()
                .accountId(accountId)
                .amount(request.amount())
                .currencyCode(request.currency())
                .direction(request.direction())
                .description(request.description())
                .build();
    }

    private void insertTransaction(Transaction transaction) {
        transactionDao.insert(transaction);
    }

    private TransactionResult buildTransactionResult(CreateTransactionRequest request, UUID accountId, BigDecimal newBalance, UUID transactionId) {
        return TransactionResult.builder()
                .accountId(accountId)
                .transactionID(transactionId)
                .amount(request.amount())
                .currency(request.currency())
                .direction(request.direction())
                .description(request.description())
                .newBalance(newBalance)
                .build();
    }
}
