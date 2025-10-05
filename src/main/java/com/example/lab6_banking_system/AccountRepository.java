package com.example.lab6_banking_system;

import com.example.lab6_banking_system.Account;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
@Profile("!nosql")
public interface AccountRepository extends JpaRepository<Account, Long> {
}
