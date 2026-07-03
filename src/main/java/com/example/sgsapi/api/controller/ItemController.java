package com.example.sgsapi.api.controller;

import com.example.sgsapi.api.dto.ItemDTO;
import com.example.sgsapi.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Item", description = "API de gerenciamento dos itens.")
@RestController
@RequestMapping("/api/v1/itens")
@RequiredArgsConstructor
@CrossOrigin
public class ItemController {

    private final ItemService service;

    @PostMapping()
    @Operation(summary = "Cadastrar um item")
    public ResponseEntity post(@RequestBody ItemDTO dto) {
        try {
            Long id = service.cadastrarItem(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Item cadastrado com sucesso! ID: " + id);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping()
    @Operation(summary = "Visualiza todos os itens cadastrados.")
    public ResponseEntity getItens() {
        return ResponseEntity.ok(service.getItens());
    }

    @GetMapping("/ativos")
    @Operation(summary = "Visualiza todos os itens cadastrados e ativos.")
    public ResponseEntity getItensAtivos() {
        return ResponseEntity.ok(service.getItensAtivos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Visualiza um item em específico pelo id")
    public ResponseEntity getItemById(@PathVariable("id") Long id) {
        try {
            ItemDTO dto = service.getItemById(id);
            return ResponseEntity.ok(dto); // Retorna 200 OK com o JSON do ItemDTO
        } catch (RuntimeException e) {
            // Se o Service gritar "Item não encontrado", o Controller devolve 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/nome/{nome}")
    @Operation(summary = "Visualiza um item em específico pelo nome")
    public ResponseEntity getItemByName(@PathVariable("nome") String nome) {
        try {
            ItemDTO dto = service.getItemByName(nome);
            return ResponseEntity.ok(dto); // Retorna 200 OK com o JSON do ItemDTO
        } catch (RuntimeException e) {
            // Retorna 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ATUALIZAR ITEM (PUT)
    @PutMapping("/{id}/atualizar")
    @Operation(summary = "Atualiza um item")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ItemDTO dto) {
        try {
            service.alterarItem(id, dto);
            return new ResponseEntity(HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // INATIVAR ITEM (DELETE)
    @DeleteMapping("/{id}/inativar")
    @Operation(summary = "Inativa um item")
    public ResponseEntity inativar(@PathVariable("id") Long id) {
        try {
            service.inativarItem(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // EXCLUIR / INATIVAR ITEM (DELETE)
    @DeleteMapping("/{id}/deletar")
    @Operation(summary = "Deleta permanetemente um item")
    public ResponseEntity deletar(@PathVariable("id") Long id) {
        try {
            service.deletarItem(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}