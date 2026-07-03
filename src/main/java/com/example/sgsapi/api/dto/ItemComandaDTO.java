package com.example.sgsapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ItemComandaDTO {
    private Long Id;
    private Long IdComanda;
    private Long idItem;
    private Long idLote;
    private float quantidade;
}
