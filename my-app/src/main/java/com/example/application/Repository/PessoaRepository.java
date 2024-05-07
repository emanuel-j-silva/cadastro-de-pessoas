package com.example.application.Repository;

import com.example.application.Model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa,Integer> {

    boolean existsByNome(String nome);
    boolean existsByNomeAndIdNot(String nome, Long id);

}
