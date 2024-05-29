package com.example.application.Repository;

import com.example.application.Model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa,Integer> {

    boolean existsByNome(String nome);
    boolean existsByNomeAndIdNot(String nome, Long id);

    @Query(value = "SELECT p.id, p.nome, p.sexo, " +
            "p.data_nascimento, p.voluntario, " +
            "e.dados_endereco ->> 'rua' AS rua, " +
            "e.dados_endereco ->> 'numero' AS numero, " +
            "e.dados_endereco ->> 'bairro' AS bairro, " +
            "e.dados_endereco ->> 'complemento' AS complemento " +
            "FROM pessoas p " +
            "LEFT JOIN enderecos e ON p.id = e.pessoa_id", nativeQuery = true)
    List<Object[]> findPessoasAndEnderecos();

}
