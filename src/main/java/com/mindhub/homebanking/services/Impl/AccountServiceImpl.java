package com.mindhub.homebanking.services.Impl;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {


    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;
    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll().stream().filter(Account::isEnable).toList();
    }

    @Override
    public Account getAccountById(Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account != null && account.isEnable()) {
            return account;
        }else{
            return null;
        }
    }

    @Override
    public List<AccountDTO> accountsToAccountsDTO(List<Account> accounts) {
        return accounts.stream().map(AccountDTO::new).toList();
    }

    @Override
    public AccountDTO accountToAccountDTO(Account account) {
        return new AccountDTO(account);
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public Account createAccount(String number, double balance, LocalDate creationDate, Client owner, AccountType type) {
        return new Account(number, balance, creationDate, owner, type);
    }

    @Override
    public Account getAccountByNumber(String number) {
        if(accountRepository.findByNumber(number) != null){
            return accountRepository.findByNumber(number);
        }else {
            return null;
        }
    }

    @Override
    public void deleteAccountById(Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        if(account != null){
            account.setEnable(false);
            account.getTransactions().forEach(transaction -> transaction.setEnable(false));

            accountRepository.save(account);
        }
    }

}
