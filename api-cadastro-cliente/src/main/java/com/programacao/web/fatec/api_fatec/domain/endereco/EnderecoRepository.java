package com.programacao.web.fatec.api_fatec.domain.endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.programacao.web.fatec.api_fatec.entities.Cliente;
import com.programacao.web.fatec.api_fatec.entities.Endereco;
import java.util.List;


public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    
}
