package com.mindhub.homebanking.dtos;

import java.time.LocalDate;

public class CardApplicationDTO {

    private String cardHolder;
    private String number;
    private Integer cvv;
    private LocalDate thruDate;
    private double amount;
    private String description;

    public CardApplicationDTO(String cardHolder, String number, Integer cvv, LocalDate thruDate, double amount, String description) {
        this.cardHolder = cardHolder;
        this.number = number;
        this.cvv = cvv;
        this.thruDate = thruDate;
        this.amount = amount;
        this.description = description;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getNumber() {
        return number;
    }

    public Integer getCvv() {
        return cvv;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
