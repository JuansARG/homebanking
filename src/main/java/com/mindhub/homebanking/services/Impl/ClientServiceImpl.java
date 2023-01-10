package com.mindhub.homebanking.services.Impl;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public Client getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }

    @Override
    public List<ClientDTO> clientsToClientsDTO(List<Client> clients) {
        return clients.stream().map(ClientDTO::new).toList();
    }

    @Override
    public ClientDTO clientToClientDTO(Client client) {
        return new ClientDTO(client);
    }

    @Override
    public Client createClient(String firstName, String lastName, String email, String password) {
        return new Client(firstName, lastName, email, passwordEncoder.encode(password));
    }

}
