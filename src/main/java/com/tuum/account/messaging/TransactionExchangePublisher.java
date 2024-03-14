package com.tuum.account.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuum.account.messaging.event.TransactionCreated;
import com.tuum.account.messaging.event.TuumEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TransactionExchangePublisher extends EventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final Exchange transactionExchange;

    @Override
    public List<Class<? extends TuumEvent>> getTypesToPublish() {
        return List.of(TransactionCreated.class);
    }

    @Override
    String getExchangeName() {
        return transactionExchange.getName();
    }

    @Override
    RabbitTemplate getRabbitTemplate() {
        return rabbitTemplate;
    }

    @Override
    ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
