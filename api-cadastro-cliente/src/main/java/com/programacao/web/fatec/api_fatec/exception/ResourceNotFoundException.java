package com.programacao.web.fatec.api_fatec.exception;

/**
 * Exceção lançada quando um recurso solicitado não é encontrado.
 * Esta exceção é usada para indicar que um recurso (como um Cliente ou Cidade)
 * não foi encontrado no banco de dados quando tentamos acessá-lo por ID ou outro identificador.
 */
public class ResourceNotFoundException extends RuntimeException {
    
    /**
     * Construtor que recebe uma mensagem detalhada sobre o recurso não encontrado.
     * 
     * @param message Mensagem descritiva do erro
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Construtor que cria uma mensagem formatada indicando qual recurso não foi encontrado.
     * 
     * @param resourceName Nome do recurso (ex: "Cliente", "Cidade")
     * @param fieldName Nome do campo usado para busca (ex: "id", "nome")
     * @param fieldValue Valor do campo que não foi encontrado
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s não encontrado com %s: '%s'", resourceName, fieldName, fieldValue));
    }
}