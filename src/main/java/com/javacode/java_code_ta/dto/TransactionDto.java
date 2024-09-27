package com.javacode.java_code_ta.dto;

import com.javacode.java_code_ta.model.enums.OperationType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDto {

    @NotNull
    private UUID transactionId;

    @NotNull
    private UUID walletId;

    @NotNull
    private OperationType operationType;

    @NotNull
    private Long amount;

    @NotNull
    private LocalDateTime transactionDate;

}