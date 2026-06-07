package com.example.payment.payment_service.controller;

import com.example.payment.payment_service.dto.AccountRequest;
import com.example.payment.payment_service.dto.AccountResponse;
import com.example.payment.payment_service.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AccountService accountService;

    @Test
    void shouldCreateAccountSuccessfully() throws Exception {

        AccountRequest request = new AccountRequest(
                "RS123456",
                BigDecimal.valueOf(500),
                "EUR"
        );

        AccountResponse response = new AccountResponse(
                1L,
                "RS123456",
                BigDecimal.valueOf(500),
                "EUR"
        );

        when(accountService.createAccount(any(AccountRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber").value("RS123456"));
    }

    @Test
    void shouldGetAccountById() throws Exception {

        AccountResponse response = new AccountResponse(
                1L,
                "RS123456",
                BigDecimal.valueOf(500),
                "EUR"
        );

        when(accountService.getAccount(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.accountNumber").value("RS123456"));

        verify(accountService).getAccount(1L);
    }

    @Test
    void shouldReturnAllAccounts() throws Exception {

        List<AccountResponse> accounts = List.of(
                new AccountResponse(
                        1L,
                        "ACC001",
                        BigDecimal.valueOf(1000),
                        "EUR"
                ),
                new AccountResponse(
                        2L,
                        "ACC002",
                        BigDecimal.valueOf(500),
                        "EUR"
                )
        );

        when(accountService.getAllAccounts())
                .thenReturn(accounts);

        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].accountNumber").value("ACC001"))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(accountService).getAllAccounts();
    }
}
