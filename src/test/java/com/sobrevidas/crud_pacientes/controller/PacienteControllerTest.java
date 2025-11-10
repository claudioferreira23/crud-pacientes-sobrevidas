package com.sobrevidas.crud_pacientes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sobrevidas.crud_pacientes.dto.PacientePatchDTO;
import com.sobrevidas.crud_pacientes.dto.PacienteRequestDTO;
import com.sobrevidas.crud_pacientes.dto.PacienteResponseDTO;
import com.sobrevidas.crud_pacientes.exception.GlobalExceptionHandler;
import com.sobrevidas.crud_pacientes.exception.ResourceAlreadyExistsException;
import com.sobrevidas.crud_pacientes.exception.ResourceNotFoundException;
import com.sobrevidas.crud_pacientes.service.PacienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PacienteController.class)
@ContextConfiguration(classes = {PacienteController.class, GlobalExceptionHandler.class})
class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PacienteService service;

    private PacienteRequestDTO requestDTO;
    private PacienteResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new PacienteRequestDTO(
                "12345678901",
                "Paciente Teste",
                LocalDate.of(1990, 1, 1),
                "Mae Teste", "74000000", "Rua Teste", "1",
                "Casa", "Bairro Teste", "Cidade Teste", "GO",
                "62999999999", "62988888888", "teste@teste.com",
                "Masculino", "987654321",
                false, false, false, false
        );

        responseDTO = new PacienteResponseDTO(
                1L, "12345678901", "Paciente Teste",
                LocalDate.of(1990, 1, 1), "Mae Teste", "74000000",
                "Rua Teste", "1", "Casa", "Bairro Teste", "Cidade Teste", "GO",
                "62999999999", "62988888888", "teste@teste.com",
                "Masculino", "987654321",
                false, false, false, false
        );
    }

    @Test
    @DisplayName("POST /pacientes - Deve retornar 201 Created quando DTO é válido")
    void salvarPaciente_DeveRetornar201Created_QuandoDtoValido() throws Exception {
        when(service.salvarPaciente(any(PacienteRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/pacientes/1"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Paciente Teste")));
    }

    @Test
    @DisplayName("POST /pacientes - Deve retornar 400 Bad Request quando @Valid falha")
    void salvarPaciente_DeveRetornar400BadRequest_QuandoValidacaoFalha() throws Exception {
        PacienteRequestDTO dtoInvalido = new PacienteRequestDTO(
                "123",
                "",
                LocalDate.of(1990, 1, 1), "Mae Teste", "74000000", "Rua Teste", "1",
                "Casa", "Bairro Teste", "Cidade Teste", "GO",
                "62999999999", "62988888888", "teste@teste.com",
                "Masculino", "987654321",
                false, false, false, false
        );

        mockMvc.perform(post("/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Erro de validação. Verifique os campos.")))
                .andExpect(jsonPath("$.validationErrors.cpf").exists())
                .andExpect(jsonPath("$.validationErrors.nome", is("Nome não pode ser nulo ou vazio")));
    }

    @Test
    @DisplayName("POST /pacientes - Deve retornar 409 Conflict quando CPF já existe")
    void salvarPaciente_DeveRetornar409BadConflict_QuandoCpfJaExiste() throws Exception {
        when(service.salvarPaciente(any(PacienteRequestDTO.class)))
                .thenThrow(new ResourceAlreadyExistsException("CPF já cadastrado."));

        mockMvc.perform(post("/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.message", is("CPF já cadastrado.")));
    }

    @Test
    @DisplayName("GET /pacientes - Deve retornar 200 OK e lista de pacientes")
    void listarTodosPacientes_DeveRetornar200OK_QuandoExistemPacientes() throws Exception {
        when(service.listarTodosPacientes()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/pacientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nome", is("Paciente Teste")));
    }

    @Test
    @DisplayName("GET /pacientes - Deve retornar lista vazia quando não há pacientes")
    void listarTodosPacientes_DeveRetornarListaVazia_QuandoNaoHaPacientes() throws Exception {
        when(service.listarTodosPacientes()).thenReturn(List.of());

        mockMvc.perform(get("/pacientes"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }


    @Test
    @DisplayName("GET /pacientes/{id} - Deve retornar 200 OK quando ID existe")
    void buscarPacientePorId_DeveRetornar200OK_QuandoIdExiste() throws Exception {
        when(service.buscarPacientePorId(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/pacientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.cpf", is("12345678901")));
    }

    @Test
    @DisplayName("GET /pacientes/{id} - Deve retornar 404 Not Found quando ID não existe (ResourceNotFoundException)")
    void buscarPacientePorId_DeveRetornar404NotFound_QuandoIdNaoExiste() throws Exception {
        when(service.buscarPacientePorId(99L))
                .thenThrow(new ResourceNotFoundException("Paciente não encontrado com id: 99"));

        mockMvc.perform(get("/pacientes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Paciente não encontrado com id: 99")));
    }

    @Test
    @DisplayName("PUT /pacientes/{id} - Deve retornar 200 OK quando atualização for bem-sucedida")
    void atualizarPaciente_DeveRetornar200OK_QuandoAtualizacaoValida() throws Exception {
        when(service.atualizarPaciente(eq(1L), any(PacienteRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/pacientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Paciente Teste")));
    }

    @Test
    @DisplayName("PUT /pacientes/{id} - Deve retornar 404 Not Found quando paciente não existe")
    void atualizarPaciente_DeveRetornar404NotFound_QuandoPacienteNaoExiste() throws Exception {
        when(service.atualizarPaciente(eq(99L), any(PacienteRequestDTO.class)))
                .thenThrow(new ResourceNotFoundException("Paciente não encontrado com id: 99"));

        mockMvc.perform(put("/pacientes/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Paciente não encontrado com id: 99")));
    }

    @Test
    @DisplayName("PUT /pacientes/{id} - Deve retornar 409 Conflict quando CPF já está cadastrado em outro paciente")
    void atualizarPaciente_DeveRetornar409Conflict_QuandoCpfDuplicado() throws Exception {
        when(service.atualizarPaciente(eq(1L), any(PacienteRequestDTO.class)))
                .thenThrow(new ResourceAlreadyExistsException("CPF já cadastrado para outro paciente."));

        mockMvc.perform(put("/pacientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("CPF já cadastrado para outro paciente.")));
    }

    @Test
    @DisplayName("PATCH /pacientes/{id} - Deve retornar 200 OK quando atualização parcial for bem-sucedida")
    void atualizarPacienteParcial_DeveRetornar200OK_QuandoAtualizacaoValida() throws Exception {
        PacientePatchDTO patchDTO = new PacientePatchDTO("00011122233",
                null, null, null, null, null, null, null,
                null, null, null, null, null, null, null,
                null, null, null, null, null);
        PacienteResponseDTO atualizado = new PacienteResponseDTO(
                1L, "12345678901", "Paciente Atualizado", responseDTO.dataNascimento(),
                responseDTO.nomeMae(), responseDTO.cep(), responseDTO.endereco(),
                responseDTO.numEndereco(), responseDTO.complemento(), responseDTO.bairro(),
                responseDTO.cidade(), responseDTO.estado(), responseDTO.telefoneCelular(),
                responseDTO.telefoneResponsavel(), responseDTO.email(), responseDTO.sexo(),
                responseDTO.numCartaoSus(), responseDTO.ehTabagista(), responseDTO.ehEtilista(),
                responseDTO.temLesaoSuspeita(), responseDTO.participaSmartMonitor()
        );

        when(service.atualizarPacienteParcial(eq(1L), any(PacientePatchDTO.class)))
                .thenReturn(atualizado);

        mockMvc.perform(patch("/pacientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Paciente Atualizado")));
    }

    @Test
    @DisplayName("PATCH /pacientes/{id} - Deve retornar 404 Not Found quando paciente não existe")
    void atualizarPacienteParcial_DeveRetornar404NotFound_QuandoPacienteNaoExiste() throws Exception {
        when(service.atualizarPacienteParcial(eq(99L), any(PacientePatchDTO.class)))
                .thenThrow(new ResourceNotFoundException("Paciente não encontrado com id: 99"));

        mockMvc.perform(patch("/pacientes/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new PacientePatchDTO(
                                        null, null, null, null, null, null,
                                        null, null, null, null, null,
                                        null, null, null, null, null,
                                        null, null, null, null
                                )
                        )))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Paciente não encontrado com id: 99")));
    }

    @Test
    @DisplayName("PATCH /pacientes/{id} - Deve retornar 409 Conflict quando CPF já está cadastrado em outro paciente")
    void atualizarPacienteParcial_DeveRetornar409Conflict_QuandoCpfDuplicado() throws Exception {
        when(service.atualizarPacienteParcial(eq(1L), any(PacientePatchDTO.class)))
                .thenThrow(new ResourceAlreadyExistsException("CPF já cadastrado para outro paciente."));

        mockMvc.perform(patch("/pacientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new PacientePatchDTO(
                                        null, null, null, null, null, null,
                                        null, null, null, null, null,
                                        null, null, null, null, null,
                                        null, null, null, null
                                )
                        )))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("CPF já cadastrado para outro paciente.")));
    }

    @Test
    @DisplayName("DELETE /pacientes/{id} - Deve retornar 204 No Content quando ID existe")
    void removerPaciente_DeveRetornar204NoContent_QuandoIdExiste() throws Exception {
        doNothing().when(service).removerPaciente(1L);

        mockMvc.perform(delete("/pacientes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /pacientes/{id} - Deve retornar 404 Not Found quando ID não existe")
    void removerPaciente_DeveRetornar404NotFound_QuandoIdNaoExiste() throws Exception {
        doThrow(new ResourceNotFoundException("Paciente não encontrado"))
                .when(service).removerPaciente(99L);

        mockMvc.perform(delete("/pacientes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Paciente não encontrado")));
    }
}