package com.example.sgsapi.api.controller;

import com.example.sgsapi.api.dto.HistoricoDTO;
import com.example.sgsapi.service.HistoricoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/historicos")
@RequiredArgsConstructor
@CrossOrigin
public class HistoricoController {

    private final HistoricoService service;

    // LISTAR TODO O HISTÓRICO
    // URL: http://localhost:8080/api/v1/historicos
    @GetMapping()
    public ResponseEntity getHistoricoCompleto() {
        return ResponseEntity.ok(service.buscarTodoHistorico());
    }

    // LISTAR MOVIMENTAÇÕES APENAS DE HOJE
    // URL: http://localhost:8080/api/v1/historicos/hoje
    @GetMapping("/hoje")
    public ResponseEntity getHistoricoHoje() {
        return ResponseEntity.ok(service.buscarMovimentacoesDeHoje());
    }

    // LISTAR HISTÓRICO POR PRODUTO/LOTE
    // URL: http://localhost:8080/api/v1/historicos/produto/4
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity getHistoricoPorProduto(@PathVariable("produtoId") Long produtoId) {
        // Retorna a lista de movimentações apenas do ID especificado na URL
        return ResponseEntity.ok(service.buscarHistoricoPorProduto(produtoId));
    }
}