package com.programacao.web.fatec.api_fatec.domain.pedido.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para representar um item de pedido.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoDto {
    private Long produtoId;
    private String nomeProduto;
    private Integer quantidade;
    private Double precoUnitario;
    private Double subtotal;
}
