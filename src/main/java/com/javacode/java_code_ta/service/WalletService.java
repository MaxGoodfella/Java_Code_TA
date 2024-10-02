package com.javacode.java_code_ta.service;

import com.javacode.java_code_ta.dto.WalletDto;

import java.util.UUID;

public interface WalletService {

    WalletDto editBalance(WalletDto walletDto);

    String getBalance(UUID id);

    String getCachedBalance(UUID id);

}