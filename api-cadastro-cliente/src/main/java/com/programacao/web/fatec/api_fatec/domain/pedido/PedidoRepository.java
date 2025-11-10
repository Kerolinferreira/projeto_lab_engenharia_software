package com.programacao.web.fatec.api_fatec.domain.pedido;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.programacao.web.fatec.api_fatec.entities.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    /**
     * Busca pedidos por ID do cliente.
     */
    List<Pedido> findByClienteId(Long clienteId);

    /**
     * Busca pedidos por data.
     */
    List<Pedido> findByDataPedido(LocalDate dataPedido);

    /**
     * Busca pedidos entre duas datas.
     */
    List<Pedido> findByDataPedidoBetween(LocalDate dataInicio, LocalDate dataFim);

    /**
     * Busca pedidos por texto (ID, nome do cliente, CNPJ).
     */
    @Query("""
        SELECT p FROM Pedido p
        WHERE STR(p.id) LIKE CONCAT('%', :texto, '%')
           OR LOWER(p.cliente.nome) LIKE LOWER(CONCAT('%', :texto, '%'))
           OR LOWER(p.cliente.cpf_cnpj) LIKE LOWER(CONCAT('%', :texto, '%'))
    """)
    List<Pedido> buscarPorTexto(@Param("texto") String texto);

    /**
     * Retorna o último pedido cadastrado (maior ID).
     */
    Pedido findTopByOrderByIdDesc();
}
