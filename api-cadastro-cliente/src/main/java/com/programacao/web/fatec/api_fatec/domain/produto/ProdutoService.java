package com.programacao.web.fatec.api_fatec.domain.produto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import com.programacao.web.fatec.api_fatec.domain.produto.dto.*;
import com.programacao.web.fatec.api_fatec.entities.Produto;
import com.programacao.web.fatec.api_fatec.exception.ResourceNotFoundException;



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

    public List<ProdutoResponseDto> listarProdutos(){
        return produtoRepository.findAll()
        .stream()
        .map(this::toResponseDto)
        .collect(Collectors.toList());
    }

    public List <ProdutoResponseDto> buscaPorIdOuNomeProdutoGenerico(String search){
        Long id = null;

        try{
            id = Long.parseLong(search);
        } catch (NumberFormatException ignored){}
        return produtoRepository.buscarPorIdNome(id, search)
        .stream()
        .map(this::toResponseDto)
        .collect(Collectors.toList());
    }

    public List <ProdutoResponseDto> buscaPorIdOuNome(BuscaIdNomeDto dto){
        return produtoRepository.buscarPorIdNome(dto.getId(), dto.getNome_produto())
            .stream()
            .map(this::toResponseDto)
            .collect(Collectors.toList());
    }

    public ProdutoResponseDto createProduto(ProdutoPostDto dto){
        Produto produto = new Produto();
        produto.setDescricao(dto.getDescricao());
        produto.setModelo(dto.getModelo());
        produto.setNome_produto(dto.getNome_produto());
        produto.setPreco(dto.getPreco());
        produto.setQtd_estoque(dto.getQtd_estoque());

        return toResponseDto(produtoRepository.save(produto));
    }

    public ProdutoResponseDto alterarProduto(Long id, ProdutoPutDto dto){

        Produto produto = produtoRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Produto","id",id));
        produto.setDescricao(dto.getDescricao());
        produto.setModelo(dto.getModelo());
        produto.setNome_produto(dto.getNome_produto());
        produto.setPreco(dto.getPreco());
        produto.setQtd_estoque(dto.getQtd_estoque());

        return toResponseDto(produtoRepository.save(produto));
    }

    
    public String deletarProduto(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto", "id", id);
        }
        produtoRepository.deleteById(id);
        return "Produto Deletado";
    }

    public List<ProdutoResponseDto> buscarPorTexto(String texto){
        return produtoRepository.buscarPorTexto(texto)
        .stream()
        .map(this::toResponseDto)
        .collect(Collectors.toList());
    }









       
}


