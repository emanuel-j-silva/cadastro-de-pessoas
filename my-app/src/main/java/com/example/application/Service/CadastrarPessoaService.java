package com.example.application.Service;

import com.example.application.Exception.PessoaExistenteException;
import com.example.application.Model.Pessoa;
import com.example.application.Repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CadastrarPessoaService {

    @Autowired
    PessoaRepository pessoaRepository;

    public Pessoa executar(Pessoa pessoa){
        if (pessoaRepository.existsByNome(pessoa.getNome())){
            throw new PessoaExistenteException(pessoa.getNome());
        }
        return pessoaRepository.save(pessoa);
    }
}
