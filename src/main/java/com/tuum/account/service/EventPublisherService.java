package com.tuum.account.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuum.account.domain.Account;
import com.tuum.account.domain.Balance;
import com.tuum.account.dto.TransactionResult;
import com.tuum.account.event.*;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventPublisherService {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final Exchange accountExchange;
    private final Exchange balanceExchange;
    private final Exchange transactionExchange;

    public void publishAccountCreated(Account account) {
        AccountCreated accountCreated = AccountCreated.builder()
                .accountId(account.getId())
                .customerId(account.getCustomerId())
                .country(account.getCountry())
                .build();

        publish(accountExchange, accountCreated);
    }

    public void publishBalanceCreated(Balance balance) {
        BalanceCreated balanceCreated = BalanceCreated.builder()
                .id(balance.getId())
                .accountId(balance.getAccountId())
                .amount(balance.getAvailableAmount())
                .currencyCode(balance.getCurrencyCode())
                .build();

        publish(balanceExchange, balanceCreated);
    }

    public void publishBalanceUpdated(TransactionResult transactionResult) {
        BalanceUpdated balanceUpdated = BalanceUpdated.builder()
                .id(transactionResult.balanceId())
                .amount(transactionResult.newBalance())
                .build();

        publish(balanceExchange, balanceUpdated);

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

        publish(transactionExchange, balanceUpdated);
    }

    public void publish(Exchange exchange, TuumEvent event) {
        try {
            rabbitTemplate.convertAndSend(exchange.getName(), "", objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
