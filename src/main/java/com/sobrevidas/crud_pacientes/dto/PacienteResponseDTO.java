package com.sobrevidas.crud_pacientes.dto;

import java.time.LocalDate;

public record PacienteResponseDTO(
        Long id,
        String cpf,
        String nome,
        LocalDate dataNascimento,
        String nomeMae,
        String cep,
        String endereco,
        String numEndereco,
        String complemento,
        String bairro,
        String cidade,
        String estado,
        String telefoneCelular,
        String telefoneResponsavel,
        String email,
        String sexo,
        String numCartaoSus,
        Boolean ehTabagista,
        Boolean ehEtilista,
        Boolean temLesaoSuspeita,
        Boolean participaSmartMonitor
) {}
