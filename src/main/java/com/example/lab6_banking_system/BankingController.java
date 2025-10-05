package com.example.lab6_banking_system;

import com.example.lab6_banking_system.BankingService;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
@Profile("!nosql")
@RestController
@RequestMapping("/api/bank")
public class BankingController {
    private final BankingService bankingService;

    public BankingController(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam Long senderId,
                           @RequestParam Long receiverId,
                           @RequestParam BigDecimal amount) {
        bankingService.transfer(senderId, receiverId, amount);
        return "Transfer successful!";
    }
}

