package com.programacao.web.fatec.api_fatec.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Manipulador global de exceções para a API.
 * 
 * Esta classe usa a anotação @RestControllerAdvice para interceptar exceções lançadas
 * por qualquer controlador na aplicação e fornecer respostas de erro padronizadas.
 * 
 * O @RestControllerAdvice combina as funcionalidades de @ControllerAdvice e @ResponseBody,
 * permitindo que os métodos retornem objetos que serão automaticamente convertidos para JSON.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Manipula exceções do tipo ResourceNotFoundException.
     * 
     * Este método é chamado quando um recurso não é encontrado (por exemplo, um cliente com ID inexistente).
     * Retorna uma resposta com status 404 (Not Found) e um corpo contendo detalhes do erro.
     * 
     * @param ex A exceção ResourceNotFoundException que foi lançada
     * @param request A requisição web que gerou a exceção
     * @return ResponseEntity contendo o objeto ApiError com detalhes do erro
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {

        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                "Recurso não encontrado",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    /**
     * Manipula exceções de validação de argumentos de método.
     * 
     * Este método é chamado quando a validação de um objeto de requisição falha
     * (por exemplo, quando um campo obrigatório está ausente ou um valor está fora do intervalo esperado).
     * 
     * @param ex A exceção de validação
     * @param request A requisição web que gerou a exceção
     * @return ResponseEntity contendo detalhes dos erros de validação
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, WebRequest request) {

        List<String> errors = new ArrayList<>();

        // Coleta todos os erros de validação
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação",
                "Ocorreram erros de validação: " + String.join(", ", errors),
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Manipula exceções de mensagem HTTP não legível.
     * 
     * Este método é chamado quando o corpo da requisição não pode ser lido ou convertido para o tipo esperado
     * (por exemplo, quando o JSON está mal formatado).
     * 
     * @param ex A exceção de mensagem não legível
     * @param request A requisição web que gerou a exceção
     * @return ResponseEntity contendo detalhes do erro
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, WebRequest request) {

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Formato de requisição inválido",
                "O corpo da requisição está mal formatado ou contém valores inválidos: " + ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Manipula exceções genéricas não tratadas por outros handlers.
     * 
     * Este método captura qualquer exceção não tratada especificamente e fornece
     * uma resposta de erro padronizada com status 500 (Internal Server Error).
     * 
     * @param ex A exceção que foi lançada
     * @param request A requisição web que gerou a exceção
     * @return ResponseEntity contendo o objeto ApiError com detalhes do erro
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGlobalException(
            Exception ex, WebRequest request) {

        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno do servidor",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
