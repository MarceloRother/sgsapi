package com.example.sgsapi.api.controller;

import com.example.sgsapi.api.dto.CredenciaisDTO;
import com.example.sgsapi.api.dto.TokenDTO;
import com.example.sgsapi.api.dto.UsuarioDTO;
import com.example.sgsapi.model.entity.Usuario;
import com.example.sgsapi.model.repository.UsuarioRepository;
import com.example.sgsapi.security.JwtService;
import com.example.sgsapi.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuario", description = "API de gerenciamento dos itens.")
@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@CrossOrigin
public class UsuarioController {

    private final UsuarioService service;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/auth")
    @Operation(summary = "Autentica um usuário")
    public ResponseEntity<?> autenticar(@RequestBody CredenciaisDTO dto) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getSenha());
            authenticationManager.authenticate(authToken);

            Usuario usuarioReal = usuarioRepository.findByLogin(dto.getLogin()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            String token = jwtService.gerarToken(usuarioReal);

            return ResponseEntity.ok(new TokenDTO(usuarioReal.getLogin(), token));

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login ou senha inválidos!\nMotivo da falha: " + e.getMessage());
        }
    }

    @PostMapping()
    @Operation(summary = "Cadastra um usuário")
    public ResponseEntity cadastrarUsuario(@RequestBody UsuarioDTO dto) {
        try {
            Long id = service.cadastrarUsuario(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso! ID: " + id); // Retorna 201 Created
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Retorna 400 Bad Request
        }
    }

    @GetMapping
    @Operation(summary = "Visualiza todos usuários")
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        // Chama o método que você acabou de criar no Service
        List<UsuarioDTO> lista = service.listarUsuarios();

        // Retorna o HTTP 200 (OK) enviando a lista formatada no corpo da resposta
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Altera um usuário")
    public ResponseEntity alterarUsuario(@PathVariable("id") Long id, @RequestBody UsuarioDTO dto) {
        try {
            // Desempacota o DTO para enviar os parâmetros que o Service exige
            service.alterarUsuario(id, dto.getNome(), dto.getLogin(), dto.getSenha(), dto.getGrupo());
            return new ResponseEntity(HttpStatus.OK); // Retorna 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // EXCLUIR USUÁRIO (DELETE)
    // URL: http://localhost:8080/api/v1/usuarios/1
    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um usuário")
    public ResponseEntity excluirUsuario(@PathVariable("id") Long id) {
        try {
            service.excluirUsuario(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT); // Retorna 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}