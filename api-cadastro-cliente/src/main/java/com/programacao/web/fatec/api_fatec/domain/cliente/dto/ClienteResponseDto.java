package com.programacao.web.fatec.api_fatec.domain.cliente.dto;

import java.time.LocalDate;

import com.programacao.web.fatec.api_fatec.domain.endereco.dto.EnderecoDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de saída para retornar clientes na API.
 * Representa os dados expostos pela API sem expor diretamente a entidade JPA.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDto {
    private Long id;
    private String loja;
    private String cpf_cnpj;
    private String tipo;
    private String nome;
    private String nome_fantasia;
    private LocalDate data_abertura_nascimento;
    private String homepage;
    private String email;
    private String nome_contato;
    private String contato;
    private EnderecoDto endereco;
}
