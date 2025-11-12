package com.programacao.web.fatec.api_fatec.domain.cliente.dto;

import java.time.LocalDate;

import com.programacao.web.fatec.api_fatec.domain.endereco.dto.EnderecoDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para atualização de cliente (PUT).
 * Mantém os mesmos campos obrigatórios do POST.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientePutDto {

    private String cpf_cnpj;


    private String tipo;

    private String nome;

    private String nome_fantasia;

    private LocalDate data_abertura_nascimento;

    private String homepage;

    private String email;

    private String nome_contato;

    private String contato;

    private String loja;

    private EnderecoDto endereco; // obrigatório
}
