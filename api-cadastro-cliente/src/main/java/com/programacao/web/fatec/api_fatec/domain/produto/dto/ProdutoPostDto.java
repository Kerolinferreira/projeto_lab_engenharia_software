package com.programacao.web.fatec.api_fatec.domain.produto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para criação de produto (POST).
 * Todos os campos obrigatórios estão anotados com validações.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoPostDto {
    private Long id; 
    private String nome_produto;
    private String descricao;
    private int qtd_estoque;
    private double preco;
    private String modelo;
}
