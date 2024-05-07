package com.example.application.Exception;

public class PessoaExistenteException extends RuntimeException{
    public PessoaExistenteException(String nome){
        super("Uma pessoa com o nome '" + nome + "' já existe.");
    }
}
