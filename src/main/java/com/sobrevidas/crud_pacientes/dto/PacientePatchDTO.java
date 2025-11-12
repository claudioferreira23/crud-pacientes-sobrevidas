package com.sobrevidas.crud_pacientes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record PacientePatchDTO(

        @Schema(description = "Paciente é tabagista?", example = "false")
        @NotNull(message = "O campo 'ehTabagista' não pode ser nulo")
        Boolean ehTabagista,

        @Schema(description = "Paciente é etilista?", example = "false")
        @NotNull(message = "O campo 'ehEtilista' não pode ser nulo")
        Boolean ehEtilista,

        @Schema(description = "Paciente tem lesão suspeita?", example = "false")
        @NotNull(message = "O campo 'temLesaoSuspeita' não pode ser nulo")
        Boolean temLesaoSuspeita
) {}
