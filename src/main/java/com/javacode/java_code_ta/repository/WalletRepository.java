package com.javacode.java_code_ta.repository;

import com.javacode.java_code_ta.model.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    @Query("SELECT w.balance FROM Wallet w WHERE w.walletId = :walletId")
    Optional<Long> findBalanceByWalletId(@Param("walletId") UUID walletId);

}