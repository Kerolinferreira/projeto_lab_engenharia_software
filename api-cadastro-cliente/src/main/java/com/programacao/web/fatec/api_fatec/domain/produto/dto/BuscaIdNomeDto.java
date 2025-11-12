package com.programacao.web.fatec.api_fatec.domain.produto.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class BuscaIdNomeDto {
    private Long id;
    private String nome_produto;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNome_produto() {
        return nome_produto;
    }
    public void setNome_produto(String nome_produto) {
        this.nome_produto = nome_produto;
    }
}
