package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import java.util.List;

public interface ClientService {

    List<Client> getAllClients();

    Client getClientById(Long id);

    Client getClientByEmail(String email);

    void saveClient(Client client);

    List<ClientDTO> clientsToClientsDTO(List<Client> clients);

    ClientDTO clientToClientDTO(Client client);

    Client createClient(String firstName, String lastName, String email, String password);

}
