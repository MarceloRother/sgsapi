package com.example.sgsapi.model.repository;

import com.example.sgsapi.model.entity.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoteRepository extends JpaRepository<Lote, Long> {
    @Query("SELECT COALESCE(SUM(l.quantidade), 0) FROM Lote l WHERE l.item.id = :idItem")
    Float calcularEstoqueTotalDoItem(@Param("idItem") Long idItem);

    List<Lote> findByItemIdAndQuantidadeGreaterThanOrderByDataValidadeAsc(Long itemId, float quantidadeMinima);
    List<Lote> findByItemIdAndQuantidadeGreaterThanOrderByDataValidadeDesc(Long itemId, float qtd);
}
