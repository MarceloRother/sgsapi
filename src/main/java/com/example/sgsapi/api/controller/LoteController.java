package com.example.sgsapi.api.controller;

import com.example.sgsapi.api.dto.LoteDTO;
import com.example.sgsapi.service.LoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Lote", description = "API de gerenciamento dos lotes.")
@RestController
@RequestMapping("/api/v1/lotes")
@RequiredArgsConstructor
@CrossOrigin
public class LoteController {

    private final LoteService service;

    // CADASTRAR NOVO LOTE
    // URL: http://localhost:8080/api/v1/lotes
    @PostMapping()
    @Operation(summary = "Cadastra um lote")
    public ResponseEntity cadastrarLote(@RequestBody LoteDTO dto) {
        try {
            Long idLoteGerado = service.cadastrarLote(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Lote criado com ID: " + idLoteGerado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // LISTAR TODOS OS LOTES
    // URL: http://localhost:8080/api/v1/lotes
    @GetMapping()
    @Operation(summary = "Lista todos os lotes")
    public ResponseEntity listarLotes() {
        return ResponseEntity.ok(service.listarTodosOsLotes());
    }

    // LISTAR LOTE POR ID
    // URL: http://localhost:8080/api/v1/lotes
    @GetMapping("/{id}")
    @Operation(summary = "Visualiza um lote em específico")
    public ResponseEntity loteById(Long id) {
        return ResponseEntity.ok(service.loteByBid(id));
    }

    // CALCULAR ESTOQUE TOTAL DE UM ITEM
    // URL: http://localhost:8080/api/v1/lotes/estoque/item/4
    @GetMapping("/estoque/item/{idItem}")
    @Operation(summary = "Calcula o estoque de um lote")
    public ResponseEntity calcularEstoque(@PathVariable("idItem") Long idItem) {
        try {
            float total = service.calcularEstoqueItem(idItem);
            return ResponseEntity.ok(total);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DAR BAIXA NO LOTE
    // URL: http://localhost:8080/api/v1/lotes/1/baixa?quantidade=5.5
    @PutMapping("/{id}/baixa")
    @Operation(summary = "Da baixa em um lote")
    public ResponseEntity darBaixa(@PathVariable("id") Long id, @RequestParam float quantidade) {
        try {
            service.darBaixaLote(id, quantidade);
            return new ResponseEntity(HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}