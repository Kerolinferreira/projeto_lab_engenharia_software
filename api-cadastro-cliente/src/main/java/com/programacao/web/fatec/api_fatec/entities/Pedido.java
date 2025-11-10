package com.programacao.web.fatec.api_fatec.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade que representa um pedido no sistema.
 * Um pedido possui um cliente, itens, valores e informações de pagamento/entrega.
 */
@Entity
@Table(name = "pedido")
@Getter
@Setter
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate dataPedido;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    @Column(nullable = false)
    private Double desconto = 0.0;

    @Column(nullable = false)
    private Double frete = 0.0;

    @Column(nullable = false)
    private Double total = 0.0;

    @Column(length = 50)
    private String formaPagamento;

    @Column(length = 50)
    private String condicaoPagamento;

    @Column
    private LocalDate previsaoEntrega;

    @Column(length = 500)
    private String observacoes;

    public Pedido(LocalDate dataPedido, Cliente cliente, Double desconto, Double frete,
                  String formaPagamento, String condicaoPagamento, LocalDate previsaoEntrega,
                  String observacoes) {
        this.dataPedido = dataPedido;
        this.cliente = cliente;
        this.desconto = desconto != null ? desconto : 0.0;
        this.frete = frete != null ? frete : 0.0;
        this.formaPagamento = formaPagamento;
        this.condicaoPagamento = condicaoPagamento;
        this.previsaoEntrega = previsaoEntrega;
        this.observacoes = observacoes;
    }

    /**
     * Adiciona um item ao pedido e recalcula o total.
     */
    public void adicionarItem(ItemPedido item) {
        itens.add(item);
        item.setPedido(this);
        calcularTotal();
    }

    /**
     * Remove um item do pedido e recalcula o total.
     */
    public void removerItem(ItemPedido item) {
        itens.remove(item);
        item.setPedido(null);
        calcularTotal();
    }

    /**
     * Calcula o total do pedido baseado nos itens, desconto e frete.
     */
    public void calcularTotal() {
        Double subtotal = itens.stream()
            .mapToDouble(ItemPedido::getSubtotal)
            .sum();

        this.total = subtotal - this.desconto + this.frete;
    }
}
