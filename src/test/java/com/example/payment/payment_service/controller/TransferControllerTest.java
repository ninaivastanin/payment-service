package com.example.payment.payment_service.controller;

import com.example.payment.payment_service.dto.TransferRequest;
import com.example.payment.payment_service.dto.TransferResponse;
import com.example.payment.payment_service.entity.TransferStatus;
import com.example.payment.payment_service.exception.InvalidTransferAccountsException;
import com.example.payment.payment_service.service.TransferService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransferController.class)
class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TransferService transferService;

    @Test
    void shouldReturnCreatedWhenTransferIsSuccessful() throws Exception {

        TransferRequest request = new TransferRequest(
                1L,
                2L,
                BigDecimal.valueOf(100)
        );

        TransferResponse response = new TransferResponse(
                UUID.randomUUID(),
                TransferStatus.SUCCESS,
                "Transfer completed successfully",
                1L,
                2L,
                BigDecimal.valueOf(100)
        );

        when(transferService.transfer(any(TransferRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("SUCCESS"));

        verify(transferService)
                .transfer(any(TransferRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenSameAccountTransfer() throws Exception {

        TransferRequest request = new TransferRequest(
                1L,
                1L,
                BigDecimal.valueOf(100)
        );

        when(transferService.transfer(any()))
                .thenThrow(new InvalidTransferAccountsException(1L));

        mockMvc.perform(post("/api/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestForNegativeAmount() throws Exception {

        TransferRequest request = new TransferRequest(
                1L,
                2L,
                BigDecimal.valueOf(-10)
        );

        mockMvc.perform(post("/api/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(transferService);
    }

    @Test
    void shouldReturnAllTransfers() throws Exception {

        List<TransferResponse> transfers = List.of(
                new TransferResponse(
                        UUID.randomUUID(),
                        TransferStatus.SUCCESS,
                        "Transfer found",
                        1L,
                        2L,
                        BigDecimal.valueOf(100)
                )
        );

        when(transferService.getAllTransfers())
                .thenReturn(transfers);

        mockMvc.perform(get("/api/transfers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].sourceAccountId").value(1))
                .andExpect(jsonPath("$[0].destinationAccountId").value(2))
                .andExpect(jsonPath("$[0].amount").value(100));

        verify(transferService).getAllTransfers();
    }
}
