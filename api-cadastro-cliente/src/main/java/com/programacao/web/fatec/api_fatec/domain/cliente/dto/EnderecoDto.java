package com.programacao.web.fatec.api_fatec.domain.cliente.dto;


import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    private String logradouro;

    @NotBlank
    private String numero;

    private String complemento;

    private String bairro;

    @NotBlank
    private String cidade;

    @NotBlank
    private String uf;

    private String pais = "Brasil";
}
