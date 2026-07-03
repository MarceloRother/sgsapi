package com.example.sgsapi.model.entity;


import com.example.sgsapi.model.enums.Perfil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;
    private String nome;
    private String senha;
    private String dataNascimento;
    private String dataContratacao;

    @Enumerated(EnumType.STRING)
    private Perfil grupo;
}
