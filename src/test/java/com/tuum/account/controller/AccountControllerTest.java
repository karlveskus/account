package com.tuum.account.controller;

import com.tuum.account.dto.AccountDto;
import com.tuum.account.dto.BalanceDto;
import com.tuum.account.dto.ErrorMessageResponse;
import com.tuum.account.dto.enumeration.ErrorCode;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountControllerTest extends IntegrationTestBase {

    @Test
    void createAccount() throws Exception {
        String customerId = UUID.randomUUID().toString();
        List<String> currencies = List.of("EUR", "USD");

        AccountDto accountDto = createAccountAndReturn(customerId, currencies);

        assertThat(accountDto.id()).isNotNull();
        assertThat(accountDto.customerId().toString()).isEqualTo(customerId);
        assertThat(accountDto.balances()).extracting(
                BalanceDto::currency,
                BalanceDto::availableAmount
        ).containsExactlyInAnyOrder(
                Tuple.tuple("EUR", BigDecimal.valueOf(0, 2)),
                Tuple.tuple("USD", BigDecimal.valueOf(0, 2))
        );
    }

    @Test
    void createAccount_CurrencyNotAllowed() throws Exception {
        String customerId = UUID.randomUUID().toString();
        List<String> currencies = List.of("EUR", "MXN");

        String contentAsString = createAccount(customerId, currencies)
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse().getContentAsString();

        ErrorMessageResponse response = objectMapper.readValue(contentAsString, ErrorMessageResponse.class);

        assertThat(response.errorCode()).isEqualTo(ErrorCode.CURRENCY_NOT_ALLOWED);
    }

    @Test
    void createAccount_MissingCustomerId() throws Exception {
        String customerId = null;
        List<String> currencies = List.of("EUR", "MXN");

        String contentAsString = createAccount(customerId, currencies)
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse().getContentAsString();

        ErrorMessageResponse response = objectMapper.readValue(contentAsString, ErrorMessageResponse.class);

        assertThat(response.errorCode()).isEqualTo(ErrorCode.INVALID_REQUEST);
    }

    @Test
    void getAccount() throws Exception {
        String customerId = UUID.randomUUID().toString();
        List<String> currencies = List.of("EUR", "JPY");

        AccountDto accountDto = createAccountAndReturn(customerId, currencies);

        accountDto = getAccountAndReturn(accountDto.id());

        assertThat(accountDto.id()).isNotNull();
        assertThat(accountDto.customerId().toString()).isEqualTo(customerId);
        assertThat(accountDto.balances()).extracting(
                BalanceDto::currency,
                BalanceDto::availableAmount
        ).containsExactlyInAnyOrder(
                Tuple.tuple("EUR", BigDecimal.valueOf(0, 2)),
                Tuple.tuple("JPY", BigDecimal.valueOf(0, 0))
        );
    }

    @Test
    void getAccount_NotFound() throws Exception {
        String responseAsString = getAccount(UUID.randomUUID())
                .andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();

        ErrorMessageResponse response = objectMapper.readValue(responseAsString, ErrorMessageResponse.class);

        assertThat(response.errorCode()).isEqualTo(ErrorCode.ACCOUNT_NOT_FOUND);
    }

}
