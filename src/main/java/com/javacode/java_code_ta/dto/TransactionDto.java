package com.javacode.java_code_ta.dto;

import com.javacode.java_code_ta.model.enums.OperationType;
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

    private UUID transactionId;

    private UUID walletId;

    private OperationType operationType;

    private Long amount;

    private LocalDateTime transactionDate;

}