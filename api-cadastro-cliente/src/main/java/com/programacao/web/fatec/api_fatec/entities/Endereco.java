
package com.programacao.web.fatec.api_fatec.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade que representa um endereço no sistema.
 * Está associado de forma 1:1 com Cliente.
 */
@Entity
@Table(name="endereco")
@Getter
@Setter
@NoArgsConstructor
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @OneToOne(mappedBy = "endereco")
    private Cliente cliente;

    @Column(nullable = true, length = 15)
    private String cep;

    @Column(nullable = false, length = 150)
    private String logradouro;

    @Column(nullable = false, length = 5)
    private String numero;

    @Column(nullable = true)
    private String complemento;

    @Column(nullable = true, length = 150)
    private String bairro;

    @Column(nullable = false, length = 50)
    private String cidade;

    @Column(nullable = false, length = 2)
    private String uf;

    @Column(nullable = false)
    private String pais = "Brasil";

    public Endereco(String logradouro, String numero, String cidade, String uf) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.cidade = cidade;
        this.uf = uf;
    }

    public Endereco(String cep, String logradouro, String numero, String complemento, String bairro, String cidade, String uf) {
        this.cep = cep;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
    }
}