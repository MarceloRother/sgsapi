package com.example.sgsapi.model.repository;

import com.example.sgsapi.model.entity.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoteRepository extends JpaRepository<Lote, Long> {
    @Query("SELECT SUM(l.quantidade) FROM Lote l WHERE l.item.id = :idItem")
    Integer calcularEstoqueTotalDoItem(@Param("idItem") Long idItem);

    List<Lote> findByItemIdAndQuantidadeGreaterThanOrderByDataValidadeAsc(Long itemId, float quantidadeMinima);
    List<Lote> findByItemIdAndQuantidadeGreaterThanOrderByDataValidadeDesc(Long itemId, float qtd);
}
