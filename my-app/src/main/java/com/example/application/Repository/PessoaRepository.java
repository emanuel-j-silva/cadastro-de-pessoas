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

    @Query(value = "SELECT p.id, p.nome, p.sexo, p.data_nascimento, p.voluntario, " +
            "endereco_principal.value ->> 'rua' AS rua, " +
            "endereco_principal.value ->> 'numero' AS numero, " +
            "endereco_principal.value ->> 'bairro' AS bairro, " +
            "endereco_principal.value ->> 'complemento' AS complemento " +
            "FROM pessoas p " +
            "LEFT JOIN enderecos e ON p.id = e.pessoa_id " +
            "LEFT JOIN LATERAL ( " +
            "    SELECT value " +
            "    FROM jsonb_array_elements(e.dados_endereco) AS value " +
            "    WHERE (value ->> 'principal') = 'true' " +
            ") endereco_principal ON true", nativeQuery = true)
    List<Object[]> findPessoasAndEnderecos();

}
