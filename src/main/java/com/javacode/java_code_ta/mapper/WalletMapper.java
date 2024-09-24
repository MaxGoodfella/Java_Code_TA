package com.javacode.java_code_ta.mapper;

import com.javacode.java_code_ta.dto.WalletDto;
import com.javacode.java_code_ta.model.enums.OperationType;
import com.javacode.java_code_ta.model.models.Wallet;
import lombok.experimental.UtilityClass;

@UtilityClass
public class WalletMapper {

    public WalletDto toWalletDto(Wallet wallet, OperationType operationType, Long amount) {
        return WalletDto.builder()
                .walletId(wallet.getWalletId())
                .operationType(operationType)
                .amount(amount)
                .build();
    }

}