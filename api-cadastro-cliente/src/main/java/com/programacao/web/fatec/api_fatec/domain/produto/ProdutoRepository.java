package com.programacao.web.fatec.api_fatec.domain.produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import com.programacao.web.fatec.api_fatec.entities.Produto;

public interface ProdutoRepository extends JpaRepository <Produto, Long> {

    @Query(value = """
        SELECT p FROM Produto p
        WHERE (:id IS NOT NULL AND id = :id)
           OR (LOWER(nome_produto) LIKE LOWER(CONCAT('%', :nome_produto, '%')))
    """, nativeQuery = true)
    List<Produto> buscarPorIdNome(@Param("id") Long id, @Param("nome_produto") String nome);

        /**
     * Busca produtos por texto (id, nome, descrição ou modelo).
     */

    @Query("""
    SELECT p FROM Produto p
    WHERE STR(p.id) LIKE %:texto%
       OR LOWER(p.nome_produto) LIKE LOWER(CONCAT('%', :texto, '%'))
       OR LOWER(p.descricao) LIKE LOWER(CONCAT('%', :texto, '%'))
       OR LOWER(p.modelo) LIKE LOWER(CONCAT('%', :texto, '%'))
""")
List<Produto> buscarPorTexto(@Param("texto") String texto);

    Produto findTopByOrderByIdDesc();
}
