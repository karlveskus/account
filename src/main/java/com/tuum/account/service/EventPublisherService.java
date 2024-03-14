package com.tuum.account.service;

import com.tuum.account.domain.Account;
import com.tuum.account.domain.Balance;
import com.tuum.account.dto.TransactionResult;
import com.tuum.account.messaging.EventPublisher;
import com.tuum.account.messaging.event.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventPublisherService {

    private final List<EventPublisher> eventPublishers;

    public void publishAccountCreated(Account account) {
        AccountCreated accountCreated = AccountCreated.builder()
                .accountId(account.getId())
                .customerId(account.getCustomerId())
                .country(account.getCountry())
                .build();

        publish(accountCreated);
    }

    public void publishBalanceCreated(Balance balance) {
        BalanceCreated balanceCreated = BalanceCreated.builder()
                .id(balance.getId())
                .accountId(balance.getAccountId())
                .amount(balance.getAvailableAmount())
                .currencyCode(balance.getCurrencyCode())
                .build();

        publish(balanceCreated);
    }

    public void publishBalanceUpdated(TransactionResult transactionResult) {
        BalanceUpdated balanceUpdated = BalanceUpdated.builder()
                .id(transactionResult.balanceId())
                .amount(transactionResult.newBalance())
                .build();

        publish(balanceUpdated);

    }

    public void publishTransactionCreated(TransactionResult transactionResult) {
        TransactionCreated balanceUpdated = TransactionCreated.builder()
                .balanceId(transactionResult.balanceId())
                .accountId(transactionResult.accountId())
                .transactionID(transactionResult.transactionID())
                .amount(transactionResult.amount())
                .currency(transactionResult.currency())
                .direction(transactionResult.direction())
                .description(transactionResult.description())
                .newBalance(transactionResult.newBalance())
                .build();

        publish(balanceUpdated);
    }

    public void publish(TuumEvent event) {
        List<EventPublisher> publishers = eventPublishers.stream()
                .filter(eventPublisher -> eventPublisher.getTypesToPublish().contains(event.getClass()))
                .toList();

        if (publishers.isEmpty()) throw new RuntimeException("No handler found for " + event);

        for (EventPublisher publisher : publishers) {
            publisher.publish(event);
        }
    }

}
