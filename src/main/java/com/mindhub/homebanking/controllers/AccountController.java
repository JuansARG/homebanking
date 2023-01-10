package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.RandomNum;
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

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.accountsToAccountsDTO(accountService.getAllAccounts());
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountService.accountToAccountDTO(accountService.getAccountById(id));
    }

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication auth){
        Client currentClient = clientService.getClientByEmail(auth.getName());

        if(currentClient == null){
            return new ResponseEntity<>("The client is not authenticated..", HttpStatus.FORBIDDEN);
        }

        if (currentClient.getAccounts().size() == 3){
            return new ResponseEntity<>("This customer already has 3 accounts", HttpStatus.FORBIDDEN);
        }

        Account newAccount = accountService.createAccount(RandomNum.getRandomNumber4Vin(), 0, LocalDate.now(), currentClient);
        accountService.saveAccount(newAccount);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    //ARREGLAR ESTO QUE NO FUNCIONA...
    @DeleteMapping(path = "/clients/current/accounts/{id}")
    public ResponseEntity<Object> deleteAccount(@PathVariable Long id,
                                                Authentication auth){
        Client currentClient = clientService.getClientByEmail(auth.getName());
        Account currentAccount = (Account) currentClient.getAccounts().stream().filter(account -> account.getId().equals(id));

        if(currentClient == null){
            return new ResponseEntity<>("The client is not authenticated..", HttpStatus.FORBIDDEN);
        }

        if(currentAccount == null){
            return new ResponseEntity<>("The account does not exist.", HttpStatus.FORBIDDEN);
        }

        if(currentAccount.getBalance() > 0){
            return new ResponseEntity<>("You cannot delete an account that has a balance.", HttpStatus.FORBIDDEN);
        }

        if(currentClient.getAccounts().stream().noneMatch(account -> account.getId().equals(id))){
            return new ResponseEntity<>("The account you want to delete does not belong to the authenticated client.", HttpStatus.FORBIDDEN);
        }

        accountService.deleteAccountById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
