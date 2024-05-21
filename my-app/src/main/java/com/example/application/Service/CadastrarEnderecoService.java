package com.example.application.Service;

import com.example.application.Model.Endereco;
import com.example.application.Repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CadastrarEnderecoService {

    @Autowired
    EnderecoRepository enderecoRepository;

    public Endereco executar(Endereco endereco){
        return enderecoRepository.save(endereco);
    }
}
