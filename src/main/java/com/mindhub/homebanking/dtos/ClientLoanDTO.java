package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

import java.time.LocalDate;

public class ClientLoanDTO {

    private Long id;
    private String name;
    private Double amount;
    private Double finalAmount;
    private Integer payments;
    private Double quotaValue;
    private Integer remainingPayments;
    private LocalDate applicationDate;

    public ClientLoanDTO(ClientLoan clientLoan){
        this.id = clientLoan.getId();
        this.name = clientLoan.getLoan().getName();
        this.amount = clientLoan.getRequestAmount();
        this.finalAmount = clientLoan.getFinalAmount();
        this.payments = clientLoan.getPayments();
        this.remainingPayments = clientLoan.getRemainingPayments();
        this.quotaValue = finalAmount / payments;
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

    public Double getFinalAmount() {
        return finalAmount;
    }

    public Integer getPayments() {
        return payments;
    }

    public Integer getRemainingPayments() {
        return remainingPayments;
    }

    public Double getQuotaValue() {
        return quotaValue;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }
}
