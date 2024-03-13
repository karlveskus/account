package com.tuum.account.service;

import com.tuum.account.domain.Balance;
import com.tuum.account.dto.enumeration.ErrorCode;
import com.tuum.account.exception.BadRequestException;
import com.tuum.account.exception.NotFoundException;
import com.tuum.account.mapper.BalanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    private final BalanceMapper balanceMapper;

    public Balance initializeBalance(UUID accountId, String currencyCode) {
        validateCurrency(currencyCode);

        Balance balance = composeInitialBalance(accountId, currencyCode);
        balanceMapper.insert(balance);

        return balance;
    }

    public List<Balance> initializeBalances(UUID accountId, List<String> currencies) {
        return currencies.stream()
                .map(currency -> initializeBalance(accountId, currency))
                .collect(Collectors.toList());
    }

    public List<Balance> getBalancesByAccountId(UUID accountId) {
        return balanceMapper.getBalancesByAccountId(accountId);
    }

    public Balance getBalanceByAccountIdAndCurrency(UUID accountId, String currency) {
        validateCurrency(currency);

        Balance balance = balanceMapper.getBalanceByAccountIdAndCurrency(accountId, currency);

        if (balance == null) {
            throw new NotFoundException(ErrorCode.BALANCE_NOT_FOUND, "Account " + accountId + " has no balance with currency " + currency);
        }

        return balance;
    }

    public void updateBalance(Balance balance) {
        balanceMapper.update(balance);
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