
package com.programacao.web.fatec.api_fatec.domain.produto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para atualização de produto (PUT).
 * Mantém os mesmos campos obrigatórios do POST.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoPutDto {
    private Long id; 
    private String nome_produto;
    private String descricao;
    private int qtd_estoque;
    private double preco;
}
