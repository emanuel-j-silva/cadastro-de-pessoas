package com.example.application.Config;

import com.example.application.Model.DadosEndereco;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Converter(autoApply = true)
public class DadosEnderecoListConverter implements AttributeConverter<List<DadosEndereco>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public String convertToDatabaseColumn(List<DadosEndereco> attribute) {
        if (attribute == null || attribute.isEmpty()){
            return null;
        }

        try{
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON writing error", e);
        }
    }

    @Override
    public List<DadosEndereco> convertToEntityAttribute(String dbData) {
        if (dbData == null){
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<DadosEndereco>>() {});
        } catch (JsonProcessingException e) {
            try {
                DadosEndereco endereco = objectMapper.readValue(dbData, DadosEndereco.class);
                return Collections.singletonList(endereco);
            }catch (JsonProcessingException ex){
                throw new IllegalArgumentException("Error converting JSON to list or object", ex);
            }
        }
    }
}
