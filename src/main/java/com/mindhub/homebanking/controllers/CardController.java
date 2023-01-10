package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.RandomNum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private ClientService clientService;

    @RequestMapping("/cards")
    public List<CardDTO> getCards(){
        return cardService.cardsToCardsDTO(cardService.getAllCards());
    }

    @RequestMapping("/cards/{id}")
    public CardDTO getCard(@PathVariable Long id){
        return cardService.cardToCardDTO(cardService.getCardById(id));
    }

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCard( @RequestParam CardType type,
                                              @RequestParam CardColor color,
                                              Authentication auth){
        Client currentClient = clientService.getClientByEmail(auth.getName());
        List<Card> cards = currentClient.getCards().stream().toList();
        List<Card> cardsTypeTarget = cards.stream().filter(card -> card.getType() == type).collect(Collectors.toList());

        if(cards.size() == 6){
            return new ResponseEntity<>("The limit is 6 cards." ,HttpStatus.FORBIDDEN);
        }else{
            if (cardsTypeTarget.size() == 3){
                return new ResponseEntity<>("You already have 3 accounts of the requested type", HttpStatus.FORBIDDEN);
            }else{
                Card newCard = cardService.createCard(currentClient, type, color, RandomNum.getRandonNumber4CardNum(), RandomNum.getRandomNum4CVV(), LocalDate.now());
                cardService.saveCard(newCard);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        }
    }

}
