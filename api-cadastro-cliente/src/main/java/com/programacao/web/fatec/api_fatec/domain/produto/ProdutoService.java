package com.programacao.web.fatec.api_fatec.domain.produto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.programacao.web.fatec.api_fatec.domain.produto.dto.*;
import com.programacao.web.fatec.api_fatec.entities.Produto;


@Service
public class ProdutoService {
    @Autowired
    private ProdutoRepository produtoRepository;

        /* ===========================
       Conversores DTO ↔ Entity
       =========================== */

       private ProdutoResponseDto toResponseDto(Produto produto) {
        if (produto == null) return null;
        return new ProdutoResponseDto(
            produto.getId(),
            produto.getNome_produto(),
            produto.getDescricao(),
            produto.getQtd_estoque(),
            produto.getPreco(),
            produto.getModelo()
        );
    }

    /* ============================
     *  Métodos de negócio 
     ==============================*/



       
}


