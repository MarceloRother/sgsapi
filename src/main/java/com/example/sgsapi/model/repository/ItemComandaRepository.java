package com.example.sgsapi.model.repository;

import com.example.sgsapi.model.entity.ItemComanda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemComandaRepository extends JpaRepository<ItemComanda, Long> {
    List<ItemComanda> findByComandaId(Long idComanda);
}
