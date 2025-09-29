package com.example.lab6_banking_system;

import com.example.lab6_banking_system.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
