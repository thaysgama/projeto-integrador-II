package com.br.digitalmoney.accountservice.accounts.controller;

import com.br.digitalmoney.accountservice.accounts.assembler.AccountMapper;
import com.br.digitalmoney.accountservice.accounts.domain.Account;
import com.br.digitalmoney.accountservice.accounts.dto.request.AccountRequest;
import com.br.digitalmoney.accountservice.accounts.dto.response.AccountResponse;
import com.br.digitalmoney.accountservice.accounts.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static com.br.digitalmoney.utils.AccountDataTest.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String URL = "/accounts";

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AccountMapper accountMapper;

    @Test
    public void whenCreateAccountShouldReturnCreated() throws Exception {
        // Arrange
        final var account = new Account(
                1L,
                1L,
                "DEPOSIT",
                0.0
        );

        final var accountRequest = new AccountRequest(
                1L,
                "DEPOSIT",
                0.0
        );
        final var bodyRequest = mapper.writeValueAsString(accountRequest);

        //final var user = new UserResponse(1L);

        when(accountService.create(any())).thenReturn(account);

        // Act, Assert
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyRequest))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenGetAccountShouldReturnSuccess() throws Exception {
        Account account = getDefaultAccount();
        AccountResponse accountResponse = getDefaultAccountResponse();

        when(accountService.findByID(any())).thenReturn(account);
        when(accountMapper.fromAccountToAccountResponse(any(Account.class))).thenReturn(accountResponse);

        mockMvc.perform(get(URL + "/" + account.id())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(account.id().intValue())));
    }

    @Test
    public void whenUpdateAccountShouldReturnSuccess() throws Exception {
        Account account = getDefaultAccount();
        AccountRequest accountRequest = getDefaultAccountRequest();

        when(accountService.findByID(any())).thenReturn(account);
        when(accountMapper.fromAccountRequestToAccount(any(AccountRequest.class))).thenReturn(account);

        final var bodyRequest = mapper.writeValueAsString(accountRequest);

        mockMvc.perform(patch(URL + "/" + account.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyRequest))
                .andExpect(status().isNoContent());
    }
}
