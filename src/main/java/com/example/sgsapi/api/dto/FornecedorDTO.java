package com.example.sgsapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FornecedorDTO {
    private Long id;
    private String razaoSocial;
    private String cnpj;
    private String telefoneContato;
    private String email;
}
