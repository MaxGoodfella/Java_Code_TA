package com.javacode.java_code_ta.service;

import com.javacode.java_code_ta.dto.WalletDto;
import com.javacode.java_code_ta.exceptions.BadRequestException;
import com.javacode.java_code_ta.exceptions.EntityNotFoundException;
import com.javacode.java_code_ta.model.enums.OperationType;
import com.javacode.java_code_ta.model.models.Transaction;
import com.javacode.java_code_ta.model.models.Wallet;
import com.javacode.java_code_ta.repository.TransactionRepository;
import com.javacode.java_code_ta.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private WalletServiceImpl walletService;

    @Captor
    private ArgumentCaptor<Wallet> walletCaptor;

    @Captor
    private ArgumentCaptor<Transaction> transactionCaptor;


    @Test
    void editBalance_WhenWalletFound_ThenReturnWallet() {

        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet();
        wallet.setWalletId(walletId);
        wallet.setBalance(100L);

        WalletDto walletDto = WalletDto
                .builder()
                .walletId(walletId)
                .operationType(OperationType.valueOf("DEPOSIT"))
                .amount(50L)
                .build();

        when(walletRepository.findByWalletIdWithPessimisticWriteLock(walletId)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        WalletDto result = walletService.editBalance(walletDto);

        assertEquals(walletId, result.getWalletId());
        assertEquals(150L, wallet.getBalance());
        verify(walletRepository).save(walletCaptor.capture());
        assertEquals(150L, walletCaptor.getValue().getBalance());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void editBalance_WhenWalletNotFound_ThenThrowEntityNotFoundException() {

        UUID walletId = UUID.randomUUID();
        WalletDto walletDto = WalletDto
                .builder()
                .walletId(walletId)
                .operationType(OperationType.valueOf("DEPOSIT"))
                .amount(50L)
                .build();

        when(walletRepository.findByWalletIdWithPessimisticWriteLock(walletId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> walletService.editBalance(walletDto));

        verify(walletRepository, never()).save(any(Wallet.class));
        verify(transactionRepository, never()).save(any(Transaction.class));

    }

    @Test
    void editBalance_WhenWithdrawSuccess_ThenReturnUpdatedWallet() {

        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet();
        wallet.setWalletId(walletId);
        wallet.setBalance(200L);

        WalletDto walletDto = WalletDto
                .builder()
                .walletId(walletId)
                .operationType(OperationType.valueOf("WITHDRAW"))
                .amount(50L)
                .build();

        when(walletRepository.findByWalletIdWithPessimisticWriteLock(walletId)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        WalletDto result = walletService.editBalance(walletDto);

        assertEquals(walletId, result.getWalletId());
        assertEquals(150L, wallet.getBalance());
        verify(walletRepository).save(walletCaptor.capture());
        assertEquals(150L, walletCaptor.getValue().getBalance());
        verify(transactionRepository).save(any(Transaction.class));

    }

    @Test
    void editBalance_WhenWithdrawInsufficientFunds_ThenThrowBadRequestException() {

        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet();
        wallet.setWalletId(walletId);
        wallet.setBalance(100L);

        WalletDto walletDto = WalletDto
                .builder()
                .walletId(walletId)
                .operationType(OperationType.valueOf("WITHDRAW"))
                .amount(150L)
                .build();

        when(walletRepository.findByWalletIdWithPessimisticWriteLock(walletId)).thenReturn(Optional.of(wallet));

        assertThrows(BadRequestException.class, () -> walletService.editBalance(walletDto));

        verify(walletRepository, never()).save(any(Wallet.class));
        verify(transactionRepository, never()).save(any(Transaction.class));

    }

    @Test
    void editBalance_WhenInvalidOperationType_ThenThrowBadRequestException() {

        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet();
        wallet.setWalletId(walletId);
        wallet.setBalance(100L);

        WalletDto walletDto = WalletDto
                .builder()
                .walletId(walletId)
                .operationType(null)
                .amount(50L)
                .build();

        when(walletRepository.findByWalletIdWithPessimisticWriteLock(walletId)).thenReturn(Optional.of(wallet));

        assertThrows(IllegalArgumentException.class, () -> walletService.editBalance(walletDto));

        verify(walletRepository, never()).save(any(Wallet.class));
        verify(transactionRepository, never()).save(any(Transaction.class));

    }

    @Test
    void editBalance_WhenWalletFound_ThenCorrectTransactionSaved() {

        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet();
        wallet.setWalletId(walletId);
        wallet.setBalance(100L);

        WalletDto walletDto = WalletDto
                .builder()
                .walletId(walletId)
                .operationType(OperationType.valueOf("DEPOSIT"))
                .amount(50L)
                .build();

        when(walletRepository.findByWalletIdWithPessimisticWriteLock(walletId)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        walletService.editBalance(walletDto);

        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction savedTransaction = transactionCaptor.getValue();
        assertEquals(50L, savedTransaction.getAmount());
        assertEquals(OperationType.DEPOSIT, savedTransaction.getOperationType());
        assertEquals(wallet, savedTransaction.getWallet());

    }

    @Test
    void editBalance_WhenWalletIdIsNull_ThenThrowIllegalArgumentException() {

        WalletDto walletDto = WalletDto
                .builder()
                .walletId(null)
                .operationType(OperationType.valueOf("DEPOSIT"))
                .amount(50L)
                .build();

        assertThrows(IllegalArgumentException.class, () -> walletService.editBalance(walletDto));

        verify(walletRepository, never()).findById(any(UUID.class));
        verify(walletRepository, never()).save(any(Wallet.class));
        verify(transactionRepository, never()).save(any(Transaction.class));

    }

    @Test
    void editBalance_WhenWalletIdIsInvalid_ThenThrowIllegalArgumentException() {

        String malformedWalletId = "invalid-uuid-format";

        assertThrows(IllegalArgumentException.class, () -> {
            UUID invalidId = UUID.fromString(malformedWalletId);
            walletService.getBalance(invalidId);
        });

        verify(walletRepository, never()).findByWalletId(any(UUID.class));

    }

    @Test
    void getBalance_WhenWalletNotFound_ThenThrowEntityNotFoundException() {

        UUID walletId = UUID.randomUUID();
        when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> walletService.getBalance(walletId));

        verify(walletRepository).findByWalletId(walletId);

    }

    @Test
    void getBalance_WhenWalletIdIsNull_ThenThrowIllegalArgumentException() {

        UUID walletId = null;

        assertThrows(IllegalArgumentException.class, () -> walletService.getBalance(walletId));

        verify(walletRepository, never()).findByWalletId(any(UUID.class));

    }

    @Test
    void getBalance_WhenWalletIdIsInvalidFormat_ThenThrowIllegalArgumentException() {

        String invalidWalletId = "invalid-uuid-format";

        assertThrows(IllegalArgumentException.class, () -> {
            UUID invalidId = UUID.fromString(invalidWalletId);
            walletService.getBalance(invalidId);
        });

        verify(walletRepository, never()).findByWalletId(any(UUID.class));

    }

}