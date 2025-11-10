package com.sobrevidas.crud_pacientes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "pacientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String cpf;

    private String nome;
    private LocalDate dataNascimento;
    private String nomeMae;
    private String cep;
    private String endereco;
    private String numEndereco;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String telefoneCelular;
    private String telefoneResponsavel;
    private String email;
    private String sexo;
    private String numCartaoSus;
    private Boolean ehTabagista;
    private Boolean ehEtilista;
    private Boolean temLesaoSuspeita;
    private Boolean participaSmartMonitor;
}
