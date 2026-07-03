package com.example.sgsapi.api.controller;

import com.example.sgsapi.api.dto.ComandaDTO;
import com.example.sgsapi.api.dto.ItemComandaDTO;
import com.example.sgsapi.service.ComandaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Comanda", description = "API de gerenciamento da comanda.")
@RestController
@RequestMapping("/api/v1/comandas")
@RequiredArgsConstructor
@CrossOrigin
public class ComandaController {

    private final ComandaService service;

    @PostMapping()
    @Operation(summary = "Abrir comanda")
    public ResponseEntity abrirComanda(){
        try {
            Long idComanda = service.abrirComanda();
            return ResponseEntity.status(HttpStatus.CREATED).body("Comanda aberta com sucesso! ID: " + idComanda);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/itens/add")
    @Operation(summary = "Adicionar item à comanda")
    public ResponseEntity addItem(@RequestBody ItemComandaDTO dto){
        try {
            service.addItem(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Item adicionado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/itens/alterar")
    @Operation(summary = "Alterar um item que já foi adicionado à comanda")
    public ResponseEntity alteraItem(ItemComandaDTO dto){
        try {
            service.alteraItem(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Item alterado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/itens/deletar")
    @Operation(summary = "Deletar um item que foi adicionado à comanda")
    public ResponseEntity deletaItem(ItemComandaDTO dto){
        try {
            service.deletaItem(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Item deletado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Vizualizar uma comanda em específico")
    public ResponseEntity visualizarConta(@PathVariable("id") Long id) {
        try {
            ComandaDTO dto = service.visualizarConta(id);
            return ResponseEntity.ok(dto); // 200 OK com os dados e itens da mesa
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping()
    @Operation(summary = "Vizualizar todas as comandas")
    public ResponseEntity visualizarComandas() {
        try {
            List<ComandaDTO> dto = service.visualizarComandas();
            return ResponseEntity.ok(dto); // 200 OK com os dados e itens da mesa
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/fechar")
    @Operation(summary = "Fechar uma comanda")
    public ResponseEntity fecharComanda(@PathVariable("id") Long id) {
        try {
            service.fecharComanda(id);
            return new ResponseEntity(HttpStatus.OK); // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}