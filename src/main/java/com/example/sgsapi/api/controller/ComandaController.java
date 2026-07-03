package com.example.sgsapi.api.controller;

import com.example.sgsapi.api.dto.ComandaDTO;
import com.example.sgsapi.service.ComandaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comandas")
@RequiredArgsConstructor
@CrossOrigin
public class ComandaController {

    private final ComandaService service;

    // ABRIR COMANDA
    // Exemplo de URL: http://localhost:8080/api/v1/comandas?numeroMesa=5&nomeCliente=Joao
    @PostMapping()
    public ResponseEntity abrirComanda(
            @RequestParam int numeroMesa,
            @RequestParam(required = false) String nomeCliente) {
        try {
            Long idComanda = service.abrirComanda(nomeCliente, numeroMesa);
            return ResponseEntity.status(HttpStatus.CREATED).body("Comanda aberta com sucesso! ID: " + idComanda);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // VISUALIZAR CONTA
    // URL: http://localhost:8080/api/v1/comandas/1
    @GetMapping("/{id}")
    public ResponseEntity visualizarConta(@PathVariable("id") Long id) {
        try {
            ComandaDTO dto = service.visualizarConta(id);
            return ResponseEntity.ok(dto); // 200 OK com os dados e itens da mesa
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // FECHAR COMANDA
    // URL: http://localhost:8080/api/v1/comandas/1/fechar
    @PutMapping("/{id}/fechar")
    public ResponseEntity fecharComanda(@PathVariable("id") Long id) {
        try {
            service.fecharComanda(id);
            return new ResponseEntity(HttpStatus.OK); // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}