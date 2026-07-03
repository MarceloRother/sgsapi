package com.example.sgsapi.model.entity;

import com.example.sgsapi.model.enums.TipoMovimentacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

public class Historico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime data;
    private Long idProduto;
    private float quantidade;

    @Enumerated(EnumType.STRING)
    private TipoMovimentacao tipoOperacao; // [cite: 32]
}
