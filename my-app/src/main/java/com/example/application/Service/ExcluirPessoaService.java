package com.example.application.Service;

import com.example.application.Exception.PessoaNotFoundException;
import com.example.application.Model.Pessoa;
import com.example.application.Repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExcluirPessoaService {

    @Autowired
    PessoaRepository pessoaRepository;

    public String executar(Pessoa pessoa){
        if (!pessoaRepository.existsById(pessoa.getId())){
            throw new PessoaNotFoundException();
        }
        pessoaRepository.delete(pessoa);
        return "Pessoa deletada com sucesso.";
    }
}
