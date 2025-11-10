package com.programacao.web.fatec.api_fatec.domain.pedido.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de saída para retornar pedidos na API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDto {
    private Long id;
    private LocalDate dataPedido;
    private Long clienteId;
    private String clienteNome;
    private String clienteCnpj;
    private List<ItemPedidoDto> itens;
    private Double desconto;
    private Double frete;
    private Double total;
    private String formaPagamento;
    private String condicaoPagamento;
    private LocalDate previsaoEntrega;
    private String observacoes;
}
