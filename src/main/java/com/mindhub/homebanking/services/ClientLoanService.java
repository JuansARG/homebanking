package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

import java.time.LocalDate;

public interface ClientLoanService {

    ClientLoan createClientLoan(Double amount, Double finalAmount, Integer payments, Client client, Loan loan, LocalDate applicationDate, double quotaValue);

    void saveClientLoan(ClientLoan clientLoan);

    ClientLoan getClientLoanById(Long id);

    void deleteClientLoanById(Long id);

}
