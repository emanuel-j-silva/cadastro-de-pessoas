package com.example.application.Service;

import com.example.application.Model.Pessoa;
import com.example.application.Repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindAllPessoaService {

    @Autowired
    PessoaRepository pessoaRepository;

    public List<Pessoa> executar(){
        List<Pessoa> pessoas = pessoaRepository.findAll();
        return pessoas;
    }
}
