package com.tuum.account.service;

import com.tuum.account.dao.BalanceDao;
import com.tuum.account.domain.Balance;
import com.tuum.account.dto.enumeration.ErrorCode;
import com.tuum.account.exception.BadRequestException;
import com.tuum.account.exception.NotFoundException;
import com.tuum.account.dao.BalanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private static final BigDecimal INITIAL_BALANCE = BigDecimal.ZERO;

    @Value("#{'${currencies.enabled}'.split(',')}")
    private List<String> enabledCurrencies;

    private final BalanceDao balanceDao;

    public Balance initializeBalance(UUID accountId, String currencyCode) {
        validateCurrency(currencyCode);

        Balance balance = composeInitialBalance(accountId, currencyCode);
        balanceDao.insert(balance);

        return balance;
    }

    public List<Balance> initializeBalances(UUID accountId, List<String> currencies) {
        return currencies.stream()
                .map(currency -> initializeBalance(accountId, currency))
                .collect(Collectors.toList());
    }

    public List<Balance> getBalancesByAccountId(UUID accountId) {
        return balanceDao.getBalancesByAccountId(accountId);
    }

    public Balance getBalanceByAccountIdAndCurrency(UUID accountId, String currency) {
        validateCurrency(currency);

        Balance balance = balanceDao.getBalanceByAccountIdAndCurrency(accountId, currency);

        if (balance == null) {
            throw new NotFoundException(ErrorCode.BALANCE_NOT_FOUND, "Account " + accountId + " has no balance with currency " + currency);
        }

        return balance;
    }

    public void updateBalance(Balance balance) {
        balanceDao.update(balance);
    }

    private void validateCurrency(String currencyCode) {
        if (!enabledCurrencies.contains(currencyCode)) {
            throw new BadRequestException(ErrorCode.CURRENCY_NOT_ALLOWED, "Currency code " + currencyCode + " not allowed");
        }
    }

    private Balance composeInitialBalance(UUID accountId, String currencyCode) {
        return Balance.builder()
                .accountId(accountId)
                .availableAmount(INITIAL_BALANCE)
                .currencyCode(currencyCode)
                .build();
    }

}