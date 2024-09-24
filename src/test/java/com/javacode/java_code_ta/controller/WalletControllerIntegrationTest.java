package com.javacode.java_code_ta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javacode.java_code_ta.dto.WalletDto;
import com.javacode.java_code_ta.exceptions.EntityNotFoundException;
import com.javacode.java_code_ta.model.enums.OperationType;
import com.javacode.java_code_ta.model.models.Wallet;
import com.javacode.java_code_ta.service.WalletService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WalletController.class)
public class WalletControllerIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    private final UUID walletId = UUID.fromString(UUID.randomUUID().toString());

    private final WalletDto walletDto = WalletDto.builder()
            .walletId(walletId)
            .operationType(OperationType.DEPOSIT)
            .amount(1000L)
            .build();

    @SneakyThrows
    @Test
    void editBalance_WhenValidData() {
        when(walletService.editBalance(any())).thenReturn(walletDto);

        mockMvc.perform(post("/api/v1/wallet")
                        .content(objectMapper.writeValueAsString(walletDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.walletId", is(walletDto.getWalletId().toString())))
                        .andExpect(jsonPath("$.operationType", is(walletDto.getOperationType().toString())))
                        .andExpect(jsonPath("$.amount", is(walletDto.getAmount().intValue())));
    }

    @SneakyThrows
    @Test
    void editBalance_WhenInvalidData() {
        WalletDto invalidWalletDto = WalletDto.builder()
                .walletId(null)
                .operationType(null)
                .amount(null)
                .build();

        mockMvc.perform(post("/api/v1/wallet")
                        .content(objectMapper.writeValueAsString(invalidWalletDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                        .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getBalanceById_WhenWalletFound() {
        when(walletService.getBalance(any())).thenReturn(String.valueOf(1000L));

        mockMvc.perform(get("/api/v1/wallets/{id}", walletId))
                        .andExpect(status().isOk())
                        .andExpect(content().string(String.valueOf(1000L)));
    }

    @SneakyThrows
    @Test
    void getBalanceById_WhenWalletNotFound() {
        when(walletService.getBalance(any()))
                .thenThrow(new EntityNotFoundException(Wallet.class, walletDto.getWalletId().toString(),
                "Wallet with id = " + walletDto.getWalletId() + " hasn't been found."));

        mockMvc.perform(get("/api/v1/wallets/{id}", UUID.randomUUID()))
                        .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void getBalanceById_WhenInvalidWalletIdFormat() {
        mockMvc.perform(get("/api/v1/wallets/{id}", "invalid_uuid_format"))
                        .andExpect(status().isBadRequest());
    }

}