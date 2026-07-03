package com.example.sgsapi.api.controller;

import com.example.sgsapi.api.dto.CredenciaisDTO;
import com.example.sgsapi.api.dto.TokenDTO;
import com.example.sgsapi.api.dto.UsuarioDTO;
import com.example.sgsapi.model.entity.Usuario;
import com.example.sgsapi.model.repository.UsuarioRepository;
import com.example.sgsapi.security.JwtService;
import com.example.sgsapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<?> autenticar(@RequestBody CredenciaisDTO dto) {
        try {
            // Prepara e valida as credenciais
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getSenha());
            authenticationManager.authenticate(authToken);

            // Busca o usuário REAL e COMPLETO no banco de dados!
            // (Certifique-se de ter o usuarioRepository injetado no Controller)
            Usuario usuarioReal = usuarioRepository.findByLogin(dto.getLogin()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // Gera o token usando o usuário que tem ID, Nome, Grupo, etc.
            String token = jwtService.gerarToken(usuarioReal);

            return ResponseEntity.ok(new TokenDTO(usuarioReal.getLogin(), token));

        } catch (Exception e) {
            // 1. Imprime o erro exato no terminal do IntelliJ!
            e.printStackTrace();

            // 2. Devolve a mensagem real de erro para o Swagger/Postman
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Motivo da falha: " + e.getMessage());
            //return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login ou senha inválidos!");
        }
    }

    // CADASTRAR USUÁRIO (POST)
    // URL: http://localhost:8080/api/v1/usuarios
    @PostMapping()
    public ResponseEntity cadastrarUsuario(@RequestBody UsuarioDTO dto) {
        try {
            service.cadastrarUsuario(dto);
            return new ResponseEntity(HttpStatus.CREATED); // Retorna 201 Created
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Retorna 400 Bad Request
        }
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        // Chama o método que você acabou de criar no Service
        List<UsuarioDTO> lista = service.listarUsuarios();

        // Retorna o HTTP 200 (OK) enviando a lista formatada no corpo da resposta
        return ResponseEntity.ok(lista);
    }

    // ALTERAR USUÁRIO (PUT)
    // URL: http://localhost:8080/api/v1/usuarios/1
    @PutMapping("/{id}")
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
    public ResponseEntity excluirUsuario(@PathVariable("id") Long id) {
        try {
            service.excluirUsuario(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT); // Retorna 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}