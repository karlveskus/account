package com.tuum.account.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuum.account.messaging.event.TuumEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;

public abstract class EventPublisher {

    public abstract List<Class<? extends TuumEvent>> getTypesToPublish();

    public void publish(TuumEvent event) {
        try {
            getRabbitTemplate().convertAndSend(getExchangeName(), "", getObjectMapper().writeValueAsString(event));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    abstract String getExchangeName();

    abstract RabbitTemplate getRabbitTemplate();

    abstract ObjectMapper getObjectMapper();

}
