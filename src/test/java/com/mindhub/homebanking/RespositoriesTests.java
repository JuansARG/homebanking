package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RespositoriesTests {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ClientLoanRepository clientLoanRepository;

    @Autowired
    TransactionRepository transactionRepository;

//    @Test
//    public void existClients(){
//        List<Client> clients = clientRepository.findAll();
//        assertThat(clients, is(not(empty())));
//    }
//
//    @Test
//    public void findMelbaByEmail(){
//        Client melba = clientRepository.findByEmail("melba@mindhub.com");
//        assertThat(melba, hasProperty("firstName", is("Melba")));
//    }
//
//    @Test
//    public void existAccounts(){
//        List<Account> accounts = accountRepository.findAll();
//        assertThat(accounts, is(not(empty())));
//    }
//
//    @Test
//    public void existCards(){
//        List<Card> cards = cardRepository.findAll();
//        assertThat(cards, is(not(empty())));
//    }
//
//    @Test
//    public void existLoans(){
//        List<Loan> loans = loanRepository.findAll();
//        assertThat(loans, is(not(empty())));
//    }
//
//    @Test
//    public void existPersonalLoan(){
//        List<Loan> loans = loanRepository.findAll();
//        assertThat(loans, hasItem(hasProperty("name", is("Personal Loan"))));
//    }
//
//    @Test
//    public void existClientLoan(){
//        List<ClientLoan> clientLoans = clientLoanRepository.findAll();
//        assertThat(clientLoans, is(not(empty())));
//    }
//
//    @Test
//    public void existTransactions(){
//        List<Transaction> transactions = transactionRepository.findAll();
//        assertThat(transactions, is(not(empty())));
//    }
}
