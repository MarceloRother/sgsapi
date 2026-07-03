package com.example.sgsapi.api.controller;

import com.example.sgsapi.api.dto.ItemDTO;
import com.example.sgsapi.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/itens")
@RequiredArgsConstructor
@CrossOrigin
public class ItemController {

    private final ItemService service;

    // CADASTRAR ITEM
    @PostMapping()
    public ResponseEntity post(@RequestBody ItemDTO dto) {
        try {
            service.cadastrarItem(dto);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // LISTAR TODOS OS ITENS
    // URL: http://localhost:8080/api/v1/itens
    @GetMapping()
    public ResponseEntity getItens() {
        return ResponseEntity.ok(service.getItens());
    }

    // LISTAR TODOS OS ITENS ATIVOS
    // URL: http://localhost:8080/api/v1/itens
    @GetMapping("/ativos")
    public ResponseEntity getItensAtivos() {
        return ResponseEntity.ok(service.getItensAtivos());
    }

    // BUSCAR ITEM POR ID
    // URL: http://localhost:8080/api/v1/itens/4
    @GetMapping("/{id}")
    public ResponseEntity getItemById(@PathVariable("id") Long id) {
        try {
            ItemDTO dto = service.getItemById(id);
            return ResponseEntity.ok(dto); // Retorna 200 OK com o JSON do ItemDTO
        } catch (RuntimeException e) {
            // Se o Service gritar "Item não encontrado", o Controller devolve 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // BUSCAR ITEM POR NOME (GET)
    // URL: http://localhost:8080/api/v1/itens/nome/Picole de Limao
    @GetMapping("/nome/{nome}")
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
    public ResponseEntity inativar(@PathVariable("id") Long id) {
        try {
            service.inativarItem(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // EXCLUIR ITEM (DELETE)
    @DeleteMapping("/{id}/deletar")
    public ResponseEntity deletar(@PathVariable("id") Long id) {
        try {
            service.deletarItem(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}