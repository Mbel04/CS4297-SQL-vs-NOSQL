package com.example.lab6_banking_system;

import com.example.lab6_banking_system.Account;
import com.example.lab6_banking_system.Transaction;
import com.example.lab6_banking_system.AccountRepository;
import com.example.lab6_banking_system.TransactionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class BankingService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public BankingService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void transfer(Long senderId, Long receiverId, BigDecimal amount) {
        Account sender = accountRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        Account receiver = accountRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        Transaction tx = new Transaction();
        tx.setSenderAccountId(senderId);
        tx.setReceiverAccountId(receiverId);
        tx.setAmount(amount);
        tx.setTimestamp(LocalDateTime.now());

        transactionRepository.save(tx);
    }
}