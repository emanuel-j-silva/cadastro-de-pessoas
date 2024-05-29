package com.example.application.Service;

import com.example.application.DTO.PessoaAndEnderecoDTO;
import com.example.application.Repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FindAllPessoasWithEnderecosService {
    @Autowired PessoaRepository pessoaRepository;

    public List<PessoaAndEnderecoDTO> executar(){
        var listPessoas = pessoaRepository.findPessoasAndEnderecos();

        return listPessoas.stream()
                .map(pessoa -> new PessoaAndEnderecoDTO(
                        (Integer) pessoa[0],
                        (String) pessoa[1],
                        (String) pessoa[2],
                        (Date) pessoa[3],
                        (Boolean) pessoa[4],
                        (String) pessoa[5],
                        (String) pessoa[6],
                        (String) pessoa[7],
                        (String) pessoa[8]
                )).collect(Collectors.toList());
    }
}
