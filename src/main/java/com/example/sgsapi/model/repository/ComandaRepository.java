package com.example.sgsapi.model.repository;

import com.example.sgsapi.model.entity.Comanda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComandaRepository extends JpaRepository<Comanda, Long> {
}
