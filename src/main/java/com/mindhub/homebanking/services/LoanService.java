package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {

    List<Loan> getAllLoans();

    List<LoanDTO> loansToLoansDTO(List<Loan> loans);

    Loan getLoanById(Long id);

    Loan getLoanByName(String name);

    void saveLoan(Loan loan);
}
