package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @RequestMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientService.clientsToClientsDTO(clientService.getAllClients());
    }

    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        return clientService.clientToClientDTO(clientService.getClientById(id));
    }


    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register( @RequestParam String firstname,
                                            @RequestParam String lastname,
                                            @RequestParam String email,
                                            @RequestParam String password
    ){
        if(firstname.isEmpty()){
            return new ResponseEntity<>("First name is empty", HttpStatus.FORBIDDEN);
        }

        if(lastname.isEmpty()){
            return new ResponseEntity<>("Last name is empty", HttpStatus.FORBIDDEN);
        }

        if(email.isEmpty()){
            return new ResponseEntity<>("Email is empty", HttpStatus.FORBIDDEN);
        }

        if(password.isEmpty()){
            return new ResponseEntity<>("Password is empty", HttpStatus.FORBIDDEN);
        }

        if(clientService.getClientByEmail(email) != null){
            return new ResponseEntity<>("Email already in use", HttpStatus.CONFLICT);
        }

        Client client = clientService.createClient(firstname, lastname, email, password);
        clientService.saveClient(client);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping("/clients/current")
    public ClientDTO getClient(Authentication auth){
        return clientService.clientToClientDTO(clientService.getClientByEmail(auth.getName()));
    }



}
