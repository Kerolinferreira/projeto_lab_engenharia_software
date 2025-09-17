package com.programacao.web.fatec.api_fatec.domain.cliente.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para operações de criação de cliente (POST)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientePostDto {
    private String nome;
    private String endereco;
    private Long cidadeId;
}
