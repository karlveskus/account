package com.tuum.account.controller;

import com.tuum.account.domain.TransactionDirection;
import com.tuum.account.dto.AccountDto;
import com.tuum.account.dto.ErrorMessageResponse;
import com.tuum.account.dto.TransactionResult;
import com.tuum.account.dto.enumeration.ErrorCode;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransactionControllerTest extends IntegrationTestBase {

    @Test
    void createTwoTransactions() throws Exception {
        AccountDto accountDto = createAccountAndReturn(UUID.randomUUID().toString(), List.of("EUR", "USD"));

        BigDecimal amountIn = BigDecimal.valueOf(12.34);
        BigDecimal amountOut = BigDecimal.valueOf(3.14);
        String currency = "EUR";

        createTransactionAndReturn(
                accountDto.id().toString(),
                amountIn,
                currency,
                TransactionDirection.IN,
                "Top-up");

        TransactionResult transactionResult = createTransactionAndReturn(
                accountDto.id().toString(),
                amountOut,
                currency,
                TransactionDirection.OUT,
                "Pizza");

        assertThat(transactionResult.accountId()).isEqualTo(accountDto.id());
        assertThat(transactionResult.transactionID()).isNotNull();
        assertThat(transactionResult.amount()).isEqualTo(amountOut);
        assertThat(transactionResult.direction()).isEqualTo(TransactionDirection.OUT);
        assertThat(transactionResult.description()).isEqualTo("Pizza");
        assertThat(transactionResult.newBalance()).isEqualTo(amountIn.subtract(amountOut));
    }

    @Test
    void createTransaction_NegativeBalance() throws Exception {
        AccountDto accountDto = createAccountAndReturn(UUID.randomUUID().toString(), List.of("EUR", "USD"));

        BigDecimal amount = BigDecimal.valueOf(12.34).negate();
        String currency = "EUR";
        TransactionDirection direction = TransactionDirection.IN;
        String description = "Top-up";

        String responseAsString = createTransaction(
                accountDto.id().toString(),
                amount,
                currency,
                direction,
                description)
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ErrorMessageResponse response = objectMapper.readValue(responseAsString, ErrorMessageResponse.class);

        assertThat(response.errorCode()).isEqualTo(ErrorCode.INVALID_AMOUNT);
    }

    @Test
    void createTransaction_InsufficientFunds() throws Exception {
        AccountDto accountDto = createAccountAndReturn(UUID.randomUUID().toString(), List.of("EUR", "USD"));

        BigDecimal amount = BigDecimal.valueOf(12.34);
        String currency = "EUR";
        TransactionDirection direction = TransactionDirection.OUT;
        String description = "Top-up";

        String responseAsString = createTransaction(
                accountDto.id().toString(),
                amount,
                currency,
                direction,
                description)
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ErrorMessageResponse response = objectMapper.readValue(responseAsString, ErrorMessageResponse.class);

        assertThat(response.errorCode()).isEqualTo(ErrorCode.INSUFFICIENT_FUNDS);
    }

    @Test
    void createTransaction_InvalidCurrency() throws Exception {
        AccountDto accountDto = createAccountAndReturn(UUID.randomUUID().toString(), List.of("EUR", "USD"));

        BigDecimal amount = BigDecimal.valueOf(12.34);
        String currency = "MXN";
        TransactionDirection direction = TransactionDirection.OUT;
        String description = "Top-up";

        String responseAsString = createTransaction(
                accountDto.id().toString(),
                amount,
                currency,
                direction,
                description)
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ErrorMessageResponse response = objectMapper.readValue(responseAsString, ErrorMessageResponse.class);

        assertThat(response.errorCode()).isEqualTo(ErrorCode.CURRENCY_NOT_ALLOWED);
    }

    @Test
    void createTransaction_MissingDirection() throws Exception {
        AccountDto accountDto = createAccountAndReturn(UUID.randomUUID().toString(), List.of("EUR", "USD"));

        BigDecimal amount = BigDecimal.valueOf(12.34);
        String currency = "JPY";
        TransactionDirection direction = null;
        String description = "Top-up";

        String responseAsString = createTransaction(
                accountDto.id().toString(),
                amount,
                currency,
                direction,
                description)
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ErrorMessageResponse response = objectMapper.readValue(responseAsString, ErrorMessageResponse.class);

        assertThat(response.errorCode()).isEqualTo(ErrorCode.INVALID_REQUEST);
    }

    @Test
    void createTransaction_MissingDescription() throws Exception {
        AccountDto accountDto = createAccountAndReturn(UUID.randomUUID().toString(), List.of("EUR", "USD"));

        BigDecimal amount = BigDecimal.valueOf(12.34);
        String currency = "JPY";
        TransactionDirection direction = TransactionDirection.IN;
        String description = null;

        String responseAsString = createTransaction(
                accountDto.id().toString(),
                amount,
                currency,
                direction,
                description)
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ErrorMessageResponse response = objectMapper.readValue(responseAsString, ErrorMessageResponse.class);

        assertThat(response.errorCode()).isEqualTo(ErrorCode.INVALID_REQUEST);
    }

    @Test
    void createTransaction_AccountNotFound() throws Exception {
        BigDecimal amount = BigDecimal.valueOf(12.34);
        String currency = "EUR";
        TransactionDirection direction = TransactionDirection.OUT;
        String description = "Top-up";

        String responseAsString = createTransaction(
                UUID.randomUUID().toString(),
                amount,
                currency,
                direction,
                description)
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ErrorMessageResponse response = objectMapper.readValue(responseAsString, ErrorMessageResponse.class);

        assertThat(response.errorCode()).isEqualTo(ErrorCode.ACCOUNT_NOT_FOUND);
    }

}
