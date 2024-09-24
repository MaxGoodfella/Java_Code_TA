package com.javacode.java_code_ta.repository;

import com.javacode.java_code_ta.model.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
}