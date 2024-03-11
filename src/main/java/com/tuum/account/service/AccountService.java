package com.tuum.account.service;

import com.tuum.account.domain.Account;
import com.tuum.account.dto.CreateAccountRequest;
import com.tuum.account.dto.enumeration.ErrorCode;
import com.tuum.account.exception.NotFoundException;
import com.tuum.account.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountMapper accountDao;

    public Account createAccount(CreateAccountRequest createAccountRequest) {
        Account account = Account.builder()
                .customerId(UUID.fromString(createAccountRequest.customerId()))
                .country(createAccountRequest.country())
                .build();

        accountDao.save(account);

        return account;
    }

    public Account getAccount(UUID id) {
        Account account = accountDao.getAccount(id);

        if (account == null) {
            throw new NotFoundException(ErrorCode.ACCOUNT_NOT_FOUND, "Account not found");
        }

        return account;
    }
}
