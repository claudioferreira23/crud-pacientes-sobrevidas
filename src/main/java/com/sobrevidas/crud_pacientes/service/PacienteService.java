package com.sobrevidas.crud_pacientes.service;

import com.sobrevidas.crud_pacientes.dto.PacientePatchDTO;
import com.sobrevidas.crud_pacientes.dto.PacienteRequestDTO;
import com.sobrevidas.crud_pacientes.dto.PacienteResponseDTO;
import com.sobrevidas.crud_pacientes.exception.ResourceAlreadyExistsException;
import com.sobrevidas.crud_pacientes.exception.ResourceNotFoundException;
import com.sobrevidas.crud_pacientes.mapper.PacienteMapper;
import com.sobrevidas.crud_pacientes.entity.Paciente;
import com.sobrevidas.crud_pacientes.repository.PacienteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository repository;
    private final PacienteMapper mapper;

    public List<PacienteResponseDTO> listarTodosPacientes() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public PacienteResponseDTO buscarPacientePorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com id: " + id));
    }

    @Transactional
    public PacienteResponseDTO salvarPaciente(PacienteRequestDTO dto) {
        if (repository.existsByCpf(dto.cpf())) {
            throw new ResourceAlreadyExistsException("CPF já cadastrado.");
        }

        Paciente paciente = mapper.toEntity(dto);
        Paciente pacienteSalvo = repository.save(paciente);
        return mapper.toResponseDTO(pacienteSalvo);
    }

    @Transactional
    public PacienteResponseDTO atualizarPaciente(Long id, PacienteRequestDTO dto) {
        Paciente pacienteExistente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com id: " + id));

        if (!pacienteExistente.getCpf().equals(dto.cpf()) && repository.existsByCpf(dto.cpf())) {
            throw new ResourceAlreadyExistsException("CPF já cadastrado para outro paciente.");
        }

        mapper.updateEntityFromDto(dto, pacienteExistente);

        Paciente pacienteSalvo = repository.save(pacienteExistente);

        return mapper.toResponseDTO(pacienteSalvo);
    }

    @Transactional
    public void removerPaciente(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Paciente não encontrado com id: " + id);
        }
        repository.deleteById(id);
    }

    @Transactional
    public PacienteResponseDTO atualizarPacienteParcial(Long id, PacientePatchDTO dto) {
        Paciente pacienteExistente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com id: " + id));

        mapper.patchEntityFromDto(dto, pacienteExistente);

        Paciente pacienteSalvo = repository.save(pacienteExistente);

        return mapper.toResponseDTO(pacienteSalvo);
    }

}
