package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    LoanRepository loanRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    ClientLoanRepository clientLoanRepository;

    @GetMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanRepository.findAll().stream().map(LoanDTO::new).toList();
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> requestLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication auth){

        Client currentClient = clientRepository.findByEmail(auth.getName());
        Loan currentLoan = loanRepository.findById(loanApplicationDTO.getIdOfLoan()).orElse(null);
        Account destinationAccount = accountRepository.findByNumber(loanApplicationDTO.getDestinationAccountNumber());

        if(loanApplicationDTO.getAmount() <= 0){
            return new ResponseEntity<>("Invalid amount.", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.getPayments() <= 0){
            return new ResponseEntity<>("Invalid quotas.", HttpStatus.FORBIDDEN);
        }

        if(currentLoan == null){
            return new ResponseEntity<>("The type of loan requested does not exist.", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.getAmount() > currentLoan.getMaxAmount()){
            return new ResponseEntity<>("The amount exceeds the requested loan limit.", HttpStatus.FORBIDDEN);
        }

        if(!currentLoan.getPayments().contains(loanApplicationDTO.getPayments())){
            return new ResponseEntity<>("The requested installments do not correspond to the type of loan requested.", HttpStatus.FORBIDDEN);
        }

        if(destinationAccount == null){
            return new ResponseEntity<>("The destination account does not exist.", HttpStatus.FORBIDDEN);
        }

        if(currentClient.getAccounts().stream().noneMatch(account -> account.getId().equals(destinationAccount.getId()))){
            return new ResponseEntity<>("The destination account does not belong to the authenticated client.", HttpStatus.FORBIDDEN);
        }

        double finalAmount = loanApplicationDTO.getAmount() + (loanApplicationDTO.getAmount() * 0.20);

        Transaction transaction = new Transaction(TransactionType.CREDIT, finalAmount, currentLoan.getName() + " Loan Approved", LocalDateTime.now(), destinationAccount);
        transactionRepository.save(transaction);

        destinationAccount.setBalance(destinationAccount.getBalance() + loanApplicationDTO.getAmount());
        accountRepository.save(destinationAccount);

        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount(), loanApplicationDTO.getPayments(), currentClient, currentLoan, LocalDate.now());
        clientLoanRepository.save(clientLoan);

        return new ResponseEntity<>("Everything has gone well!", HttpStatus.CREATED);
    }

}
