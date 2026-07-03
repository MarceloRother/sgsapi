package com.example.sgsapi.api.controller;

import com.example.sgsapi.api.dto.FornecedorDTO;
import com.example.sgsapi.service.FornecedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Fornecedor", description = "API de gerenciamento de fornecedores.")
@RestController
@RequestMapping("/api/v1/fornecedores")
@RequiredArgsConstructor
@CrossOrigin
public class FornecedorController {

    private final FornecedorService service;

    @PostMapping()
    @Operation(summary = "Cadastrar um fornecedor")
    public ResponseEntity post(@RequestBody FornecedorDTO dto) {
        try {
            Long id = service.cadastrarFornecedor(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Fornecedor cadastrado com sucesso! ID: " + id);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // 400 Bad Request
        }
    }

    @Operation(summary = "Visualizar todos os fornecedores")
    @GetMapping()
    public ResponseEntity getFornecedores() {
        return ResponseEntity.ok(service.consultarFornecedores()); // 200 OK
    }

    @GetMapping("/{id}")
    @Operation(summary = "Visualizar um fornecedor por id")
    public ResponseEntity getFornecedorById(@PathVariable("id") Long id) {
        try {
            FornecedorDTO dto = service.consultarFornecedorPorId(id);
            return ResponseEntity.ok(dto); // 200 OK
        } catch (RuntimeException e) {
            // 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza o cadastro de um fornecedor")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody FornecedorDTO dto) {
        try {
            dto.setId(id);
            service.alterarFornecedor(id, dto);
            return new ResponseEntity(HttpStatus.OK); // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // 400 Bad Request
        }
    }

    @Operation(summary = "Inativa o cadastro de um fornecedor")
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