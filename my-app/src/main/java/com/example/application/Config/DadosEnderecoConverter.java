package com.example.application.Config;

import com.example.application.Model.DadosEndereco;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;

public class DadosEnderecoConverter implements AttributeConverter<DadosEndereco, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public String convertToDatabaseColumn(DadosEndereco dadosEndereco) {
        try{
            return objectMapper.writeValueAsString(dadosEndereco);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON writing error", e);
        }
    }

    @Override
    public DadosEndereco convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, DadosEndereco.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON reading error", e);
        }
    }
}
