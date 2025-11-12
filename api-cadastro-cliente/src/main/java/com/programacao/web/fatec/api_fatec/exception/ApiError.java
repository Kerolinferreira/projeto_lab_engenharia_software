package com.programacao.web.fatec.api_fatec.exception;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe que representa uma resposta de erro padronizada para a API.
 * Esta classe é usada para fornecer uma estrutura consistente para todas as respostas de erro,
 * incluindo timestamp, código de status HTTP, mensagem de erro e detalhes adicionais.
 */
@Data
@NoArgsConstructor
public class ApiError {

    private LocalDateTime timestamp = LocalDateTime.now();
    private int status;
    private String error;
    private String message;
    private String path;

    /**
     * Construtor com todos os campos.
     * 
     * @param status Código de status HTTP
     * @param error Tipo de erro (ex: "Not Found", "Bad Request")
     * @param message Mensagem detalhada do erro
     * @param path Caminho da requisição que gerou o erro
     */
    public ApiError(int status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}
