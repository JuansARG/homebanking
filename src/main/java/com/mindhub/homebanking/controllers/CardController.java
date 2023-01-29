package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.CardsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/cards")
    public List<CardDTO> getCards(){
        return cardService.cardsToCardsDTO(cardService.getAllCards());
    }

    @GetMapping("/cards/{id}")
    public CardDTO getCard(@PathVariable Long id){
        return cardService.cardToCardDTO(cardService.getCardById(id));
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard( @RequestParam CardType type,
                                              @RequestParam CardColor color,
                                              @RequestParam String numberOfAccount,
                                              Authentication auth){
        Client currentClient = clientService.getClientByEmail(auth.getName());
        Account currentAccount = accountService.getAccountByNumber(numberOfAccount);
        // PRIMERO FILTRO Y GUARDO LAS TARJETAS ACTIVAS
        List<Card> cards = currentClient.getCards().stream().filter(Card::isEnable).toList();
        // LUEGO SOBRE LAS ACTIVAS EMPIEZO A TRABAJAR
        List<Card> cardsTypeTarget = cards.stream().filter(card -> card.getType() == type).toList();

        //SI EL TYPE ES CREDIT NO IMPORTA COMPROBAR EL TEMA DE LA CUENTA
        if(type.equals("DEBIT")){

            //COMPROBAR QUE LA CUENTA EXISTA Y QUE ESTE ENABLE/ACTIVADA
            if(currentAccount == null || !currentAccount.isEnable()){
                return new ResponseEntity<>("The account does not exist.", HttpStatus.FORBIDDEN);
            }

            //COMPROBAR QUE LA CUENTA PERTENEZCA AL CLIENTE AUTHENTICADO
            if(currentClient.getAccounts().stream().noneMatch(account -> account.getId().equals(currentAccount.getId()))){
                return new ResponseEntity<>("The account does not belong to the authenticated client.", HttpStatus.FORBIDDEN);
            }
        }


        if(cards.size() == 6){
            return new ResponseEntity<>("The limit is 6 cards." ,HttpStatus.FORBIDDEN);
        }else{
            if (cardsTypeTarget.size() == 3){
                return new ResponseEntity<>("You already have 3 accounts of the requested type", HttpStatus.FORBIDDEN);
            }else{
                Card newCard = cardService.createCard(
                                            currentClient.getFirstName() + " " + currentClient.getLastName(),
                                            type,
                                            color,
                                            CardsUtils.getCardNumber(),
                                            CardsUtils.getCVV(),
                                            LocalDate.now(),
                                            LocalDate.now().plusYears(5),
                                            currentClient,
                                            currentAccount);
                cardService.saveCard(newCard);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        }
    }

    @DeleteMapping("/clients/current/cards/{id}")
    public ResponseEntity<Object> deleteCard(@PathVariable Long id,
                                             Authentication auth){
        Client currentClient = clientService.getClientByEmail(auth.getName());
        Card currentCard = cardService.getCardById(id);

        if(currentClient == null){
            return new ResponseEntity<>("The client is not authenticated..", HttpStatus.FORBIDDEN);
        }

        if(currentCard == null){
            return new ResponseEntity<>("The card does not exist.", HttpStatus.FORBIDDEN);
        }

        if(currentClient.getCards().stream().noneMatch(card -> card.getId().equals(id))){
            return new ResponseEntity<>("The card you want to delete does not belong to the authenticated client.", HttpStatus.FORBIDDEN);
        }

        cardService.deleteCardById(id);
        return new ResponseEntity<>("The card has been deleted.", HttpStatus.OK);
    }
}
