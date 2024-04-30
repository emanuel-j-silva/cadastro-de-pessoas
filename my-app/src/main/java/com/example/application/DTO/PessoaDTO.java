package com.example.application.DTO;

import com.example.application.Model.Sexo;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record PessoaDTO(@NotBlank String nome,
                        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate dataNascimento,
                        Sexo sexo, Boolean voluntario) {
}
