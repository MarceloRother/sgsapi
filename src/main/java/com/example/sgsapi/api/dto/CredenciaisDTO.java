package com.example.sgsapi.api.dto;

import lombok.Data;

@Data // O Lombok já cria os Getters e Setters automaticamente
public class CredenciaisDTO {
    private String login;
    private String senha;
}