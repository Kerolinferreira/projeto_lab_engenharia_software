package com.programacao.web.fatec.api_fatec.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade que representa um item de pedido.
 * Relaciona um pedido com um produto, contendo quantidade e valores.
 */
@Entity
@Table(name = "item_pedido")
@Getter
@Setter
@NoArgsConstructor
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private Double precoUnitario;

    @Column(nullable = false)
    private Double subtotal;

    public ItemPedido(Pedido pedido, Produto produto, Integer quantidade, Double precoUnitario) {
        this.pedido = pedido;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.subtotal = quantidade * precoUnitario;
    }

    /**
     * Calcula o subtotal do item (quantidade * preço unitário).
     */
    public void calcularSubtotal() {
        this.subtotal = this.quantidade * this.precoUnitario;
    }

    /**
     * Atualiza a quantidade e recalcula o subtotal.
     */
    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
        calcularSubtotal();
    }

    /**
     * Atualiza o preço unitário e recalcula o subtotal.
     */
    public void setPrecoUnitario(Double precoUnitario) {
        this.precoUnitario = precoUnitario;
        calcularSubtotal();
    }
}
