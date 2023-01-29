package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardApplicationDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CardService cardService;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(@RequestParam double amount,
                                                    @RequestParam String description,
                                                    @RequestParam String numberRootAccount,
                                                    @RequestParam String numberDestinationAccount,
                                                    Authentication auth){

        Client currentClient = clientService.getClientByEmail(auth.getName());
        Account rootAccount = accountService.getAccountByNumber(numberRootAccount);

        if(amount == 0 || amount < 500){
            return new ResponseEntity<>("Invalid amount, the minimum amount is 500.", HttpStatus.FORBIDDEN);
        }

        if(description.isEmpty()){
            return new ResponseEntity<>("The description is required.", HttpStatus.FORBIDDEN);
        }

        if(numberRootAccount.isEmpty()){
            return new ResponseEntity<>("The source account number is invalid.", HttpStatus.FORBIDDEN);
        }

        if(numberDestinationAccount.equals("")){
            return new ResponseEntity<>("The destination account number is invalid.", HttpStatus.FORBIDDEN);
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

        if(destinationAccount == null || !destinationAccount.isEnable()){
            return new ResponseEntity<>("The recipient account does not exist.", HttpStatus.FORBIDDEN);
        }

        if(rootAccount.getBalance() < amount){
            return new ResponseEntity<>("Insufficient funds.", HttpStatus.FORBIDDEN);
        }

        Transaction transaction1 = transactionService.createTransaction(TransactionType.DEBIT, amount, description + " -> " + destinationAccount.getNumber(), LocalDateTime.now(), rootAccount, rootAccount.getBalance() - amount);
        Transaction transaction2 = transactionService.createTransaction(TransactionType.CREDIT, amount, description + " -> " + rootAccount.getNumber(), LocalDateTime.now(), destinationAccount, destinationAccount.getBalance() + amount);

        transactionService.saveTransaction(transaction1);
        transactionService.saveTransaction(transaction2);

        rootAccount.setBalance(rootAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);

        accountService.saveAccount(rootAccount);
        accountService.saveAccount(destinationAccount);

        return new ResponseEntity<>("Everything has gone well!", HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("/transactions/pay")
    public ResponseEntity<Object> pay(@RequestBody CardApplicationDTO cardApplicationDTO, Authentication auth){

        Client currentClient = clientService.getClientByEmail(auth.getName());
        Card currentCard = cardService.getCardByNumber(cardApplicationDTO.getNumber());
        Account currentAccount = accountService.getAccountById(currentCard.getAccount().getId());

        //QUE EL CLIENTE ESTE AUTENTICADO
        if(currentClient == null){
            return new ResponseEntity<>("The client does not exist.", HttpStatus.FORBIDDEN);
        }

        //COMPROBAR QUE EL NUMERO NO ESTE VACIO
        if(cardApplicationDTO.getNumber().isEmpty()){
            return new ResponseEntity<>("Invalid number of card", HttpStatus.FORBIDDEN);
        }

        //COMPROBAR QUE EL NUMERO PERTENEZCA A UNA CARD
        if(currentCard == null){
            return new ResponseEntity<>("The number does not belong to a card.", HttpStatus.FORBIDDEN);
        }

        //QUE EL CVV NO ESTE VACIO
        if(cardApplicationDTO.getCvv() < 100 || cardApplicationDTO.getCvv() > 999){
            return new ResponseEntity<>("Invalid CVV", HttpStatus.FORBIDDEN);
        }

        //COMPROBAR QUE EL CVV COINCIDA CON EL CVV LA CURRENT CARD
        if(!Objects.equals(cardApplicationDTO.getCvv(), currentCard.getCvv())){
            return new ResponseEntity<>("The CVV does not correspond to the card. || " + cardApplicationDTO.getCvv() + " || " + currentCard.getCvv(), HttpStatus.FORBIDDEN);
        }

        //QUE LA CARD PERTENEZCA AL CLIENTE
        if(currentClient.getCards().stream().noneMatch(card -> card.getId().equals(currentCard.getId()))){
            return new ResponseEntity<>("The CVV does not correspond to the card.", HttpStatus.FORBIDDEN);
        }

        //COMPROBAR QUE EL NOMBRE NO ESTE VACIO
        if(cardApplicationDTO.getCardHolder().isEmpty()){
            return new ResponseEntity<>("Invalid name", HttpStatus.FORBIDDEN);
        }

        //COMPROBAR QUE LA CUENTA EXISTA y que no este desactivada
        if(currentAccount == null || !currentAccount.isEnable()){
            return new ResponseEntity<>("The account does not exist.", HttpStatus.FORBIDDEN);
        }

        //COMPROBAR QUE LA CUENTA PERTENEZA AL CLIENTE
        if(currentClient.getAccounts().stream().noneMatch(account -> account.getId().equals(currentAccount.getId()))){
            return new ResponseEntity<>("The account does not belong to the authenticated client.", HttpStatus.FORBIDDEN);
        }

        //COMPROBORAR QUE LA CUENTA ASOCIADA A LA TARJETA TENGA EL MONTO SUFICIENTE
        if(currentAccount.getBalance() < cardApplicationDTO.getAmount()){
            return new ResponseEntity<>("Insufficient amount.", HttpStatus.FORBIDDEN);
        }

        //COMPROBAR QUE LA FECHA ACTUAL SEA > A LA FECHA DE VENCIMIENTO DE LA TARJETA
        if(LocalDate.now().isAfter(cardApplicationDTO.getThruDate())){
            return new ResponseEntity<>("The card is expired.", HttpStatus.FORBIDDEN);
        }

        //COMPROBAR QUE LA DESCRIPCION NO ESTE VACIA
        if(cardApplicationDTO.getDescription() == ""){
            return new ResponseEntity<>("Invalid description.", HttpStatus.FORBIDDEN);
        }

        Transaction transaction = new Transaction(
                TransactionType.DEBIT,
                cardApplicationDTO.getAmount(),
                cardApplicationDTO.getDescription(),
                LocalDateTime.now(),
                currentAccount,
                currentAccount.getBalance() - cardApplicationDTO.getAmount());

        currentAccount.setBalance(currentAccount.getBalance() - cardApplicationDTO.getAmount());

        transactionService.saveTransaction(transaction);
        accountService.saveAccount(currentAccount);

        return new ResponseEntity<>("The payment has been made.", HttpStatus.CREATED);

    }
}
