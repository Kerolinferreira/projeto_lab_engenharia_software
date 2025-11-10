package com.programacao.web.fatec.api_fatec.domain.pedido;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programacao.web.fatec.api_fatec.entities.ItemPedido;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    /**
     * Busca itens de pedido por ID do pedido.
     */
    List<ItemPedido> findByPedidoId(Long pedidoId);

    /**
     * Busca itens de pedido por ID do produto.
     */
    List<ItemPedido> findByProdutoId(Long produtoId);

    /**
     * Deleta todos os itens de um pedido.
     */
    void deleteByPedidoId(Long pedidoId);
}
