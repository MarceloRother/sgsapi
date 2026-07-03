package com.example.sgsapi.api.dto;

import com.example.sgsapi.model.enums.TipoMovimentacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoDTO {
    private Long id;
    private Long idLote;
    private TipoMovimentacao tipo;
    private float quantidade;
    private LocalDateTime dataHoraMovimentacao;
    private String nomeItem;
}