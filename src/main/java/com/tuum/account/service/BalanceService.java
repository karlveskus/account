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

import static com.tuum.account.dto.enumeration.ErrorCode.CURRENCY_NOT_ALLOWED;

@Service
@RequiredArgsConstructor
public class BalanceService {

    @Value("#{'${currencies.enabled}'.split(',')}")
    private List<String> enabledCurrencies;

    private final BalanceMapper balanceMapper;

    public Balance initializeBalance(UUID accountId, String currencyCode) {
        if (!enabledCurrencies.contains(currencyCode)) {
            throw new BadRequestException(CURRENCY_NOT_ALLOWED, "Currency code " + currencyCode + " not allowed");
        }

        Balance balance = Balance.builder()
                .accountId(accountId)
                .availableAmount(BigDecimal.ZERO)
                .currencyCode(currencyCode)
                .build();

        balanceMapper.insert(balance);

        return balance;
    }

    public List<Balance> initializeBalances(UUID accountId, List<String> currencies) {
        return currencies.stream()
                .map((currency) -> initializeBalance(accountId, currency))
                .collect(Collectors.toList());
    }

    public List<Balance> getBalancesByAccountId(UUID accountId) {
        return balanceMapper.getBalancesByAccountId(accountId);
    }

    public Balance getBalanceByAccountIdAndCurrency(UUID accountId, String currency) {
        if (!enabledCurrencies.contains(currency)) {
            throw new BadRequestException(CURRENCY_NOT_ALLOWED, "Currency code " + currency + " not allowed");
        }

        Balance balance = balanceMapper.getBalanceByAccountIdAndCurrency(accountId, currency);

        if (balance == null) {
            throw new NotFoundException(ErrorCode.BALANCE_NOT_FOUND, "Account " + accountId + " has no balance with currency " + currency);
        }

        return balance;
    }

    public void updateBalance(Balance balance) {
        balanceMapper.update(balance);
    }
}
