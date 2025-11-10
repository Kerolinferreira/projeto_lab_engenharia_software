package com.programacao.web.fatec.api_fatec.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.programacao.web.fatec.api_fatec.domain.pedido.PedidoService;
import com.programacao.web.fatec.api_fatec.domain.pedido.dto.PedidoPostDto;
import com.programacao.web.fatec.api_fatec.domain.pedido.dto.PedidoPutDto;
import com.programacao.web.fatec.api_fatec.domain.pedido.dto.PedidoResponseDto;

/**
 * Controlador REST para operações relacionadas a pedidos.
 *
 * Endpoints disponíveis:
 * - GET    /api/pedidos                    → lista todos os pedidos
 * - GET    /api/pedidos/{id}               → busca pedido por ID
 * - GET    /api/pedidos/cliente/{id}       → lista pedidos de um cliente
 * - GET    /api/pedidos/buscarPorTexto     → busca pedidos por texto
 * - GET    /api/pedidos/ultimoId           → retorna o último ID cadastrado
 * - POST   /api/pedidos                    → cria um novo pedido
 * - PUT    /api/pedidos/{id}               → atualiza um pedido existente
 * - DELETE /api/pedidos/{id}               → deleta um pedido
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    /**
     * Lista todos os pedidos.
     */
    @GetMapping
    public ResponseEntity<List<PedidoResponseDto>> listarPedidos() {
        List<PedidoResponseDto> pedidos = pedidoService.listarPedidos();
        return ResponseEntity.ok(pedidos);
    }

    /**
     * Busca um pedido por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDto> buscarPorId(@PathVariable Long id) {
        PedidoResponseDto pedido = pedidoService.buscarPorId(id);
        return ResponseEntity.ok(pedido);
    }

    /**
     * Lista pedidos de um cliente específico.
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PedidoResponseDto>> buscarPorCliente(@PathVariable Long clienteId) {
        List<PedidoResponseDto> pedidos = pedidoService.buscarPorCliente(clienteId);
        return ResponseEntity.ok(pedidos);
    }

    /**
     * Busca pedidos por texto (ID, nome do cliente, CNPJ).
     */
    @GetMapping("/buscarPorTexto")
    public ResponseEntity<List<PedidoResponseDto>> buscarPorTexto(@RequestParam String texto) {
        List<PedidoResponseDto> pedidos = pedidoService.buscarPorTexto(texto);
        return ResponseEntity.ok(pedidos);
    }

    /**
     * Retorna o ID do último pedido cadastrado.
     */
    @GetMapping("/ultimoId")
    public ResponseEntity<Long> obterUltimoId() {
        Long ultimoId = pedidoService.obterUltimoId();
        return ResponseEntity.ok(ultimoId);
    }

    /**
     * Cria um novo pedido.
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<PedidoResponseDto> criarPedido(@RequestBody PedidoPostDto dto) {
        PedidoResponseDto novoPedido = pedidoService.criarPedido(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);
    }

    /**
     * Atualiza um pedido existente.
     */
    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<PedidoResponseDto> atualizarPedido(
            @PathVariable Long id,
            @RequestBody PedidoPutDto dto) {
        PedidoResponseDto pedidoAtualizado = pedidoService.atualizarPedido(id, dto);
        return ResponseEntity.ok(pedidoAtualizado);
    }

    /**
     * Deleta um pedido.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarPedido(@PathVariable Long id) {
        String mensagem = pedidoService.deletarPedido(id);
        return ResponseEntity.ok(mensagem);
    }
}
