package com.programacao.web.fatec.api_fatec.controller;
import java.util.List;
import java.util.Map;

import org.apache.catalina.connector.Response;
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
import com.google.genai.Client;
import com.google.genai.Models;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private org.springframework.core.env.Environment env;
    

    /*** 
     *  Listar os produtos cadastrados
     * ***/

     @GetMapping("/listarProdutos")
     public ResponseEntity<List<ProdutoResponseDto>> listarProdutos() {
         List<ProdutoResponseDto> produtos = produtoService.listarProdutos();
         return ResponseEntity.ok(produtos);
     }

     @GetMapping("/buscaidounome/{search}")
     public ResponseEntity<List<ProdutoResponseDto>> buscaPorIdouNomeGenerico(@PathVariable String search){
        List<ProdutoResponseDto> produtos = produtoService.buscaPorIdOuNomeProdutoGenerico(search);
        return ResponseEntity.ok(produtos);
     }

     @PostMapping("buscaporidounome")
     public ResponseEntity<List<ProdutoResponseDto>> buscarPorIdouNome(@RequestBody BuscaIdNomeDto dto){
        List<ProdutoResponseDto> produtos = produtoService.buscaPorIdOuNome(dto);
        return ResponseEntity.ok(produtos);
     }

     @GetMapping("buscarportexto/")
     public ResponseEntity<List<ProdutoResponseDto>> buscarporTexto(@RequestParam String texto){
        List<ProdutoResponseDto> produtos = produtoService.buscarPorTexto(texto);
        return ResponseEntity.ok(produtos);
     }

     @PostMapping(value ="", consumes="application/json")
     public ResponseEntity<ProdutoResponseDto> createProduto(@RequestBody ProdutoPostDto dto){
        ProdutoResponseDto novoProduto = produtoService.createProduto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
     }
     
     @DeleteMapping("/{id}")
     public ResponseEntity<String> deletarProduto(@PathVariable Long id){
        String mensagem = produtoService.deletarProduto(id);
        return ResponseEntity.ok(mensagem);
     }


     @PutMapping(value ="/{id}", consumes = "application/json")
     public ResponseEntity<ProdutoResponseDto> alterarProduto(@PathVariable Long id, @RequestBody ProdutoPutDto dto){
        ProdutoResponseDto produtoAtualizado = produtoService.alterarProduto(id, dto);
        return ResponseEntity.ok(produtoAtualizado);
     }
     
     @PostMapping("/gerarDescricao")
     public Map<String, String> gerarDescricao(@RequestBody Map<String, Object> produto) {
         try {
             String nome = (String) produto.getOrDefault("nome_produto", "produto");
             String modelo = (String) produto.getOrDefault("modelo", "");
             String tamanho = (String) produto.getOrDefault("tamanho", "");
             String cor = (String) produto.getOrDefault("cor", "");
     
             String googleApiKey = env.getProperty("google.api.key");
             if (googleApiKey == null || googleApiKey.isEmpty()) {
                 return Map.of("erro", "Chave da API Google não configurada (google.api.key)");
             }
     
             String prompt = String.format("""
                 Create a short, catchy commercial description in English (up to 100 characters)
                 for a product named '%s'. 
                 It is a %s, size: %s, color: %s. 
                 Make it sound appealing and elegant (pt-br).
                 """, nome, modelo, tamanho, cor);

                 try (Client client = Client.builder().apiKey(googleApiKey).build()) {
                    String modelId = "gemini-2.5-flash";  
                
                    GenerateContentResponse response = client.models
                            .generateContent(modelId, prompt, null);
                
                    return Map.of("descricaoGerada", response.text());
                }
                
                
     
         } catch (Exception e) {
             e.printStackTrace();
             return Map.of("erro", "Erro ao gerar descrição: " + e.getMessage());
         }
     }
     
}


