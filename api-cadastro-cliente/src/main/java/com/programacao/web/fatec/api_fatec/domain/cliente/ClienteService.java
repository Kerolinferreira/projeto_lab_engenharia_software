package com.programacao.web.fatec.api_fatec.domain.cliente;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.programacao.web.fatec.api_fatec.domain.cidade.CidadeRepository;
import com.programacao.web.fatec.api_fatec.domain.cliente.dto.BuscaPorIdOuNomeDto;
import com.programacao.web.fatec.api_fatec.domain.cliente.dto.ClientePostDto;
import com.programacao.web.fatec.api_fatec.domain.cliente.dto.ClientePutDto;
import com.programacao.web.fatec.api_fatec.entities.Cidade;
import com.programacao.web.fatec.api_fatec.entities.Cliente;
import com.programacao.web.fatec.api_fatec.exception.ResourceNotFoundException;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public List<Cliente> buscaPorIdOuNomeGenerico(String search) {
        Long id = null;
        try {
            id = Long.parseLong(search);
        } catch (Exception e) {
            // TODO: handle exception
            System.err.println(e.getMessage());
        }

        return clienteRepository.buscarPorIdOuNome(id, search);
    }

    public List<Cliente> buscaPorIdOuNome(BuscaPorIdOuNomeDto dto) {
        return clienteRepository.buscarPorIdOuNome(dto.getId(), dto.getNome());
    }

    public List<Cliente> buscarPorTexto(String texto) {
        // Tenta converter o texto para Long para buscar por ID
        Long idLong = null;
        try {
            idLong = Long.parseLong(texto);
        } catch (NumberFormatException e) {
            // Se não for possível converter para Long, deixa idLong como null
            System.out.println("Texto não pode ser convertido para Long: " + texto);
        }

        // Chama o método do repositório com o texto e o possível ID
        return clienteRepository.buscarPorIdOuNomeComCidade(texto, idLong);
    }

    public Cliente createCliente(ClientePostDto dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setEndereco(dto.getEndereco());

        // Verifica se o cliente tem uma cidade associada
        if (dto.getCidadeId() != null) {
            // Busca a cidade pelo ID
            Optional<Cidade> cidadeOpt = cidadeRepository.findById(dto.getCidadeId());

            // Se a cidade existir, associa ao cliente
            if (cidadeOpt.isPresent()) {
                cliente.setCidade(cidadeOpt.get());
            }
        }

        return clienteRepository.save(cliente);
    }

    public Cliente alterarCliente(Long id, ClientePutDto dto) {
        // Verifica se o cliente existe
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente", "id", id);
        }

        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNome(dto.getNome());
        cliente.setEndereco(dto.getEndereco());

        // Verifica se o cliente tem uma cidade associada
        if (dto.getCidadeId() != null) {
            // Busca a cidade pelo ID
            Optional<Cidade> cidadeOpt = cidadeRepository.findById(dto.getCidadeId());

            // Se a cidade existir, associa ao cliente
            cidadeOpt.ifPresent(cliente::setCidade);
        }

        return clienteRepository.save(cliente);
    }

    /**
     * Deleta um cliente pelo ID.
     * 
     * @param id ID do cliente a ser deletado
     * @return Mensagem de confirmação
     * @throws ResourceNotFoundException se o cliente não for encontrado
     */
    public String deletarCliente(Long id) {
        // Verifica se o cliente existe
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente", "id", id);
        }

        // Deleta o cliente
        clienteRepository.deleteById(id);
        return "Cliente Deletado";
    }
}
