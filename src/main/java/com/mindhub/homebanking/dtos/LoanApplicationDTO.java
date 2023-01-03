package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {

    private Long idOfLoan;
    private Double amount;
    private Integer payments;
    private String destinationAccountNumber;

    public Long getIdOfLoan() {
        return idOfLoan;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }
}
