package com.example.application.Controller;

import com.example.application.DTO.PessoaDTO;
import com.example.application.Model.Pessoa;
import com.example.application.Service.CadastrarPessoaService;
import com.example.application.Service.ExcluirPessoaService;
import com.example.application.Service.FindAllPessoaService;
import com.example.application.Service.FindOnePessoaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PessoaController {

    @Autowired CadastrarPessoaService cadastrarPessoa;
    @Autowired FindAllPessoaService findAll;
    @Autowired FindOnePessoaService findOne;
    @Autowired ExcluirPessoaService excluirPessoa;



    @PostMapping("/api/pessoas")
    public ResponseEntity<Pessoa> salvarPessoa(@RequestBody @Valid PessoaDTO pessoaDTO){
        var pessoa = new Pessoa();

        pessoa.setNome(pessoaDTO.nome());
        pessoa.setDataNascimento(pessoaDTO.dataNascimento());
        pessoa.setSexo(pessoaDTO.sexo());
        pessoa.setVoluntario(pessoaDTO.voluntario());

        return ResponseEntity.status(HttpStatus.OK).body(cadastrarPessoa.executar(pessoa));
    }

    @GetMapping("/api/pessoas")
    public ResponseEntity<List<Pessoa>> findAllPessoas(){
        var listPessoas = findAll.executar();
        return listPessoas != null ?
                ResponseEntity.status(HttpStatus.OK).body(listPessoas):
                ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/api/pessoas/{id}")
    public ResponseEntity<Object> findOnePessoa(@PathVariable(value = "id")Integer id){
        var pessoa = findOne.executar(id);
        return ResponseEntity.status(HttpStatus.OK).body(pessoa);
    }

    @DeleteMapping("/api/pessoas/{id}")
    public ResponseEntity<Object> deletarPessoa(@PathVariable(value = "id")Integer id){
        var pessoa = findOne.executar(id);
        return ResponseEntity.status(HttpStatus.OK).body("Pessoa deletada com sucesso.");
    }
}
