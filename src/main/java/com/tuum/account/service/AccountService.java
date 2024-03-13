package com.tuum.account.service;

import com.tuum.account.domain.Account;
import com.tuum.account.domain.Balance;
import com.tuum.account.dto.CreateAccountRequest;
import com.tuum.account.dto.enumeration.ErrorCode;
import com.tuum.account.exception.NotFoundException;
import com.tuum.account.dao.AccountDao;
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

    @Transactional
    public Account createAccount(CreateAccountRequest createAccountRequest) {
        Account account = composeAccount(createAccountRequest);

        accountDao.insert(account);

        List<Balance> balances = balanceService.initializeBalances(account.getId(), createAccountRequest.currencies());

        account.setBalances(balances);

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
