package com.example.sgsapi.api.controller;

import com.example.sgsapi.api.dto.RemessaDTO;
import com.example.sgsapi.service.RemessaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Remessa", description = "API de gerenciamento das remessas.")
@RestController
@RequestMapping("/api/v1/remessas")
@RequiredArgsConstructor
@CrossOrigin
public class RemessaController {

    private final RemessaService service;

    @PostMapping()
    @Operation(summary = "Cadastra uma remessa")
    public ResponseEntity post(@RequestBody RemessaDTO dto) {
        try {
            Long id = service.cadastraRemessa(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Remessa cadastrada com sucesso! ID: " + id); // Retorna 201 Created
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping()
    @Operation(summary = "Visualiza todas as remessas")
    public ResponseEntity getRemessas() {
        // Retorna 200 OK com a lista de RemessaDTO
        return ResponseEntity.ok(service.consultarRemessa());
    }

    // BUSCAR REMESSA POR ID
    // URL: http://localhost:8080/api/v1/remessas/1
    @GetMapping("/{id}")
    @Operation(summary = "Visualiza uma remessa em específico")
    public ResponseEntity getRemessaById(@PathVariable("id") Long id) {
        try {
            RemessaDTO dto = service.consultarRemessaID(id);
            return ResponseEntity.ok(dto); // Retorna 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atiualiza os dados de uma remessa")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody RemessaDTO dto) {
        try {
            dto.setId(id);

            service.alterarRemessa(dto);
            return new ResponseEntity(HttpStatus.OK); // Retorna 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta uma remessa")
    public ResponseEntity deletar(@PathVariable("id") Long id){
        try {
            service.deletarRemessa(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}