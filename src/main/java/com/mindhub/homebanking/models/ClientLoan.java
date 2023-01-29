package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDate;


@Entity
public class ClientLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private Double requestAmount;
    private Double finalAmount;
    private Double restAmount;
    private Double quotaValue;
    private Integer payments;
    private Integer remainingPayments;
    private LocalDate applicationDate;
    private boolean enable;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_id")
    private Loan loan;

    public ClientLoan(){

    }

    public ClientLoan(Double amount, Double finalAmount, Integer payments, Client client, Loan loan, LocalDate applicationDate, double quotaValue) {
        this.requestAmount = amount;
        this.finalAmount = finalAmount;
        this.restAmount = finalAmount;
        this.payments = payments;
        this.remainingPayments = payments;
        this.client = client;
        this.loan = loan;
        this.applicationDate = applicationDate;
        this.quotaValue = quotaValue;
        this.enable = true;
    }

    public Long getId() {
        return id;
    }

    public Double getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(Double finalAmount) {
        this.finalAmount = finalAmount;
    }

    public Double getQuotaValue() {
        return quotaValue;
    }

    public void setQuotaValue(Double quotaValue) {
        this.quotaValue = quotaValue;
    }

    public Integer getPayments() {
        return payments;
    }

    public void setPayments(Integer payments) {
        this.payments = payments;
    }

    public Integer getRemainingPayments() {
        return remainingPayments;
    }

    public void setRemainingPayments(Integer remainingPayments) {
        this.remainingPayments = remainingPayments;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Double getRequestAmount() {
        return requestAmount;
    }

    public void setRequestAmount(Double requestAmount) {
        this.requestAmount = requestAmount;
    }

    public Double getRestAmount() {
        return restAmount;
    }

    public void setRestAmount(Double restAmount) {
        this.restAmount = restAmount;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
