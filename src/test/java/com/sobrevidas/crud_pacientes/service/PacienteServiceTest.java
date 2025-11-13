package com.sobrevidas.crud_pacientes.service;

import com.sobrevidas.crud_pacientes.dto.PacientePatchDTO;
import com.sobrevidas.crud_pacientes.dto.PacienteRequestDTO;
import com.sobrevidas.crud_pacientes.dto.PacienteResponseDTO;
import com.sobrevidas.crud_pacientes.exception.ResourceAlreadyExistsException;
import com.sobrevidas.crud_pacientes.exception.ResourceNotFoundException;
import com.sobrevidas.crud_pacientes.mapper.PacienteMapper;
import com.sobrevidas.crud_pacientes.entity.Paciente;
import com.sobrevidas.crud_pacientes.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    private PacienteRepository repository;

    @Mock
    private PacienteMapper mapper;

    @InjectMocks
    private PacienteService service;

    private Paciente paciente;
    private PacienteRequestDTO requestDTO;
    private PacientePatchDTO patchDTO;
    private PacienteResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        paciente = Paciente.builder()
                .id(1L)
                .nome("Paciente Teste")
                .cpf("12345678901")
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .build();

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
    @DisplayName("Salva paciente com sucesso")
    void salvarPaciente_SalvarComSucesso() {
        when(repository.existsByCpf(requestDTO.cpf())).thenReturn(false);
        when(mapper.toEntity(requestDTO)).thenReturn(paciente);
        when(repository.save(paciente)).thenReturn(paciente);
        when(mapper.toResponseDTO(paciente)).thenReturn(responseDTO);

        PacienteResponseDTO resultado = service.salvarPaciente(requestDTO);

        assertNotNull(resultado);
        assertEquals(responseDTO.id(), resultado.id());
        assertEquals(responseDTO.nome(), resultado.nome());
        verify(repository, times(1)).existsByCpf(requestDTO.cpf());
        verify(repository, times(1)).save(paciente);
    }

    @Test
    @DisplayName("salvarPaciente lança ResourceAlreadyExistsException quando CPF já existe")
    void salvarPaciente_LancaResourceAlreadyExistsException_QuandoCpfJaExiste() {
        when(repository.existsByCpf(requestDTO.cpf())).thenReturn(true);

        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class, () -> {
            service.salvarPaciente(requestDTO);
        });

        assertEquals("CPF já cadastrado.", exception.getMessage());
        verify(repository, never()).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Busca paciente por ID com sucesso")
    void buscarPacientePorId_RetornaPaciente_QuandoIdExiste() {
        when(repository.findById(1L)).thenReturn(Optional.of(paciente));
        when(mapper.toResponseDTO(paciente)).thenReturn(responseDTO);

        PacienteResponseDTO resultado = service.buscarPacientePorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("buscarPaciente lança ResourceNotFoundException ao buscar ID inexistente")
    void buscarPacientePorId_LancaResourceNotFoundException_QuandoIdNaoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.buscarPacientePorId(99L);
        });

        assertEquals("Paciente não encontrado com id: 99", exception.getMessage());
    }

    @Test
    @DisplayName("Atualiza paciente com sucesso (PUT)")
    void atualizarPaciente_AtualizarComSucesso() {
        when(repository.findById(1L)).thenReturn(Optional.of(paciente));
        when(repository.save(paciente)).thenReturn(paciente);
        when(mapper.toResponseDTO(paciente)).thenReturn(responseDTO);

        PacienteResponseDTO resultado = service.atualizarPaciente(1L, requestDTO);

        assertNotNull(resultado);
        verify(repository, times(1)).findById(1L);
        verify(mapper, times(1)).updateEntityFromDto(requestDTO, paciente);
        verify(repository, times(1)).save(paciente);
    }

    @Test
    @DisplayName("atualizarPaciente lança ResourceNotFoundException quando ID não existe (PUT)")
    void atualizarPaciente_LancaResourceNotFoundException_QuandoIdNaoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            service.atualizarPaciente(99L, requestDTO);
        });
        verify(mapper, never()).updateEntityFromDto(any(), any());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("atualizarPaciente lança ResourceAlreadyExistsException ao atualizar para CPF duplicado (PUT)")
    void atualizarPaciente_LancaResourceAlreadyExistsException_QuandoCpfJaExisteEmOutro() {
        PacienteRequestDTO dtoCpfDuplicado = new PacienteRequestDTO(
                "00011122233",
                "Outro Nome",
                LocalDate.of(1990, 1, 1), "Mae Teste", "74000000", "Rua Teste", "1",
                "Casa", "Bairro Teste", "Cidade Teste", "GO",
                "62999999999", "62988888888", "teste@teste.com",
                "Masculino", "987654321",
                false, false, false, false
        );

        when(repository.findById(1L)).thenReturn(Optional.of(paciente));
        when(repository.existsByCpf("00011122233")).thenReturn(true);

        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class, () -> {
            service.atualizarPaciente(1L, dtoCpfDuplicado);
        });

        assertEquals("CPF já cadastrado para outro paciente.", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Atualiza paciente parcialmente com sucesso (PATCH)")
    void atualizarPacienteParcial_AtualizaComSucesso() {
        patchDTO = new PacientePatchDTO(
                false,
                false,
                true
        );

        when(repository.findById(1L)).thenReturn(Optional.of(paciente));
        when(repository.save(paciente)).thenReturn(paciente);
        when(mapper.toResponseDTO(paciente)).thenReturn(responseDTO);

        PacienteResponseDTO resultado = service.atualizarPacienteParcial(1L, patchDTO);

        assertNotNull(resultado);
        verify(repository, times(1)).findById(1L);
        verify(mapper, times(1)).patchEntityFromDto(patchDTO, paciente);
        verify(repository, times(1)).save(paciente);
    }

    @Test
    @DisplayName("atualizarPacienteParcial lança ResourceNotFoundException quando ID não existe (PATCH)")
    void atualizarPacienteParcial_LancaResourceNotFoundException_QuandoIdNaoExiste() {
        patchDTO = new PacientePatchDTO(false, true, false);

        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            service.atualizarPacienteParcial(99L, patchDTO);
        });

        verify(mapper, never()).patchEntityFromDto(any(), any());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Remove paciente com sucesso")
    void removerPaciente_RemoveComSucesso() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        service.removerPaciente(1L);

        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("removerPaciente lança ResourceNotFoundException quando ID não existe")
    void removerPaciente_DeveLancarResourceNotFoundException_QuandoIdNaoExiste() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            service.removerPaciente(99L);
        });
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("lista pacientes com sucesso")
    void listarTodosPacientes_RetornaListaPacientes() {
        when(repository.findAll()).thenReturn(List.of(paciente));
        when(mapper.toResponseDTO(paciente)).thenReturn(responseDTO);

        List<PacienteResponseDTO> lista = service.listarTodosPacientes();

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("Paciente Teste", lista.get(0).nome());
        verify(repository, times(1)).findAll();
    }
}