package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AccountDTO {
    private Long id;
    private String number;
    private LocalDate creationDate;
    private double balance;
    private List<TransactionDTO> transactions;
    private AccountType accountType;


    public AccountDTO(Account account){
         id = account.getId();
         number = account.getNumber();
         creationDate = account.getCreationDate();
         balance = account.getBalance();
         transactions = account.getTransactions().stream().map(TransactionDTO::new).toList();
         accountType = account.getAccountType();
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public AccountType getAccountType() {
        return accountType;
    }
}
