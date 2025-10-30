package com.programacao.web.fatec.api_fatec.domain.produto;
import org.springframework.data.jpa.repository.JpaRepository;

import com.programacao.web.fatec.api_fatec.entities.Produto;

public interface ProdutoRepository extends JpaRepository <Produto, Long>{

}