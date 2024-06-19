package com.example.application.Controller;

import com.example.application.DTO.DadosEnderecoDTO;
import com.example.application.Model.DadosEndereco;
import com.example.application.Service.CadastrarEnderecoService;
import com.example.application.Service.FindEnderecoByPessoaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class EnderecoController {

    @Autowired CadastrarEnderecoService cadastrarEndereco;
    @Autowired FindEnderecoByPessoaService findEnderecoByPessoa;

    @PostMapping("/api/pessoas/{pessoaId}/endereco")
    public ResponseEntity<DadosEndereco> salvarEndereco(@PathVariable(value = "pessoaId") int pessoaId,
                                                @RequestBody @Valid DadosEnderecoDTO dadosEnderecoDTO){

        DadosEndereco endereco = cadastrarEndereco.executar(pessoaId, dadosEnderecoDTO);

        if (endereco != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(endereco);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/api/pessoas/{pessoaId}/endereco")
    public ResponseEntity<Object> findEnderecoByPessoa(
            @PathVariable(value = "pessoaId") int pessoaId){

        var endereco = findEnderecoByPessoa.executar(pessoaId);
        if (endereco.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereço não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(endereco.get().getDadosEndereco());
    }
}
