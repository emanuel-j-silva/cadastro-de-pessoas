package com.example.application.Repository;

import com.example.application.Model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco,Integer> {

    @Query(value = "SELECT * FROM enderecos WHERE pessoa_id = :pessoaId",nativeQuery = true)
    Optional<Endereco> findByPessoaId(@Param("pessoaId") int pessoaId);
}
