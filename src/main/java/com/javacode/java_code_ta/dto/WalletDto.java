package com.javacode.java_code_ta.dto;

import com.javacode.java_code_ta.model.enums.OperationType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletDto {

    private UUID walletId;

    private OperationType operationType;

    @NotNull
    private Long amount;

}