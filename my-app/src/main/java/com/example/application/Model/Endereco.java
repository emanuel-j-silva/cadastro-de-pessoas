package com.example.application.Model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import elemental.json.JsonType;
import jakarta.persistence.*;

import org.hibernate.annotations.Type;


import java.util.Map;



@Entity
@Table(name = "enderecos")
@Convert(attributeName = "jsonb", converter = JsonBinaryType.class)
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "dados_endereco", columnDefinition = "jsonb")
    private Map<String, Map<String, Object>> dadosEndereco;


}
