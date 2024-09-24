package com.javacode.java_code_ta.service;

import com.javacode.java_code_ta.dto.WalletDto;
import com.javacode.java_code_ta.exceptions.BadRequestException;
import com.javacode.java_code_ta.exceptions.EntityNotFoundException;
import com.javacode.java_code_ta.mapper.WalletMapper;
import com.javacode.java_code_ta.model.enums.OperationType;
import com.javacode.java_code_ta.model.models.Transaction;
import com.javacode.java_code_ta.model.models.Wallet;
import com.javacode.java_code_ta.repository.TransactionRepository;
import com.javacode.java_code_ta.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    @Override
    public WalletDto editBalance(WalletDto walletDto) {

        if (walletDto.getWalletId() == null) {
            throw new IllegalArgumentException("Wallet ID cannot be null");
        }

        Wallet wallet = walletRepository.findById(walletDto.getWalletId())
                .orElseThrow(() -> new EntityNotFoundException(Wallet.class, walletDto.getWalletId().toString(),
                        "Wallet with id = " + walletDto.getWalletId() + " hasn't been found."));

        OperationType operationType = OperationType.fromString(String.valueOf(walletDto.getOperationType()));

        switch (operationType) {
            case DEPOSIT -> wallet.setBalance(wallet.getBalance() + walletDto.getAmount());
            case WITHDRAW -> {
                if (wallet.getBalance() - walletDto.getAmount() < 0) {
                    throw new BadRequestException(Wallet.class, walletDto.getWalletId().toString(), "Insufficient funds");
                }
                wallet.setBalance(wallet.getBalance() - walletDto.getAmount());
            }
            default -> throw new BadRequestException(Wallet.class, walletDto.getWalletId().toString(), "Invalid operation type");
        }

        Wallet savedWallet = walletRepository.save(wallet);

        Transaction transaction = Transaction.builder()
                .wallet(savedWallet)
                .operationType(operationType)
                .amount(walletDto.getAmount())
                .transactionDate(LocalDateTime.now())
                .build();
        transactionRepository.save(transaction);

        return WalletMapper.toWalletDto(savedWallet, operationType, walletDto.getAmount());

    }

    @Override
    public String getBalance(UUID id) {

        if (id == null) {
            throw new IllegalArgumentException("Wallet ID cannot be null");
        }

        Long balance = walletRepository.findBalanceByWalletId(id)
                .orElseThrow(() -> new EntityNotFoundException(Wallet.class, String.valueOf(id),
                        "Wallet with id = " + id.toString() + " hasn't been found."));

        return String.valueOf(balance);

    }

}