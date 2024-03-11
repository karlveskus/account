package com.tuum.account.controller;

import com.tuum.account.domain.Account;
import com.tuum.account.domain.Balance;
import com.tuum.account.dto.AccountDto;
import com.tuum.account.dto.BalanceDto;
import com.tuum.account.dto.CreateAccountRequest;
import com.tuum.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@RequestBody @Valid CreateAccountRequest request) {
        Account account = accountService.createAccount(request);

        AccountDto accountDto = mapAccountToDto(account);

        return ResponseEntity.ok(accountDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable UUID id) {
        Account account = accountService.getAccount(id);

        AccountDto accountDto = mapAccountToDto(account);

        return ResponseEntity.ok(accountDto);
    }

    private AccountDto mapAccountToDto(Account account) {
        List<BalanceDto> balances = account.getBalances().stream()
                .map(this::mapBalanceToDto)
                .collect(Collectors.toList());

        return AccountDto.builder()
                .id(account.getId())
                .customerId(account.getCustomerId())
                .balances(balances)
                .build();
    }

    private BalanceDto mapBalanceToDto(Balance balance) {
        return BalanceDto.builder()
                .availableAmount(balance.getAvailableAmount())
                .currency(balance.getCurrencyCode())
                .build();
    }

}
