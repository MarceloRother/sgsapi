package com.example.sgsapi.api.dto;

import com.example.sgsapi.model.enums.Perfil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String login;
    private String nome;
    private String senha;
    private LocalDate dataNascimento;
    private LocalDate dataContratacao;
    private Perfil grupo;
}
