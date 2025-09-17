package com.programacao.web.fatec.api_fatec.controller;

import org.springframework.web.bind.annotation.RestController;

import com.programacao.web.fatec.api_fatec.domain.cliente.ClienteService;
import com.programacao.web.fatec.api_fatec.domain.cliente.dto.BuscaPorIdOuNomeDto;
import com.programacao.web.fatec.api_fatec.domain.cliente.dto.ClientePostDto;
import com.programacao.web.fatec.api_fatec.domain.cliente.dto.ClientePutDto;
import com.programacao.web.fatec.api_fatec.entities.Cliente;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Controlador REST para operações relacionadas a clientes.
 * 
 * Este controlador implementa o padrão ResponseEntity do Spring para melhorar o controle
 * sobre as respostas HTTP, permitindo:
 * - Definir códigos de status HTTP apropriados (200 OK, 201 Created, 404 Not Found, etc.)
 * - Incluir cabeçalhos personalizados quando necessário
 * - Estruturar o corpo da resposta de forma consistente
 * 
 * As exceções são tratadas globalmente pelo GlobalExceptionHandler, que captura exceções
 * como ResourceNotFoundException e as converte em respostas HTTP adequadas.
 * 
 * Utiliza o ClienteService para executar a lógica de negócio.
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    /**
     * Lista todos os clientes.
     * 
     * @return ResponseEntity com a lista de clientes e status 200 (OK)
     */
    @GetMapping("/listarClientes")
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = clienteService.listarClientes();
        return ResponseEntity.ok(clientes);
    }

    /**
     * Busca clientes por ID ou nome usando um parâmetro de caminho.
     * 
     * @param search Texto para busca (ID ou nome)
     * @return ResponseEntity com a lista de clientes encontrados e status 200 (OK)
     */
    @GetMapping("/buscaPorIdOuNome/{search}")
    public ResponseEntity<List<Cliente>> buscaPorIdOuNomeGenerico(@PathVariable String search) {
        List<Cliente> clientes = clienteService.buscaPorIdOuNomeGenerico(search);
        return ResponseEntity.ok(clientes);
    }

    /**
     * Busca clientes por ID ou nome usando um DTO.
     * 
     * @param dto DTO contendo ID e nome para busca
     * @return ResponseEntity com a lista de clientes encontrados e status 200 (OK)
     */
    @PostMapping("/buscaPorIdOuNome")
    public ResponseEntity<List<Cliente>> buscaPorIdOuNome(@RequestBody BuscaPorIdOuNomeDto dto) {
        List<Cliente> clientes = clienteService.buscaPorIdOuNome(dto);
        return ResponseEntity.ok(clientes);
    }

    /**
     * Busca clientes por texto que corresponda ao ID, nome ou nome da cidade.
     * 
     * @param texto O texto a ser buscado em múltiplos campos
     * @return ResponseEntity com a lista de clientes encontrados e status 200 (OK)
     */
    @GetMapping("/buscarPorTexto")
    public ResponseEntity<List<Cliente>> buscarPorTexto(@RequestParam String texto) {
        List<Cliente> clientes = clienteService.buscarPorTexto(texto);
        return ResponseEntity.ok(clientes);
    }

    /**
     * Cria um novo cliente.
     * 
     * @param dto Dados do cliente a ser criado
     * @return ResponseEntity com o cliente criado e status 201 (Created)
     */
    @PostMapping(value = "", consumes = "application/json")
    public ResponseEntity<Cliente> createCliente(@RequestBody ClientePostDto dto) {
        Cliente novoCliente = clienteService.createCliente(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
    }

    /**
     * Deleta um cliente pelo ID.
     * 
     * @param id ID do cliente a ser deletado
     * @return ResponseEntity com mensagem de sucesso e status 200 (OK)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarCliente(@PathVariable Long id) {
        String mensagem = clienteService.deletarCliente(id);
        return ResponseEntity.ok(mensagem);
    }

    /**
     * Atualiza um cliente existente.
     * 
     * @param id ID do cliente a ser atualizado
     * @param dto Novos dados do cliente
     * @return ResponseEntity com o cliente atualizado e status 200 (OK)
     */
    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<Cliente> alterarCliente(@PathVariable Long id, @RequestBody ClientePutDto dto) {
        Cliente clienteAtualizado = clienteService.alterarCliente(id, dto);
        return ResponseEntity.ok(clienteAtualizado);
    }

}
