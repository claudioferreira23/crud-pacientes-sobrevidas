package com.sobrevidas.crud_pacientes.controller;

import com.sobrevidas.crud_pacientes.dto.PacientePatchDTO;
import com.sobrevidas.crud_pacientes.dto.PacienteRequestDTO;
import com.sobrevidas.crud_pacientes.dto.PacienteResponseDTO;
import com.sobrevidas.crud_pacientes.exception.ApiErrorResponse;
import com.sobrevidas.crud_pacientes.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Pacientes", description = "Endpoints para o gerenciamento de pacientes")
@RestController
@RequestMapping("pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService service;

    @Operation(summary = "Lista todos os pacientes", description = "Retorna uma lista de todos os pacientes.")
    @ApiResponse(responseCode = "200", description = "Lista de pacientes retornada com sucesso")
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<PacienteResponseDTO>> listarTodosPacientes() {
        return ResponseEntity.ok(service.listarTodosPacientes());
    }

    @Operation(summary = "Busca um paciente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PacienteResponseDTO> buscarPacientePorId(
            @Parameter(description = "ID do paciente a ser buscado", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPacientePorId(id));
    }

    @Operation(summary = "Cria um novo paciente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Paciente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição (erro de validação)",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflito de dados (ex: CPF já cadastrado)",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PacienteResponseDTO> salvarPaciente(
            @Parameter(description = "Objeto do paciente a ser criado", required = true)
            @Valid @RequestBody PacienteRequestDTO pacienteDTO) {
        PacienteResponseDTO pacienteSalvo = service.salvarPaciente(pacienteDTO);
        URI location = URI.create(String.format("/pacientes/%d", pacienteSalvo.id()));
        return ResponseEntity.created(location).body(pacienteSalvo);
    }

    @Operation(summary = "Atualiza um paciente (Substituição Total)",
            description = "Atualiza todos os dados de um paciente existente. Requer o envio do objeto completo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição (erro de validação)",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflito de dados (ex: CPF já cadastrado para outro paciente)",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PacienteResponseDTO> atualizarPaciente(
            @Parameter(description = "ID do paciente a ser atualizado", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Objeto completo do paciente com os novos dados", required = true)
            @Valid @RequestBody PacienteRequestDTO pacienteDTO) {

        PacienteResponseDTO pacienteAtualizado = service.atualizarPaciente(id, pacienteDTO);
        return ResponseEntity.ok(pacienteAtualizado);
    }

    @Operation(summary = "Atualiza um paciente (Atualização Parcial)",
            description = "Atualiza apenas os campos fornecidos de um paciente existente. Campos nulos são ignorados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição (erro de validação de formato)",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflito de dados (ex: CPF já cadastrado para outro paciente)",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PacienteResponseDTO> atualizarPacienteParcial(
            @Parameter(description = "ID do paciente a ser atualizado", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Objeto do paciente com os campos a serem atualizados", required = true)
            @Valid @RequestBody PacientePatchDTO patchDTO) {

        PacienteResponseDTO pacienteAtualizado = service.atualizarPacienteParcial(id, patchDTO);
        return ResponseEntity.ok(pacienteAtualizado);
    }


    @Operation(summary = "Remove um paciente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Paciente removido com sucesso (Sem conteúdo)"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> removerPaciente(
            @Parameter(description = "ID do paciente a ser removido", required = true, example = "1")
            @PathVariable Long id) {
        service.removerPaciente(id);
        return ResponseEntity.noContent().build();
    }
}