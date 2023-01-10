package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;

public interface TransactionService {

    Transaction createTransaction(TransactionType type, double amount, String description, LocalDateTime date, Account account);

    void saveTransaction(Transaction transaction);

}
