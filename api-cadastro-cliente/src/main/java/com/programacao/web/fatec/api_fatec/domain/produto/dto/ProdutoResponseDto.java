

package com.programacao.web.fatec.api_fatec.domain.produto.dto;


import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor

/**
 * DTO de saída para retornar produtos na API.
 * Representa os dados expostos pela API sem expor diretamente a entidade JPA.
 */
public class ProdutoResponseDto {
    private Long id; 
    private String nome_produto;
    private String descricao;
    private int qtd_estoque;
    private double preco;
}
