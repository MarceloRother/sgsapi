package com.example.sgsapi.api.controller;

import com.example.sgsapi.api.dto.RemessaDTO;
import com.example.sgsapi.service.RemessaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/remessas")
@RequiredArgsConstructor
@CrossOrigin
public class RemessaController {

    private final RemessaService service;

    // CADASTRAR REMESSA
    // URL: http://localhost:8080/api/v1/remessas
    @PostMapping()
    public ResponseEntity post(@RequestBody RemessaDTO dto) {
        try {
            service.cadastraRemessa(dto);
            return new ResponseEntity(HttpStatus.CREATED); // Retorna 201 Created
        } catch (RuntimeException e) {
            // Se o Fornecedor não for encontrado, cai aqui
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // LISTAR TODAS AS REMESSAS
    // URL: http://localhost:8080/api/v1/remessas
    @GetMapping()
    public ResponseEntity getRemessas() {
        // Retorna 200 OK com a lista de RemessaDTO
        return ResponseEntity.ok(service.consultarRemessa());
    }

    // BUSCAR REMESSA POR ID
    // URL: http://localhost:8080/api/v1/remessas/1
    @GetMapping("/{id}")
    public ResponseEntity getRemessaById(@PathVariable("id") Long id) {
        try {
            RemessaDTO dto = service.consultarRemessaID(id);
            return ResponseEntity.ok(dto); // Retorna 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ATUALIZAR REMESSA
    // URL: http://localhost:8080/api/v1/remessas/1
    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody RemessaDTO dto) {
        try {
            dto.setId(id);

            service.alterarRemessa(dto);
            return new ResponseEntity(HttpStatus.OK); // Retorna 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // EXCLUI REMESSA
    // URL: https://localhost:8080/api/v1/remessas/1
    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable("id") Long id){
        try {
            service.deletarRemessa(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}