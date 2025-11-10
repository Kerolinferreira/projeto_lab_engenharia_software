package com.programacao.web.fatec.api_fatec.domain.pedido;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programacao.web.fatec.api_fatec.domain.cliente.ClienteRepository;
import com.programacao.web.fatec.api_fatec.domain.pedido.dto.ItemPedidoDto;
import com.programacao.web.fatec.api_fatec.domain.pedido.dto.PedidoPostDto;
import com.programacao.web.fatec.api_fatec.domain.pedido.dto.PedidoPutDto;
import com.programacao.web.fatec.api_fatec.domain.pedido.dto.PedidoResponseDto;
import com.programacao.web.fatec.api_fatec.domain.produto.ProdutoRepository;
import com.programacao.web.fatec.api_fatec.entities.Cliente;
import com.programacao.web.fatec.api_fatec.entities.ItemPedido;
import com.programacao.web.fatec.api_fatec.entities.Pedido;
import com.programacao.web.fatec.api_fatec.entities.Produto;
import com.programacao.web.fatec.api_fatec.exception.ResourceNotFoundException;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    /* ===========================
       Conversores DTO ↔ Entity
       =========================== */

    private PedidoResponseDto toResponseDto(Pedido pedido) {
        if (pedido == null) return null;

        List<ItemPedidoDto> itensDto = pedido.getItens().stream()
            .map(this::toItemPedidoDto)
            .collect(Collectors.toList());

        return new PedidoResponseDto(
            pedido.getId(),
            pedido.getDataPedido(),
            pedido.getCliente().getId(),
            pedido.getCliente().getNome(),
            pedido.getCliente().getCpf_cnpj(),
            itensDto,
            pedido.getDesconto(),
            pedido.getFrete(),
            pedido.getTotal(),
            pedido.getFormaPagamento(),
            pedido.getCondicaoPagamento(),
            pedido.getPrevisaoEntrega(),
            pedido.getObservacoes()
        );
    }

    private ItemPedidoDto toItemPedidoDto(ItemPedido item) {
        return new ItemPedidoDto(
            item.getProduto().getId(),
            item.getProduto().getNome_produto(),
            item.getQuantidade(),
            item.getPrecoUnitario(),
            item.getSubtotal()
        );
    }

    /* ============================
     *  Métodos de negócio
     ==============================*/

    /**
     * Lista todos os pedidos.
     */
    public List<PedidoResponseDto> listarPedidos() {
        return pedidoRepository.findAll()
            .stream()
            .map(this::toResponseDto)
            .collect(Collectors.toList());
    }

    /**
     * Busca um pedido por ID.
     */
    public PedidoResponseDto buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", id));
        return toResponseDto(pedido);
    }

    /**
     * Busca pedidos por ID do cliente.
     */
    public List<PedidoResponseDto> buscarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId)
            .stream()
            .map(this::toResponseDto)
            .collect(Collectors.toList());
    }

    /**
     * Busca pedidos por texto (ID, nome do cliente, CNPJ).
     */
    public List<PedidoResponseDto> buscarPorTexto(String texto) {
        return pedidoRepository.buscarPorTexto(texto)
            .stream()
            .map(this::toResponseDto)
            .collect(Collectors.toList());
    }

    /**
     * Retorna o ID do último pedido cadastrado.
     */
    public Long obterUltimoId() {
        Pedido ultimoPedido = pedidoRepository.findTopByOrderByIdDesc();
        return ultimoPedido != null ? ultimoPedido.getId() : 0L;
    }

    /**
     * Cria um novo pedido.
     */
    @Transactional
    public PedidoResponseDto criarPedido(PedidoPostDto dto) {
        // Busca o cliente
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
            .orElseThrow(() -> new ResourceNotFoundException("Cliente", "id", dto.getClienteId()));

        // Cria o pedido
        Pedido pedido = new Pedido(
            dto.getDataPedido(),
            cliente,
            dto.getDesconto(),
            dto.getFrete(),
            dto.getFormaPagamento(),
            dto.getCondicaoPagamento(),
            dto.getPrevisaoEntrega(),
            dto.getObservacoes()
        );

        // Adiciona os itens
        if (dto.getItens() != null && !dto.getItens().isEmpty()) {
            for (ItemPedidoDto itemDto : dto.getItens()) {
                Produto produto = produtoRepository.findById(itemDto.getProdutoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto", "id", itemDto.getProdutoId()));

                ItemPedido item = new ItemPedido(
                    pedido,
                    produto,
                    itemDto.getQuantidade(),
                    itemDto.getPrecoUnitario()
                );
                pedido.adicionarItem(item);
            }
        }

        // Calcula o total
        pedido.calcularTotal();

        // Salva o pedido (cascade salva os itens automaticamente)
        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        return toResponseDto(pedidoSalvo);
    }

    /**
     * Atualiza um pedido existente.
     */
    @Transactional
    public PedidoResponseDto atualizarPedido(Long id, PedidoPutDto dto) {
        // Busca o pedido existente
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", id));

        // Atualiza o cliente se necessário
        if (dto.getClienteId() != null && !dto.getClienteId().equals(pedido.getCliente().getId())) {
            Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", "id", dto.getClienteId()));
            pedido.setCliente(cliente);
        }

        // Atualiza os dados do pedido
        pedido.setDataPedido(dto.getDataPedido());
        pedido.setDesconto(dto.getDesconto());
        pedido.setFrete(dto.getFrete());
        pedido.setFormaPagamento(dto.getFormaPagamento());
        pedido.setCondicaoPagamento(dto.getCondicaoPagamento());
        pedido.setPrevisaoEntrega(dto.getPrevisaoEntrega());
        pedido.setObservacoes(dto.getObservacoes());

        // Remove todos os itens antigos
        pedido.getItens().clear();

        // Adiciona os novos itens
        if (dto.getItens() != null && !dto.getItens().isEmpty()) {
            for (ItemPedidoDto itemDto : dto.getItens()) {
                Produto produto = produtoRepository.findById(itemDto.getProdutoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto", "id", itemDto.getProdutoId()));

                ItemPedido item = new ItemPedido(
                    pedido,
                    produto,
                    itemDto.getQuantidade(),
                    itemDto.getPrecoUnitario()
                );
                pedido.adicionarItem(item);
            }
        }

        // Recalcula o total
        pedido.calcularTotal();

        // Salva as alterações
        Pedido pedidoAtualizado = pedidoRepository.save(pedido);

        return toResponseDto(pedidoAtualizado);
    }

    /**
     * Deleta um pedido.
     */
    @Transactional
    public String deletarPedido(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pedido", "id", id);
        }
        pedidoRepository.deleteById(id);
        return "Pedido deletado com sucesso";
    }
}
