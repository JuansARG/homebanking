package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    Transaction createTransaction(TransactionType type, double amount, String description, LocalDateTime date, Account account, double updateBalance);

    void saveTransaction(Transaction transaction);

    List<Transaction> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<TransactionDTO> transactionsToTransactionsDTO(List<Transaction> transactions);

    TransactionDTO transactionToTransactionDTO(Transaction transaction);
}
