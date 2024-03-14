package com.tuum.account.service;

import com.tuum.account.dao.AccountDao;
import com.tuum.account.domain.Account;
import com.tuum.account.domain.Balance;
import com.tuum.account.dto.CreateAccountRequest;
import com.tuum.account.dto.enumeration.ErrorCode;
import com.tuum.account.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountDao accountDao;
    private final BalanceService balanceService;
    private final EventPublisherService eventPublisherService;
    private final TransactionRunner transactionRunner;

    @Transactional
    public Account createAccount(CreateAccountRequest createAccountRequest) {
        Account account = transactionRunner.runInTransaction(() -> {
            Account newAccount = composeAccount(createAccountRequest);

            accountDao.insert(newAccount);

            List<Balance> balances = balanceService.initializeBalances(newAccount.getId(), createAccountRequest.currencies());

            newAccount.setBalances(balances);

            return newAccount;
        });

        eventPublisherService.publishAccountCreated(account);

        for (Balance balance : account.getBalances()) {
            eventPublisherService.publishBalanceCreated(balance);
        }

        return account;
    }

    public Account getAccount(UUID id) {
        Account account = accountDao.getAccount(id);

        if (account == null) {
            throw new NotFoundException(ErrorCode.ACCOUNT_NOT_FOUND, "Account not found");
        }

        account.setBalances(balanceService.getBalancesByAccountId(account.getId()));

        return account;
    }

    private Account composeAccount(CreateAccountRequest createAccountRequest) {
        return Account.builder()
                .customerId(UUID.fromString(createAccountRequest.customerId()))
                .country(createAccountRequest.country())
                .build();
    }
}
