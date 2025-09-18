package com.programacao.web.fatec.api_fatec.controller;

import org.springframework.web.bind.annotation.RestController;

import com.programacao.web.fatec.api_fatec.domain.cliente.ClienteService;
import com.programacao.web.fatec.api_fatec.domain.cliente.dto.BuscaPorIdOuNomeDto;
import com.programacao.web.fatec.api_fatec.domain.cliente.dto.ClientePostDto;
import com.programacao.web.fatec.api_fatec.domain.cliente.dto.ClientePutDto;
import com.programacao.web.fatec.api_fatec.domain.cliente.dto.ClienteResponseDto;

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
 * Retorna e recebe DTOs, mantendo a API desacoplada da entidade JPA.
 * 
 * - GET    /listarClientes          → lista todos os clientes
 * - GET    /buscaPorIdOuNome/{txt}  → busca por ID ou nome via path param
 * - POST   /buscaPorIdOuNome        → busca por ID ou nome via DTO
 * - GET    /buscarPorTexto?texto=X  → busca genérica por texto (id, nome, cidade)
 * - POST   /                        → cria um novo cliente
 * - PUT    /{id}                    → atualiza cliente existente
 * - DELETE /{id}                    → exclui cliente
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    /**
     * Lista todos os clientes cadastrados.
     * @return lista de clientes em formato DTO
     */
    @GetMapping("/listarClientes")
    public ResponseEntity<List<ClienteResponseDto>> listarClientes() {
        List<ClienteResponseDto> clientes = clienteService.listarClientes();
        return ResponseEntity.ok(clientes);
    }

    /**
     * Busca clientes por ID numérico ou parte do nome, informado no path.
     */
    @GetMapping("/buscaPorIdOuNome/{search}")
    public ResponseEntity<List<ClienteResponseDto>> buscaPorIdOuNomeGenerico(@PathVariable String search) {
        List<ClienteResponseDto> clientes = clienteService.buscaPorIdOuNomeGenerico(search);
        return ResponseEntity.ok(clientes);
    }

    /**
     * Busca clientes por ID ou nome, recebendo dados em um DTO.
     */
    @PostMapping("/buscaPorIdOuNome")
    public ResponseEntity<List<ClienteResponseDto>> buscaPorIdOuNome(@RequestBody BuscaPorIdOuNomeDto dto) {
        List<ClienteResponseDto> clientes = clienteService.buscaPorIdOuNome(dto);
        return ResponseEntity.ok(clientes);
    }

    /**
     * Busca clientes por texto (id, nome ou cidade).
     */
    @GetMapping("/buscarPorTexto")
    public ResponseEntity<List<ClienteResponseDto>> buscarPorTexto(@RequestParam String texto) {
        List<ClienteResponseDto> clientes = clienteService.buscarPorTexto(texto);
        return ResponseEntity.ok(clientes);
    }

    /**
     * Cria um novo cliente.
     * @param dto dados do cliente a cadastrar
     */
    @PostMapping(value = "", consumes = "application/json")
    public ResponseEntity<ClienteResponseDto> createCliente(@RequestBody ClientePostDto dto) {
        ClienteResponseDto novoCliente = clienteService.createCliente(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
    }

    /**
     * Exclui um cliente existente pelo ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarCliente(@PathVariable Long id) {
        String mensagem = clienteService.deletarCliente(id);
        return ResponseEntity.ok(mensagem);
    }

    /**
     * Atualiza um cliente existente.
     * @param id identificador do cliente (path)
     * @param dto dados novos
     */
    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<ClienteResponseDto> alterarCliente(@PathVariable Long id, @RequestBody ClientePutDto dto) {
        ClienteResponseDto clienteAtualizado = clienteService.alterarCliente(id, dto);
        return ResponseEntity.ok(clienteAtualizado);
    }
}
