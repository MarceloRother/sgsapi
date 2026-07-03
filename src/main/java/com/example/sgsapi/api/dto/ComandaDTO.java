package com.example.sgsapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ComandaDTO {
    private Long id;
    private int numeroMesa;
    private LocalDateTime dataHoraAbertura;
    private LocalDateTime dataHoraFechamento;
    private boolean aberta;
    private List<ItemComandaDTO> itens;
}
