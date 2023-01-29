package com.mindhub.homebanking.services.Impl;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Transaction createTransaction(TransactionType type, double amount, String description, LocalDateTime date, Account account, double updateBalance) {
        return new Transaction(type, amount, description, date, account, updateBalance);
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByDateBetween(startDate, endDate);
    }

    @Override
    public List<TransactionDTO> transactionsToTransactionsDTO(List<Transaction> transactions) {
        return transactions.stream().map(TransactionDTO::new).toList();
    }

    @Override
    public TransactionDTO transactionToTransactionDTO(Transaction transaction) {
        return new TransactionDTO(transaction);
    }
}
