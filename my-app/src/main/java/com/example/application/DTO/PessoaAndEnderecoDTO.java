package com.example.application.DTO;

import java.util.Date;

public record PessoaAndEnderecoDTO(Integer id, String nome, String sexo, Date dataNascimento,
                                   Boolean voluntario, String rua, String numero,
                                   String bairro, String complemento) {


}