package com.programacao.web.fatec.api_fatec.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.programacao.web.fatec.api_fatec.domain.produto.dto.*;
import com.programacao.web.fatec.api_fatec.entities.Produto;
import com.programacao.web.fatec.api_fatec.domain.produto.ProdutoService;
import com.programacao.web.fatec.api_fatec.domain.produto.ProdutoRepository;



/**
 * Controlador REST para operações relacionadas a  produtos.
 *
 * Retorna e recebe DTOs, mantendo a API desacoplada da entidade JPA.
 *
 * - GET    /listarProdutos          → lista todos os produtos
 * - GET    /buscaPorIdOuNome/{txt}  → busca por ID ou nome via path param
 * - POST   /buscaPorIdOuNome        → busca por ID ou nome via DTO
 * - GET    /buscarPorTexto?texto=X  → busca genérica por texto (id, nome_produto, descricao, modelo)
 * - POST   /                        → cria um novo produto
 * - PUT    /{id}                    → atualiza produto existente
 * - DELETE /{id}                    → exclui cliente
 */


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
    
    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ProdutoRepository produtoRepository; 


    /*** 
     *  Listar os produtos cadastrados
     * ***/

     @GetMapping("/listarProdutos")
     public ResponseEntity<List<ProdutoResponseDto>> listarProdutos() {
         List<ProdutoResponseDto> produtos = produtoService.listarProdutos();
         return ResponseEntity.ok(produtos);
     }
     


}
