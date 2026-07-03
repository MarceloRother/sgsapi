package com.example.sgsapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class LoteDTO {
    private Long id;
    private Long idItem;
    private Long idRemessa;
    private String nome;
    private LocalDate dataValidade;
    private float custoFabrica;
    private float quantidade;
}
