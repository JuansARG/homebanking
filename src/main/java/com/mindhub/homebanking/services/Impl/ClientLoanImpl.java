package com.mindhub.homebanking.services.Impl;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.services.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
@Service
public class ClientLoanImpl implements ClientLoanService {

    @Autowired
    ClientLoanRepository clientLoanRepository;

    @Override
    public ClientLoan createClientLoan(Double amount, Double finalAmount, Integer payments, Client client, Loan loan, LocalDate applicationDate, double quotaValue) {
        return new ClientLoan(amount, finalAmount, payments, client, loan, applicationDate, quotaValue);
    }

    @Override
    public void saveClientLoan(ClientLoan clientLoan) {
        clientLoanRepository.save(clientLoan);
    }

    @Override
    public ClientLoan getClientLoanById(Long id) {
        return clientLoanRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteClientLoanById(Long id) {
        ClientLoan clientLoan = clientLoanRepository.findById(id).orElse(null);
        if(clientLoan != null){
            clientLoan.setEnable(false);
            clientLoanRepository.save(clientLoan);
        }
    }
}
