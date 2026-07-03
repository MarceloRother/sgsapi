package com.example.sgsapi.model.repository;

import com.example.sgsapi.model.entity.Historico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoricoRepository extends JpaRepository<Historico, Long> {
    List<Historico> findByIdLote(Long id);

    List<Historico> findByDataBetween(LocalDateTime dataInicio, LocalDateTime dataFim);

    // Busca tudo o que aconteceu DEPOIS de uma certa data
    List<Historico> findByDataAfter(LocalDateTime data);

    // Busca tudo o que aconteceu ANTES de uma certa data
    List<Historico> findByDataBefore(LocalDateTime data);
}
