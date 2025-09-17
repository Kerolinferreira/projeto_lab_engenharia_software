package com.programacao.web.fatec.api_fatec.domain.cliente.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para operações de atualização de cliente (PUT)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientePutDto {
    private Long id;
    private String nome;
    private String endereco;
    private Long cidadeId;
}
