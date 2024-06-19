package com.example.application.Service;

import com.example.application.DTO.DadosEnderecoDTO;
import com.example.application.Model.DadosEndereco;
import com.example.application.Repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CadastrarEnderecoService {

    @Autowired
    EnderecoRepository enderecoRepository;
    @Autowired FindOnePessoaService findPessoa;

    public DadosEndereco executar(int pessoaId, DadosEnderecoDTO dadosEnderecoDTO){
        var pessoa = findPessoa.executar(pessoaId);
        if (pessoa == null)
            return null;

        boolean isPrincipal = dadosEnderecoDTO.principal();

        List<DadosEndereco> enderecosExistentes = recuperarEnderecosExistentes(pessoaId);

        if (isPrincipal){
            for (DadosEndereco endereco:enderecosExistentes){
                endereco.setPrincipal(false);
            }
        }

        DadosEndereco novoEndereco = new DadosEndereco();
        novoEndereco.setRua(dadosEnderecoDTO.rua());
        novoEndereco.setBairro(dadosEnderecoDTO.bairro());
        novoEndereco.setNumero(dadosEnderecoDTO.numero());
        novoEndereco.setComplemento(dadosEnderecoDTO.complemento());
        novoEndereco.setPrincipal(isPrincipal);

        enderecosExistentes.add(novoEndereco);

        atualizarEnderecos(pessoaId,enderecosExistentes);

        return novoEndereco;
    }

    private List<DadosEndereco> recuperarEnderecosExistentes(int pessoaId) {
        var optionalEndereco = enderecoRepository.findByPessoaId(pessoaId);
        if (optionalEndereco.isEmpty()) {
            return new ArrayList<>();
        }

        var endereco = optionalEndereco.get();

        return new ArrayList<>(endereco.getDadosEndereco());
    }
    private void atualizarEnderecos(int pessoaId, List<DadosEndereco> enderecos) {
        var endereco = enderecoRepository.findByPessoaId(pessoaId)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));
        endereco.setDadosEndereco(enderecos);
        enderecoRepository.save(endereco);
    }
}
