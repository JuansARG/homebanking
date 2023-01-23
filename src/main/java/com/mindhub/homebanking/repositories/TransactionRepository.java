package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    //AGREGAR LA QUERY PARA FILTRAR POR ID CUENTA
    //sin efecto el mensaje de arriba
    //lo solucione filtrando todas las cuenta que me devuelve este metodo,
    // las filtre y extra unicamente que las tienen tiene una cuenta asociado con un numero x


}
