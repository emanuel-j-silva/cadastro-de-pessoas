package com.example.application.Controller;

import com.example.application.DTO.DadosEnderecoDTO;
import com.example.application.Model.DadosEndereco;
import com.example.application.Model.Endereco;
import com.example.application.Service.CadastrarEnderecoService;
import com.example.application.Service.FindEnderecoByPessoaService;
import com.example.application.Service.FindOnePessoaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EnderecoController {

    @Autowired CadastrarEnderecoService cadastrarEndereco;
    @Autowired FindOnePessoaService findOnePessoa;
    @Autowired FindEnderecoByPessoaService findEnderecoByPessoa;

    @PostMapping("/api/pessoas/{pessoaId}/endereco")
    public ResponseEntity<Endereco> salvarEndereco(@PathVariable(value = "pessoaId") int pessoaId,
                                                @RequestBody @Valid DadosEnderecoDTO dadosEnderecoDTO){
        var endereco = new Endereco();
        var dadosEndereco = new DadosEndereco();
        var bairro = dadosEnderecoDTO.bairro();
        var complemento = dadosEnderecoDTO.complemento();
        dadosEndereco.setRua(dadosEnderecoDTO.rua());
        dadosEndereco.setNumero(dadosEnderecoDTO.numero());

        if (bairro != null) dadosEndereco.setBairro(bairro);
        if (complemento != null) dadosEndereco.setComplemento(complemento);

        endereco.setPessoa(findOnePessoa.executar(pessoaId));
        endereco.setDadosEndereco(dadosEndereco);
        return ResponseEntity.status(HttpStatus.CREATED).body(cadastrarEndereco.executar(endereco));
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
