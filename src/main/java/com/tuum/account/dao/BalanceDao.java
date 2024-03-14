package com.tuum.account.dao;

import com.tuum.account.domain.Balance;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BalanceDao {

    private final BalanceMapper balanceMapper;

    public List<Balance> getBalancesByAccountId(UUID accountId) {
        return balanceMapper.getBalancesByAccountId(accountId);
    }

    public Balance getBalanceByAccountIdAndCurrency(UUID accountId, String currencyCode) {
        return balanceMapper.getBalanceByAccountIdAndCurrency(accountId, currencyCode);
    }

    public void insert(Balance balance) {
        balanceMapper.insert(balance);
    }

    public void update(Balance balance) {
        int updateCount = balanceMapper.update(balance);

        if (updateCount != 1) {
            throw new OptimisticLockingFailureException("Updating balance failed");
        }
    }

}
