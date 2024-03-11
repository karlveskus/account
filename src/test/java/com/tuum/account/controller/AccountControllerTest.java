package com.tuum.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuum.account.dto.AccountDto;
import com.tuum.account.dto.CreateAccountRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createAccount() throws Exception {
        AccountDto accountDto = createAccountAndReturn();

        assertThat(accountDto.id()).isNotNull();
    }

    @Test
    void getAccount() throws Exception {
        AccountDto accountDto = createAccountAndReturn();

        String getAccountResponseAsString = mockMvc.perform(
                        get("/v1/accounts/" + accountDto.id()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AccountDto getAccountResponse = objectMapper.readValue(getAccountResponseAsString, AccountDto.class);

        assertThat(getAccountResponse.id()).isEqualTo(accountDto.id());
    }

    private AccountDto createAccountAndReturn() throws Exception {
        CreateAccountRequest createAccountRequest = CreateAccountRequest.builder()
                .customerId(UUID.randomUUID().toString())
                .country("EE")
                .build();

        String createAccountResponseAsString = mockMvc.perform(
                        post("/v1/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createAccountRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(createAccountResponseAsString, AccountDto.class);
    }
}
