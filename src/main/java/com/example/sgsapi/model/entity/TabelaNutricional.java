package com.example.sgsapi.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

public class TabelaNutricional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "id_item")
    private Item item;

    private float calorias;
    private float gorduras;
    private float acucares;
    private boolean contemGluten;
    private boolean contemLactose;
    private boolean contemLeite;
}
