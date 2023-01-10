package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;

import java.time.LocalDate;
import java.util.List;

public interface CardService {

    List<Card> getAllCards();

    Card getCardById(Long id);

    List<CardDTO> cardsToCardsDTO(List<Card> cards);

    CardDTO cardToCardDTO(Card card);

    Card createCard(Client client, CardType type, CardColor color, String number, Integer cvv, LocalDate thruDate);

    void saveCard(Card card);

}
