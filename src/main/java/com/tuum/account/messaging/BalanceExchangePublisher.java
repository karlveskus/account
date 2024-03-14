package com.tuum.account.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuum.account.messaging.event.BalanceCreated;
import com.tuum.account.messaging.event.BalanceUpdated;
import com.tuum.account.messaging.event.TuumEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BalanceExchangePublisher extends EventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final Exchange balanceExchange;

    @Override
    public List<Class<? extends TuumEvent>> getTypesToPublish() {
        return List.of(BalanceCreated.class, BalanceUpdated.class);
    }

    @Override
    String getExchangeName() {
        return balanceExchange.getName();
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
