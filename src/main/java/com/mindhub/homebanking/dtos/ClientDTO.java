package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;

import java.util.List;
import java.util.stream.Collectors;

public class ClientDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<AccountDTO> account;
    private List<ClientLoanDTO> loans;
    private List<CardDTO> cards;

    public ClientDTO(Client client){
        id = client.getId();
        firstName = client.getFirstName();
        lastName = client.getLastName();
        email = client.getEmail();
        //account = client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
        account = client.getAccounts().stream().filter(Account::isEnable).map(AccountDTO::new).toList();
        loans = client.getLoans().stream().filter(ClientLoan::isEnable).map(ClientLoanDTO::new).toList();
        cards = client.getCards().stream().filter(Card::isEnable).map(CardDTO::new).toList();
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public List<AccountDTO> getAccount() {
        return account;
    }

    public List<ClientLoanDTO> getLoans() {
        return loans;
    }

    public List<CardDTO> getCards() {
        return cards;
    }
}
