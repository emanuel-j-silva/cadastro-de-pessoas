package com.example.application.DTO;

import jakarta.validation.constraints.NotNull;

public record DadosEnderecoDTO(@NotNull String rua, @NotNull String numero,
                               String complemento, String bairro) {
}
