package com.programacao.web.fatec.api_fatec.domain.cliente;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.programacao.web.fatec.api_fatec.domain.cliente.dto.BuscaPorIdOuNomeDto;
import com.programacao.web.fatec.api_fatec.domain.cliente.dto.ClientePostDto;
import com.programacao.web.fatec.api_fatec.domain.cliente.dto.ClientePutDto;
import com.programacao.web.fatec.api_fatec.domain.cliente.dto.ClienteResponseDto;
import com.programacao.web.fatec.api_fatec.domain.endereco.dto.EnderecoDto;
import com.programacao.web.fatec.api_fatec.entities.Cliente;
import com.programacao.web.fatec.api_fatec.entities.Endereco;
import com.programacao.web.fatec.api_fatec.exception.ResourceNotFoundException;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    /* ===========================
       Conversores DTO ↔ Entity
       =========================== */

    private Endereco toEntity(EnderecoDto dto) {
        if (dto == null) return null;
        Endereco endereco = new Endereco();
        endereco.setCep(dto.getCep());
        endereco.setLogradouro(dto.getLogradouro());
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setBairro(dto.getBairro());
        endereco.setCidade(dto.getCidade());
        endereco.setUf(dto.getUf());
         endereco.setPais("Brasil"); 
        return endereco;
    }

    private EnderecoDto toDto(Endereco endereco) {
        if (endereco == null) return null;
        return new EnderecoDto(
            endereco.getCep(),
            endereco.getLogradouro(),
            endereco.getNumero(),
            endereco.getComplemento(),
            endereco.getBairro(),
            endereco.getCidade(),
            endereco.getUf(),
            endereco.getPais()
        );
    }

    private ClienteResponseDto toResponseDto(Cliente cliente) {
        if (cliente == null) return null;
        return new ClienteResponseDto(
            cliente.getId(),
            cliente.getLoja(),
            cliente.getCpf_cnpj(),
            cliente.getTipo(),
            cliente.getNome(),
            cliente.getNome_fantasia(),
            cliente.getData_abertura_nascimento(),
            cliente.getHomepage(),
            cliente.getEmail(),
            cliente.getNome_contato(),
            cliente.getContato(),
            toDto(cliente.getEndereco())
        );
    }

    /* ===========================
       Métodos de negócio
       =========================== */

    public List<ClienteResponseDto> listarClientes() {
        return clienteRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ClienteResponseDto> buscaPorIdOuNomeGenerico(String search) {
        Long id = null;
        try {
            id = Long.parseLong(search);
        } catch (NumberFormatException ignored) {}
        return clienteRepository.buscarPorIdOuNome(id, search)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ClienteResponseDto> buscaPorIdOuNome(BuscaPorIdOuNomeDto dto) {
        return clienteRepository.buscarPorIdOuNome(dto.getId(), dto.getNome())
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public ClienteResponseDto createCliente(ClientePostDto dto) {
        if (dto.getEndereco() == null) {
            throw new IllegalArgumentException("Endereço é obrigatório");
        }

        Cliente cliente = new Cliente();
        cliente.setCpf_cnpj(dto.getCpf_cnpj());
        cliente.setTipo(dto.getTipo());
        cliente.setNome(dto.getNome());
        cliente.setNome_fantasia(dto.getNome_fantasia());
        cliente.setData_abertura_nascimento(dto.getData_abertura_nascimento());
        cliente.setHomepage(dto.getHomepage());
        cliente.setEmail(dto.getEmail());
        cliente.setNome_contato(dto.getNome_contato());
        cliente.setContato(dto.getContato());
        cliente.setEndereco(toEntity(dto.getEndereco()));

        return toResponseDto(clienteRepository.save(cliente));
    }

    public ClienteResponseDto alterarCliente(Long id, ClientePutDto dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", "id", id));

        if (dto.getEndereco() == null) {
            throw new IllegalArgumentException("Endereço é obrigatório");
        }

        cliente.setCpf_cnpj(dto.getCpf_cnpj());
        cliente.setTipo(dto.getTipo());
        cliente.setNome(dto.getNome());
        cliente.setNome_fantasia(dto.getNome_fantasia());
        cliente.setData_abertura_nascimento(dto.getData_abertura_nascimento());
        cliente.setHomepage(dto.getHomepage());
        cliente.setEmail(dto.getEmail());
        cliente.setNome_contato(dto.getNome_contato());
        cliente.setContato(dto.getContato());
        cliente.setEndereco(toEntity(dto.getEndereco()));

        return toResponseDto(clienteRepository.save(cliente));
    }

    public String deletarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente", "id", id);
        }
        clienteRepository.deleteById(id);
        return "Cliente Deletado";
    }

    public List<ClienteResponseDto> buscarPorTexto(String texto) {
    return clienteRepository.buscarPorTexto(texto)
            .stream()
            .map(this::toResponseDto)
            .collect(Collectors.toList());
}
}
