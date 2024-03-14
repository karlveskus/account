package com.tuum.account.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    FanoutExchange accountExchange(@Value("${rabbitmq.exchanges.account}") String accountExchange) {
        return new FanoutExchange(accountExchange);
    }

    @Bean
    FanoutExchange balanceExchange(@Value("${rabbitmq.exchanges.balance}") String balanceExchange) {
        return new FanoutExchange(balanceExchange);
    }

    @Bean
    FanoutExchange transactionExchange(@Value("${rabbitmq.exchanges.transaction}") String transactionExchange) {
        return new FanoutExchange(transactionExchange);
    }

}
