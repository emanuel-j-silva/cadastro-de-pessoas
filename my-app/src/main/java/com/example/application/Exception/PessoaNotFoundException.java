package com.example.application.Exception;

public class PessoaNotFoundException extends RuntimeException {
    public PessoaNotFoundException(){
        super("Pessoa não encontrada.");
    }
}
