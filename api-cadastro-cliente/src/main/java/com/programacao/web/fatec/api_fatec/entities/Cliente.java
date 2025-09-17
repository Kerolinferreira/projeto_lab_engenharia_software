package com.programacao.web.fatec.api_fatec.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade que representa um cliente no sistema.
 * Um cliente possui um id, um nome, um endereço e está associado a uma cidade.
 */
@Entity
@Table(name="cadastro_cliente")
@Getter
@Setter
@NoArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @Column(nullable = false)   
    private String loja="01";

    @Column(nullable = false, length = 20)
    private String cpf_cnpj;

    @Column(nullable = false, length = 2)
    private String tipo;

    @Column(nullable = false, length = 60)
    private String nome;

    @Column(nullable = true, length = 60)
    private String nome_fantasia;

    @Column(nullable = true)
    private LocalDate data_abertura_nascimento;

    @Column(nullable = true, length=60)
    private String homepage;

    @Column(nullable= false, length=20)
    private String email;

    @Column(nullable = true, length =30)
    private String nome_contato;
    
    @Column(nullable = false, length =15)
    private String contato;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="endereco_codigo", referencedColumnName = "codigo")
    private Endereco endereco;

    public Cliente(
        String cpf_cnpj,
        String tipo,
        String nome,
        String nome_fantasia,
        LocalDate data_abertura_nascimento,
        String homepage,
        String email,
        String nome_contato,
        String contato,
        Endereco endereco
) {
    this.cpf_cnpj = cpf_cnpj;
    this.tipo = tipo;
    this.nome = nome;
    this.nome_fantasia = nome_fantasia;
    this.data_abertura_nascimento = data_abertura_nascimento;
    this.homepage = homepage;
    this.email = email;
    this.nome_contato = nome_contato;
    this.contato = contato;
    this.endereco = endereco;
}

public Cliente (String cpf_cnpj, String tipo, String nome, String email, String contato, Endereco endereco) {
    this.cpf_cnpj = cpf_cnpj;
    this.tipo = tipo;
    this.nome = nome;
    this.email = email;
    this.contato = contato;
    this.endereco = endereco;
}

}
