
package com.programacao.web.fatec.api_fatec.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="produto", schema="cto")
@Getter
@Setter
@NoArgsConstructor

public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 
    @Column(nullable = false)
    private String nome_produto;
    @Column(nullable = false, length = 100)
    private String descricao;
    @Column(nullable = false)
    private int qtd_estoque;
    @Column(nullable = false)
    private double preco;

    {
        this.nome_produto = nome_produto;
        this.descricao = descricao;
        this.qtd_estoque = qtd_estoque;
        this.preco = preco;
    }
    
    public Produto (String nome_produto, String descricao, int qtd_estoque, double preco) {
        this.nome_produto = nome_produto;
        this.descricao = descricao;
        this.qtd_estoque = qtd_estoque;
        this.preco = preco;
    }
    

}
