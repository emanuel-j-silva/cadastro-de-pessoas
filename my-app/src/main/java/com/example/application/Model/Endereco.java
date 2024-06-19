package com.example.application.Model;

import com.example.application.Config.DadosEnderecoListConverter;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;

import java.util.List;


@Entity
@Table(name = "enderecos")
@Convert(attributeName = "jsonb", converter = JsonBinaryType.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Convert(converter = DadosEnderecoListConverter.class)
    @Column(name = "dados_endereco", columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private List<DadosEndereco> dadosEndereco;

    @OneToOne
    @JoinColumn(name = "pessoa_id", nullable = false, unique = true)
    private Pessoa pessoa;

}
