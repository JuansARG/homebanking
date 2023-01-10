package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(@RequestParam double amount,
                                                    @RequestParam String description,
                                                    @RequestParam String numberRootAccount,
                                                    @RequestParam String numberDestinationAccount,
                                                    Authentication auth){

        Client currentClient = clientService.getClientByEmail(auth.getName());
        Account rootAccount = accountService.getAccountByNumber(numberRootAccount);

        if(amount == 0 || description.isEmpty() || numberRootAccount.isEmpty() || numberDestinationAccount.isEmpty()){
            return new ResponseEntity<>("Incomplete fields.", HttpStatus.FORBIDDEN);
        }

        if(numberRootAccount.equalsIgnoreCase(numberDestinationAccount)){
            return new ResponseEntity<>("The source account and the destination account are the same.", HttpStatus.FORBIDDEN);
        }


        if(rootAccount == null){
            return new ResponseEntity<>("The source account does not exist.", HttpStatus.FORBIDDEN);
        }

        if(currentClient.getAccounts().stream().noneMatch(account -> account.getId().equals(rootAccount.getId()))){
            return new ResponseEntity<>("The source account does not belong to the authenticated client.", HttpStatus.FORBIDDEN);
        }

        Account destinationAccount = accountService.getAccountByNumber(numberDestinationAccount);

        if(destinationAccount == null){
            return new ResponseEntity<>("The recipient account does not exist.", HttpStatus.FORBIDDEN);
        }

        if(rootAccount.getBalance() < amount){
            return new ResponseEntity<>("Insufficient funds.", HttpStatus.FORBIDDEN);
        }

        Transaction transaction1 = transactionService.createTransaction(TransactionType.DEBIT, amount, description + " -> " + destinationAccount.getNumber(), LocalDateTime.now(), rootAccount);
        Transaction transaction2 = transactionService.createTransaction(TransactionType.CREDIT, amount, description + " -> " + rootAccount.getNumber(), LocalDateTime.now(), destinationAccount);

        transactionService.saveTransaction(transaction1);
        transactionService.saveTransaction(transaction2);

        rootAccount.setBalance(rootAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);

        accountService.saveAccount(rootAccount);
        accountService.saveAccount(destinationAccount);

        return new ResponseEntity<>("Everything has gone well!", HttpStatus.CREATED);
    }
}
