package com.mindhub.homebanking.services.Impl;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Override
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    @Override
    public Card getCardById(Long id) {
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public List<CardDTO> cardsToCardsDTO(List<Card> cards) {
        return cards.stream().map(CardDTO::new).toList();
    }

    @Override
    public CardDTO cardToCardDTO(Card card) {
        return new CardDTO(card);
    }

    @Override
    public Card createCard(String cardHolder, CardType type, CardColor color, String number, Integer cvv, LocalDate thruDate, LocalDate fromDate, Client client, Account account) {
        return new Card(cardHolder, type, color, number, cvv, thruDate, fromDate, client, account);
    }

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public void deleteCardById(Long id) {
        Card card = cardRepository.findById(id).orElse(null);
        if(card != null){
            card.setEnable(false);
            cardRepository.save(card);
        }
    }

    @Override
    public Card getCardByNumber(String number) {
        return cardRepository.findByNumber(number);
    }
}
