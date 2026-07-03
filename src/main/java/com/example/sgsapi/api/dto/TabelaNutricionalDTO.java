package com.example.sgsapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class TabelaNutricionalDTO {
    private Long id;
    private Long idItem;
    private float calorias;
    private float gorduras;
    private float acucares;
    private boolean contemGluten;
    private boolean contemLactose;
    private boolean contemLeite;
}
