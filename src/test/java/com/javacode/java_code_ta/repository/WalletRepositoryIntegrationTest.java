package com.javacode.java_code_ta.repository;

import com.javacode.java_code_ta.model.models.Wallet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
public class WalletRepositoryIntegrationTest {

    @Autowired
    private WalletRepository walletRepository;

    @BeforeEach
    public void addWallet() {
        walletRepository.save(Wallet.builder()
                .walletId(UUID.fromString("b1fbbf70-0b1d-4b92-946b-3c8a93b9be5b"))
                .balance(0L)
                .build());
    }

    @AfterEach
    public void removeWallets() {
        walletRepository.deleteAll();
    }

    @Test
    void findBalanceByWalletId_WhenWalletIdFound() {
        Optional<Long> balance = walletRepository
                .findBalanceByWalletId(UUID.fromString("b1fbbf70-0b1d-4b92-946b-3c8a93b9be5b"));

        assertThat(balance).isPresent();
        assertThat(balance.get()).isEqualTo(0L);
    }

    @Test
    void findBalanceByWalletId_WhenWalletIdNotFound() {
        Optional<Long> balance = walletRepository
                .findBalanceByWalletId(UUID.fromString("f9f1d6f3-6b1a-4e29-87a9-cd9fcef4f956"));

        assertThat(balance).isNotPresent();
    }

}