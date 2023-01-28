package com.mindhub.homebanking;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import java.util.List;

@SpringBootTest
public class ServicesTests {

    @Autowired
    private ClientService clientService;

//    @Test
//    public void clientToClientDTO(){
//        ClientDTO client = clientService.clientToClientDTO(clientService.getClientById(1L));
//        assertThat(client, isA(ClientDTO.class));
//    }
//
//    @Test
//    public void clientsToClientsDTO(){
//        List<ClientDTO> clients = clientService.clientsToClientsDTO(clientService.getAllClients());
//        assertThat(clients, is(not(empty())));
//        assertThat(clients, everyItem(isA(ClientDTO.class)));
//    }
//
//    @Test
//    public void createClientAndPropertyValidation(){
//        Client clientTest = clientService.createClient("prueba", "prueba", "prueba@prueba.com", "prueba");
//        assertThat(clientTest, isA(Client.class));
//        assertThat(clientTest, hasProperty("firstName", is("prueba")));
//        assertThat(clientTest, hasProperty("lastName", is("prueba")));
//        assertThat(clientTest, hasProperty("email", is(not(nullValue()))));
//        assertThat(clientTest, hasProperty("email", allOf(is(not(emptyOrNullString())), isA(String.class))));
//        assertThat(clientTest, hasProperty("password", is(not(emptyOrNullString()))));
//    }
}
