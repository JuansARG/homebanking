package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class PaymentController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private CardService cardService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Transactional
    @PostMapping("/pay")
    public ResponseEntity<?> payPurchase(Authentication auth,
                                         @RequestParam Integer totalToPay,
                                         @RequestParam String number,
                                         @RequestParam Integer cvv,
                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate thruDate){


        // COMPROBAR QUE EL CLIENTE AUTENTICADO EXISTA
        Client currentClient = clientService.getClientByEmail(auth.getName());

        if(currentClient == null){
            return new ResponseEntity<>("The client is not authenticated.", HttpStatus.FORBIDDEN);
        }

        //COMPROBAR QUE LA TARJETA EXISTA
        Card currentCard = cardService.getCardByNumber(number);
        if(currentCard == null){
            return new ResponseEntity<>("There is no card with the entered number.", HttpStatus.FORBIDDEN);
        }
        //COMPROBAR QUE EL CLIENTE POSEA UNA TARJETA CON DICHO NUMERO
        if(currentClient.getCards().stream().noneMatch(card -> card.getNumber().equalsIgnoreCase(number))){
            return new ResponseEntity<>("The authenticated customer does not have a card with that number.", HttpStatus.FORBIDDEN);
        }

        //COMPROBANDO QUE TENGA UNA CUENTA ASOCIADA
        if(currentCard.getAccount() == null){
            return new ResponseEntity<>("The card is not associated with a bank account.", HttpStatus.FORBIDDEN);
        }

        //COMPROBAR QUE EL CVV COINCIDA CON LA TARJETA
        if(!currentCard.getCvv().equals(cvv)){
            return new ResponseEntity<>("The cvv provided does not match that of the card entered.", HttpStatus.FORBIDDEN);
        }

//        //COMPROBAR QUE COINCIDA EL VENCIMIENTO
//        if(!currentCard.getFromDate().equals(thruDate)){
//            return new ResponseEntity<>("The expiration date entered does not match.", HttpStatus.FORBIDDEN);
//        }


        //COMPROBAR QUE LA TARJETA NO ESTE VENCIDA
        if(LocalDate.now().isAfter(thruDate)){
            return new ResponseEntity<>("The entered card is expired.", HttpStatus.FORBIDDEN);
        }

        //COMPROBAR QUE LA CUENTA ASOCIADA A LA TARJETA POSEA EL MONTO SUFICIENTE PARA PAGAR
        if(currentCard.getAccount().getBalance() < totalToPay){
            return new ResponseEntity<>("The account associated with your card does not have sufficient funds.", HttpStatus.FORBIDDEN);
        }

        Account currentAccount = currentCard.getAccount();

        //GENERAR LA TRANSACcION Y PERSISTIR LOS DATOS GENERADOS NUEVOS
        Transaction transaction = new Transaction(
                TransactionType.CREDIT, totalToPay,
                "Purchase Completed at The Camp",
                LocalDateTime.now(),
                currentAccount,
                currentAccount.getBalance() - totalToPay);

        transactionService.saveTransaction(transaction);

        currentAccount.setBalance(currentAccount.getBalance() - totalToPay);
        accountService.saveAccount(currentAccount);

        return new ResponseEntity<>("The payment has been approved.", HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/pay/v2")
    public ResponseEntity<?> payPurchaseV2(@RequestParam Double totalToPay,
                                           @RequestParam String numberCard,
                                           @RequestParam Integer cvv,
                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate thruDate){

        if(numberCard.isEmpty() || numberCard == null){
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }

        Card currentCard = cardService.getCardByNumber(numberCard);

        if(currentCard == null || !currentCard.isEnable()){
            return new ResponseEntity<>("The card does not exist or is disabled.", HttpStatus.FORBIDDEN);
        }

        if(!currentCard.getCvv().equals(cvv)){
            return new ResponseEntity<>("The cvv does not correspond to the card entered.", HttpStatus.FORBIDDEN);
        }

        if(currentCard.getThruDate() != thruDate){
            return new ResponseEntity<>("The expiration date does not correspond to the card entered.", HttpStatus.FORBIDDEN);
        }

        if(currentCard.getThruDate().isAfter(LocalDate.now())){
            return new ResponseEntity<>("The card is expired.", HttpStatus.FORBIDDEN);
        }

        Account currentAccount = currentCard.getAccount();

        if(currentAccount == null || !currentAccount.isEnable()){
            return new ResponseEntity<>("The account to which the card is associated does not exist or is disabled.", HttpStatus.FORBIDDEN);
        }

        if(currentAccount.getBalance() < totalToPay){
            return new ResponseEntity<>("The account associated with the card does not have a sufficient amount.", HttpStatus.FORBIDDEN);
        }

        Transaction transaction = new Transaction(
                TransactionType.CREDIT, totalToPay,
                "Purchase Completed at The Camp",
                LocalDateTime.now(),
                currentAccount,
                currentAccount.getBalance() - totalToPay);

        transactionService.saveTransaction(transaction);

        currentAccount.setBalance(currentAccount.getBalance() - totalToPay);
        accountService.saveAccount(currentAccount);

        return new ResponseEntity<>("The payment has been approved.", HttpStatus.OK);

    }

}
