package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;

import java.time.LocalDate;
import java.util.List;

public interface CardService {

    List<Card> getAllCards();

    Card getCardById(Long id);

    List<CardDTO> cardsToCardsDTO(List<Card> cards);

    CardDTO cardToCardDTO(Card card);

    Card createCard(String cardHolder, CardType type, CardColor color, String number, Integer cvv, LocalDate thruDate, LocalDate fromDate, Client client, Account account);

    void saveCard(Card card);

    void deleteCardById(Long id);

    Card getCardByNumber(String number);

}
