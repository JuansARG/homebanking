package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.AccountsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.accountsToAccountsDTO(accountService.getAllAccounts());
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountService.accountToAccountDTO(accountService.getAccountById(id));
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(@RequestParam AccountType type, Authentication auth){
        Client currentClient = clientService.getClientByEmail(auth.getName());

        if(currentClient == null){
            return new ResponseEntity<>("The client is not authenticated..", HttpStatus.FORBIDDEN);
        }

        if (currentClient.getAccounts().stream().filter(Account::isEnable).toList().size() == 3){
            return new ResponseEntity<>("This customer already has 3 accounts", HttpStatus.FORBIDDEN);
        }

        Account newAccount = accountService.createAccount("VIN-" + AccountsUtils.getNumber4VIN(), 0, LocalDate.now(), currentClient, type);
        accountService.saveAccount(newAccount);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @DeleteMapping("/clients/current/accounts/{id}")
    public ResponseEntity<Object> deleteAccount(@PathVariable Long id,
                                                Authentication auth){
        Client currentClient = clientService.getClientByEmail(auth.getName());
        Account currentAccount = accountService.getAccountById(id);


        if(currentClient == null){
            return new ResponseEntity<>("The client is not authenticated..", HttpStatus.FORBIDDEN);
        }

        if(currentAccount == null){
            return new ResponseEntity<>("The account does not exist.", HttpStatus.FORBIDDEN);
        }

        if(currentAccount.getBalance() > 0){
            return new ResponseEntity<>("You cannot delete an account that has a balance.", HttpStatus.CONFLICT);
        }

        if(currentClient.getAccounts().stream().noneMatch(account -> account.getId().equals(id))){
            return new ResponseEntity<>("The account you want to delete does not belong to the authenticated client.", HttpStatus.FORBIDDEN);
        }

        currentAccount.getCards().forEach(card -> card.setEnable(false));

        accountService.deleteAccountById(id);
        return new ResponseEntity<>("The account has been deleted.", HttpStatus.OK);
    }

}
