package com.example.application.Service;

import com.example.application.Model.Endereco;
import com.example.application.Repository.EnderecoRepository;
import com.example.application.Repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FindEnderecoByPessoaService {

    @Autowired PessoaRepository pessoaRepository;
    @Autowired EnderecoRepository enderecoRepository;

    public Optional<Endereco> executar(int pessoaId){
        var endereco  = enderecoRepository.findByPessoaId(pessoaId);
        return endereco;
    }
}
