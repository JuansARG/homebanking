package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

import java.time.LocalDate;

public class ClientLoanDTO {

    private Long id;
    private String name;
    private Double amount;
    private Integer payments;
    private LocalDate applicationDate;

    public ClientLoanDTO(ClientLoan clientLoan){
        this.id = clientLoan.getId();
        this.name = clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
        this.applicationDate = clientLoan.getApplicationDate();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }
}
