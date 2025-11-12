package com.programacao.web.fatec.api_fatec.domain.cliente.dto;

import java.time.LocalDate;

import com.programacao.web.fatec.api_fatec.domain.endereco.dto.EnderecoDto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para criação de cliente (POST).
 * Todos os campos obrigatórios estão anotados com validações.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientePostDto {

    private String cpf_cnpj;


    private String tipo;

    private String nome;

    private String nome_fantasia;

    private LocalDate data_abertura_nascimento;

    private String homepage;

    private String email;

    private String nome_contato;

    private String contato;

    private EnderecoDto endereco; // obrigatório

    private String loja;
}
