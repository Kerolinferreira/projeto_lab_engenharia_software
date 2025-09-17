package com.programacao.web.fatec.api_fatec.domain.cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.programacao.web.fatec.api_fatec.entities.Cliente;
import com.programacao.web.fatec.api_fatec.entities.Endereco;
import java.util.List;

/**
 */
public interface ClienteRepository extends JpaRepository<Cliente, Long>{

}


