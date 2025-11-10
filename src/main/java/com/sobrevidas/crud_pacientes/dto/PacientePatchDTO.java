package com.sobrevidas.crud_pacientes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PacientePatchDTO(

        @Schema(description = "CPF do paciente (11 dígitos, sem pontos ou traços)", example = "12345678901")
        @Size(min = 11, max = 11, message = "CPF deve ter 11 dígitos")
        @Pattern(regexp = "[0-9]*", message = "CPF deve conter apenas números")
        String cpf,

        @Schema(description = "Nome completo do paciente", example = "João da Silva")
        @Pattern(regexp = ".*\\S+.*", message = "Nome não pode ser vazio")
        String nome,

        @Schema(description = "Data de nascimento (formato AAAA-MM-DD)", example = "1990-01-30")
        @Past(message = "Data de nascimento deve ser no passado")
        LocalDate dataNascimento,

        @Schema(description = "Nome completo da mãe do paciente", example = "Maria da Silva")
        @Pattern(regexp = ".*\\S+.*", message = "Nome da mãe não pode ser vazio")
        String nomeMae,

        @Schema(description = "CEP (formato XXXXXXXX)", example = "74000000")
        @Pattern(regexp = ".*\\S+.*", message = "CEP não pode ser vazio")
        String cep,

        @Schema(description = "Endereço (Rua, Av, etc)", example = "Rua 10")
        @Pattern(regexp = ".*\\S+.*", message = "Endereço não pode ser vazio")
        String endereco,

        @Schema(description = "Número do endereço", example = "100")
        @Pattern(regexp = ".*\\S+.*", message = "Número do endereço não pode ser vazio")
        String numEndereco,

        @Schema(description = "Complemento (Casa, Apto, etc). Envie \"\" (string vazia) para limpar o campo.", example = "Apto 201")
        String complemento,

        @Schema(description = "Bairro", example = "Setor Central")
        @Pattern(regexp = ".*\\S+.*", message = "Bairro não pode ser vazio")
        String bairro,

        @Schema(description = "Cidade", example = "Goiânia")
        @Pattern(regexp = ".*\\S+.*", message = "Cidade não pode ser vazia")
        String cidade,

        @Schema(description = "Estado (Sigla UF)", example = "GO")
        @Pattern(regexp = ".*\\S+.*", message = "Estado não pode ser vazio")
        String estado,

        @Schema(description = "Telefone celular com DDD", example = "62999998888")
        @Pattern(regexp = ".*\\S+.*", message = "Telefone celular não pode ser vazio")
        String telefoneCelular,

        @Schema(description = "Telefone do responsável com DDD", example = "62988887777")
        @Pattern(regexp = ".*\\S+.*", message = "Telefone do responsável não pode ser vazio")
        String telefoneResponsavel,

        @Schema(description = "Endereço de e-mail válido", example = "joao.silva@email.com")
        @Email(message = "Email deve ter um formato válido")
        @Pattern(regexp = ".*\\S+.*", message = "Email não pode ser vazio")
        String email,

        @Schema(description = "Sexo do paciente", example = "Masculino")
        @Pattern(regexp = ".*\\S+.*", message = "Sexo não pode ser vazio")
        String sexo,

        @Schema(description = "Número do Cartão SUS", example = "700001234567890")
        @Pattern(regexp = ".*\\S+.*", message = "Número do Cartão SUS não pode ser vazio")
        String numCartaoSus,

        @Schema(description = "Paciente é tabagista?", example = "false")
        Boolean ehTabagista,

        @Schema(description = "Paciente é etilista?", example = "false")
        Boolean ehEtilista,

        @Schema(description = "Paciente tem lesão suspeita?", example = "false")
        Boolean temLesaoSuspeita,

        @Schema(description = "Paciente participa do Smart Monitor?", example = "true")
        Boolean participaSmartMonitor
) {}
