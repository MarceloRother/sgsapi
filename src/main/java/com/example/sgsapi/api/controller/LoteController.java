package com.example.sgsapi.api.controller;

import com.example.sgsapi.api.dto.LoteDTO;
import com.example.sgsapi.service.LoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lotes")
@RequiredArgsConstructor
@CrossOrigin
public class LoteController {

    private final LoteService service;

    // CADASTRAR NOVO LOTE
    // URL: http://localhost:8080/api/v1/lotes
    @PostMapping()
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
    public ResponseEntity listarLotes() {
        return ResponseEntity.ok(service.listarTodosOsLotes());
    }

    // LISTAR LOTE POR ID
    // URL: http://localhost:8080/api/v1/lotes/1
    @GetMapping("/{id}")
    public ResponseEntity loteById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(service.loteByBid(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // CALCULAR ESTOQUE TOTAL DE UM ITEM
    // URL: http://localhost:8080/api/v1/lotes/estoque/item/4
    @GetMapping("/estoque/item/{idItem}")
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
    public ResponseEntity darBaixa(@PathVariable("id") Long id, @RequestParam float quantidade) {
        try {
            service.darBaixaLote(id, quantidade);
            return new ResponseEntity(HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}