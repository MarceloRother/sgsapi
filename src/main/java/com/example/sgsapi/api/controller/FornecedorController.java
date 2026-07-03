package com.example.sgsapi.api.controller;

import com.example.sgsapi.api.dto.FornecedorDTO;
import com.example.sgsapi.service.FornecedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/fornecedores")
@RequiredArgsConstructor
@CrossOrigin
public class FornecedorController {

    private final FornecedorService service;

    // CADASTRAR FORNECEDOR
    // URL: http://localhost:8080/api/v1/fornecedores
    @PostMapping()
    public ResponseEntity post(@RequestBody FornecedorDTO dto) {
        try {
            service.cadastrarFornecedor(dto);
            return new ResponseEntity(HttpStatus.CREATED); // 201 Created
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // 400 Bad Request
        }
    }

    // LISTAR TODOS OS FORNECEDORES
    // URL: http://localhost:8080/api/v1/fornecedores
    @GetMapping()
    public ResponseEntity getFornecedores() {
        return ResponseEntity.ok(service.consultarFornecedores()); // 200 OK
    }

    // BUSCAR FORNECEDOR POR ID (GET)
    // URL: http://localhost:8080/api/v1/fornecedores/1
    @GetMapping("/{id}")
    public ResponseEntity getFornecedorById(@PathVariable("id") Long id) {
        try {
            FornecedorDTO dto = service.consultarFornecedorPorId(id);
            return ResponseEntity.ok(dto); // 200 OK
        } catch (RuntimeException e) {
            // 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ATUALIZAR FORNECEDOR (PUT)
    // URL: http://localhost:8080/api/v1/fornecedores/1
    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody FornecedorDTO dto) {
        try {
            dto.setId(id);
            service.alterarFornecedor(id, dto);
            return new ResponseEntity(HttpStatus.OK); // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // 400 Bad Request
        }
    }

    // INATIVAR FORNECEDOR (DELETE)
    // URL: http://localhost:8080/api/v1/fornecedores/1
    @DeleteMapping("/{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        try {
            service.inativarFornecedor(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT); // 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // 400 Bad Request
        }
    }
}