package com.example.sgsapi.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

public class Comanda {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dataHoraAbertura;
    private String dataHoraFechamento;
    private boolean aberta;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "comanda_id")
    private List<ItemComanda> itens;
}
