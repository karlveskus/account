package com.tuum.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuum.account.domain.TransactionDirection;
import com.tuum.account.dto.AccountDto;
import com.tuum.account.dto.CreateAccountRequest;
import com.tuum.account.dto.CreateTransactionRequest;
import com.tuum.account.dto.TransactionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.PostgreSQLContainer;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = Initializer.class)
@Sql(scripts = "/clear_database.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public abstract class IntegrationTestBase {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    AccountDto createAccountAndReturn(String customerId, List<String> currencies) throws Exception {
        String responseAsString = createAccount(customerId, currencies)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(responseAsString, AccountDto.class);
    }

    ResultActions createAccount(String customerId, List<String> currencies) throws Exception {
        CreateAccountRequest createAccountRequest = CreateAccountRequest.builder()
                .customerId(customerId)
                .country("EST")
                .currencies(currencies)
                .build();

        return mockMvc.perform(
                post("/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAccountRequest)));
    }

    AccountDto getAccountAndReturn(UUID accountId) throws Exception {
        String responseAsString = getAccount(accountId)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(responseAsString, AccountDto.class);
    }

    ResultActions getAccount(UUID accountId) throws Exception {
        return mockMvc.perform(
                get("/v1/accounts/" + accountId.toString()));
    }

    TransactionResult createTransactionAndReturn(String accountId, BigDecimal amount, String currency, TransactionDirection direction, String description) throws Exception {
        String responseAsString = createTransaction(accountId, amount, currency, direction, description)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(responseAsString, TransactionResult.class);
    }

    ResultActions createTransaction(String accountId, BigDecimal amount, String currency, TransactionDirection direction, String description) throws Exception {
        CreateTransactionRequest request = CreateTransactionRequest.builder()
                .accountId(accountId)
                .amount(amount)
                .currency(currency)
                .direction(direction)
                .description(description)
                .build();

        return mockMvc.perform(
                post("/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));
    }
}

class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final PostgreSQLContainer<?> POSTGRES_DB_CONTAINER = new PostgreSQLContainer<>("postgres:16");

    static {
        POSTGRES_DB_CONTAINER.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        TestPropertyValues.of(
                "spring.datasource.url=" + POSTGRES_DB_CONTAINER.getJdbcUrl(),
                "spring.datasource.username=" + POSTGRES_DB_CONTAINER.getUsername(),
                "spring.datasource.password=" + POSTGRES_DB_CONTAINER.getPassword()
        ).applyTo(applicationContext.getEnvironment());
    }
}
