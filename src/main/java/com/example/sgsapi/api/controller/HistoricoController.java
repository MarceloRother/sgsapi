package com.example.sgsapi.api.controller;

import com.example.sgsapi.api.dto.HistoricoDTO;
import com.example.sgsapi.service.HistoricoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Histórico", description = "API de gerenciamento do histórico de movimentações das compras.")
@RestController
@RequestMapping("/api/v1/historicos")
@RequiredArgsConstructor
@CrossOrigin
public class HistoricoController {

    private final HistoricoService service;

    @GetMapping()
    @Operation(summary = "Visualiza o histórico completo")
    public ResponseEntity getHistoricoCompleto() {
        return ResponseEntity.ok(service.buscarTodoHistorico());
    }

    @GetMapping("/hoje")
    @Operation(summary = "Visualiza o histórico hoje")
    public ResponseEntity getHistoricoHoje() {
        return ResponseEntity.ok(service.buscarMovimentacoesDeHoje());
    }

    @GetMapping("/lote/{loteId}") // <-- Mudei de /produto/{produtoId} para /lote/{loteId}
    @Operation(summary = "Visualiza o histórico por lote")
    public ResponseEntity<?> getHistoricoPorLote(@PathVariable("idLote") Long idLote) {
        // Retorna a lista de movimentações apenas do ID especificado na URL
        return ResponseEntity.ok(service.buscarHistoricoPorLote(idLote));
    }
}