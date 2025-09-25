package com.programacao.web.fatec.api_fatec.domain.endereco.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para representar endereço dentro de Cliente.
 * Os campos mínimos obrigatórios são validados.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoDto {

    private String cep;


    private String logradouro;


    private String numero;

    private String complemento;

    private String bairro;


    private String cidade;


    private String uf;
    private String pais = "Brasil";

}
