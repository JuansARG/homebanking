package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.utils.RandomNum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/cards")
    public List<CardDTO> getCards(){
        return cardRepository.findAll().stream().map(card -> new CardDTO(card)).collect(Collectors.toList());
    }

    @RequestMapping("/cards/{id}")
    public CardDTO getCard(@PathVariable Long id){
        return cardRepository.findById(id).map(card -> new CardDTO(card)).orElse(null);
    }

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCard( @RequestParam CardType type,
                                              @RequestParam CardColor color,
                                              Authentication auth){
        Client currentClient = clientRepository.findByEmail(auth.getName());
        Set<Card> cards = currentClient.getCards();
        List<Card> cardsTypeTarget = cards.stream().filter(card -> card.getType() == type).collect(Collectors.toList());

        if(cards.size() == 6){
            return new ResponseEntity<>("The limit is 6 cards." ,HttpStatus.FORBIDDEN);
        }else{
            if (cardsTypeTarget.size() == 3){
                return new ResponseEntity<>("You already have 3 accounts of the requested type", HttpStatus.FORBIDDEN);
            }else{
                Card newCard = new Card(currentClient, type, color, RandomNum.getRandonNumber4CardNum(), RandomNum.getRandomNum4CVV());
                cardRepository.save(newCard);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        }
    }

}
