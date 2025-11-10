package com.programacao.web.fatec.api_fatec.domain.pedido.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para atualização de pedido (PUT).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoPutDto {
    private LocalDate dataPedido;
    private Long clienteId;
    private List<ItemPedidoDto> itens;
    private Double desconto;
    private Double frete;
    private String formaPagamento;
    private String condicaoPagamento;
    private LocalDate previsaoEntrega;
    private String observacoes;
}
