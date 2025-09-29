package com.example.lab6_banking_system;

import com.example.lab6_banking_system.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
