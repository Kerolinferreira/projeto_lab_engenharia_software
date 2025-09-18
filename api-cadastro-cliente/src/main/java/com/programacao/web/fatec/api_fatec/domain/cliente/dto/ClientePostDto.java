package com.programacao.web.fatec.api_fatec.domain.cliente.dto;

import java.time.LocalDate;

import com.programacao.web.fatec.api_fatec.domain.endereco.dto.EnderecoDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    private String cpf_cnpj;

    @NotBlank
    private String tipo;

    @NotBlank
    private String nome;

    private String nome_fantasia;

    private LocalDate data_abertura_nascimento;

    private String homepage;

    @NotBlank
    @Email
    private String email;

    private String nome_contato;

    @NotBlank
    private String contato;

    @NotNull
    private EnderecoDto endereco; // obrigatório
}
