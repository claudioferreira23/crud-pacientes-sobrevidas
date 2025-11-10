package com.sobrevidas.crud_pacientes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record PacienteRequestDTO(
        @Schema(description = "CPF do paciente (11 dígitos, sem pontos ou traços)", example = "12345678901", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "CPF não pode ser nulo ou vazio")
        @Size(min = 11, max = 11, message = "CPF deve ter 11 dígitos")
        @Pattern(regexp = "[0-9]*", message = "CPF deve conter apenas números")
        String cpf,

        @Schema(description = "Nome completo do paciente", example = "João da Silva", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Nome não pode ser nulo ou vazio")
        String nome,

        @Schema(description = "Data de nascimento (formato AAAA-MM-DD)", example = "1990-01-30", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Data de nascimento não pode ser nula")
        @Past(message = "Data de nascimento deve ser no passado")
        LocalDate dataNascimento,

        @Schema(description = "Nome completo da mãe do paciente", example = "Maria da Silva", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Nome da mãe não pode ser nulo ou vazio")
        String nomeMae,

        @Schema(description = "CEP (XXXXXXXX)", example = "74000000", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "CEP não pode ser nulo ou vazio")
        String cep,

        @Schema(description = "Endereço (Rua, Av, etc)", example = "Rua 10", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Endereço não pode ser nulo ou vazio")
        String endereco,

        @Schema(description = "Número do endereço", example = "100", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Número do endereço não pode ser nulo ou vazio")
        String numEndereco,

        @Schema(description = "Complemento (Casa, Apto, etc). Pode ser vazio.", example = "Apto 201")
        @NotNull(message = "Complemento não pode ser nulo") // Permite "" mas não null
        String complemento,

        @Schema(description = "Bairro", example = "Setor Central", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Bairro não pode ser nulo ou vazio")
        String bairro,

        @Schema(description = "Cidade", example = "Goiânia", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Cidade não pode ser nula ou vazia")
        String cidade,

        @Schema(description = "Estado (Sigla UF)", example = "GO", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Estado não pode ser nulo ou vazio")
        String estado,

        @Schema(description = "Telefone celular com DDD", example = "62999998888", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Telefone celular não pode ser nulo ou vazio")
        String telefoneCelular,

        @Schema(description = "Telefone do responsável com DDD", example = "62988887777", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Telefone do responsável não pode ser nulo ou vazio")
        String telefoneResponsavel,

        @Schema(description = "Endereço de e-mail válido", example = "joao.silva@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Email não pode ser nulo ou vazio")
        @Email(message = "Formato de email inválido")
        String email,

        @Schema(description = "Sexo do paciente", example = "Masculino", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Sexo não pode ser nulo ou vazio")
        String sexo,

        @Schema(description = "Número do Cartão SUS", example = "700001234567890", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Número do Cartão SUS não pode ser nulo ou vazio")
        String numCartaoSus,

        @Schema(description = "Paciente é tabagista?", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "O campo 'ehTabagista' não pode ser nulo")
        Boolean ehTabagista,

        @Schema(description = "Paciente é etilista?", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "O campo 'ehEtilista' não pode ser nulo")
        Boolean ehEtilista,

        @Schema(description = "Paciente tem lesão suspeita?", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "O campo 'temLesaoSuspeita' não pode ser nulo")
        Boolean temLesaoSuspeita,

        @Schema(description = "Paciente participa do Smart Monitor?", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "O campo 'participaSmartMonitor' não pode ser nulo")
        Boolean participaSmartMonitor
) {}
