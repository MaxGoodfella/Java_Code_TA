package com.javacode.java_code_ta.controller;

import com.javacode.java_code_ta.dto.WalletDto;
import com.javacode.java_code_ta.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = "api/v1")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/wallet")
    @ResponseStatus(HttpStatus.CREATED)
    public WalletDto editBalance(@RequestBody @Valid WalletDto walletDto) {
        log.info("Start editing balance {}", walletDto);
        WalletDto editedWallet = walletService.editBalance(walletDto);
        log.info("Finish editing balance {}", walletDto);
        return editedWallet;
    }

    @GetMapping("/wallets/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String getBalance(@PathVariable UUID id) {
        return walletService.getBalance(id);
    }

}