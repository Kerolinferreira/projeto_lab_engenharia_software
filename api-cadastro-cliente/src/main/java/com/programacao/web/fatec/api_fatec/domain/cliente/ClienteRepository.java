package com.programacao.web.fatec.api_fatec.domain.cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.programacao.web.fatec.api_fatec.entities.Cliente;
import java.util.List;

/**
 * Repositório para a entidade Cliente com consultas customizadas.
 */
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * Busca clientes por ID ou nome.
     * Se o ID for nulo, busca apenas pelo nome.
     * Se o nome for nulo, busca apenas pelo ID.
     */
    @Query("SELECT c FROM Cliente c " +
           "WHERE (:id IS NULL OR c.id = :id) " +
           "AND (:nome IS NULL OR LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%')))")
    List<Cliente> buscarPorIdOuNome(@Param("id") Long id, @Param("nome") String nome);

}
