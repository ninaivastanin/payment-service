package com.example.payment.payment_service.controller;

import com.example.payment.payment_service.dto.TransferRequest;
import com.example.payment.payment_service.dto.TransferResponse;
import com.example.payment.payment_service.entity.TransferStatus;
import com.example.payment.payment_service.service.TransferService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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
                "Transfer completed successfully"
        );

        when(transferService.transfer(any(TransferRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("SUCCESS"));
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
    }
}
