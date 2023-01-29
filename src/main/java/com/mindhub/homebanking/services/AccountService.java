package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;

import java.time.LocalDate;
import java.util.List;

public interface AccountService {

    List<Account> getAllAccounts();

    Account getAccountById(Long id);

    List<AccountDTO> accountsToAccountsDTO(List<Account> accounts);

    AccountDTO accountToAccountDTO(Account account);

    void saveAccount(Account account);

    Account createAccount(String number, double balance, LocalDate creationDate, Client owner, AccountType type);

    Account getAccountByNumber(String number);

    void deleteAccountById(Long id);

}
