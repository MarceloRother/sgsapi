package com.example.sgsapi.model.entity;

import com.example.sgsapi.model.enums.TipoItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nome;

    private float precoVenda;

    @Column(nullable = false)
    private boolean ativo = true;

    @Enumerated(EnumType.STRING)
    private TipoItem tipo;

    @OneToOne(mappedBy = "item", cascade = CascadeType.ALL)
    private TabelaNutricional tabelaNutricional;
}
