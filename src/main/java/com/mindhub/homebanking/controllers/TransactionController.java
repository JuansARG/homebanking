package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
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
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(@RequestParam double amount,
                                                    @RequestParam String description,
                                                    @RequestParam String numberRootAccount,
                                                    @RequestParam String numberDestinationAccount,
                                                    Authentication auth){

        Client currentClient = clientRepository.findByEmail(auth.getName());
        Account rootAccount = accountRepository.findByNumber(numberRootAccount);

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

        Account destinationAccount = accountRepository.findByNumber(numberDestinationAccount);

        if(destinationAccount == null){
            return new ResponseEntity<>("The recipient account does not exist.", HttpStatus.FORBIDDEN);
        }

        if(rootAccount.getBalance() < amount){
            return new ResponseEntity<>("Insufficient funds.", HttpStatus.FORBIDDEN);
        }

        Transaction transaction1 = new Transaction(TransactionType.DEBIT, amount, description + " -> " + destinationAccount.getNumber(), LocalDateTime.now(), rootAccount);
        Transaction transaction2 = new Transaction(TransactionType.CREDIT, amount, description + " -> " + rootAccount.getNumber(), LocalDateTime.now(), destinationAccount);

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);

        rootAccount.setBalance(rootAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);

        accountRepository.save(rootAccount);
        accountRepository.save(destinationAccount);

        return new ResponseEntity<>("Everything has gone well!", HttpStatus.CREATED);
    }
}
