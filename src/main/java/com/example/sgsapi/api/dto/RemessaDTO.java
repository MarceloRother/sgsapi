package com.example.sgsapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class RemessaDTO {
    private Long id;
    private Long fornecedor_id;
    private LocalDate dataChegada;
    private Long numeroNotaFiscal;
    private float custoTotalFrete;
}
