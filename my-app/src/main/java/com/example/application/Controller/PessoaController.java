package com.example.application.Controller;

import com.example.application.DTO.PessoaDTO;
import com.example.application.Model.Pessoa;
import com.example.application.Service.CadastrarPessoaService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PessoaController {

    @Autowired
    CadastrarPessoaService cadastrarPessoa;

    @PostMapping("/api/pessoas")
    public ResponseEntity<Pessoa> salvarPessoa(@RequestBody @Valid PessoaDTO pessoaDTO){
        var pessoa = new Pessoa();

        pessoa.setNome(pessoaDTO.nome());
        pessoa.setDataNascimento(pessoaDTO.dataNascimento());
        pessoa.setSexo(pessoaDTO.sexo());
        pessoa.setVoluntario(pessoaDTO.voluntario());

        return ResponseEntity.status(HttpStatus.OK).body(cadastrarPessoa.executar(pessoa));
    }
}
