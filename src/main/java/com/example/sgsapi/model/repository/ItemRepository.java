package com.example.sgsapi.model.repository;

import com.example.sgsapi.model.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByNome(String nome);
    List<Item> findByAtivoTrue();
}
