package com.example.sgsapi.api.dto;

import com.example.sgsapi.model.enums.TipoItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ItemDTO {
    private Long id;
    private String nome;
    private float precoVenda;
    private TipoItem tipo;
    private TabelaNutricionalDTO tabelaNutricional;
}
