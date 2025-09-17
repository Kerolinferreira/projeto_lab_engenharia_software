package com.programacao.web.fatec.api_fatec.domain.cliente.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuscaPorIdOuNomeDto {
    private Long id;
    private String nome;
}
