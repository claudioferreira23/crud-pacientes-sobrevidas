package com.sobrevidas.crud_pacientes.mapper;

import com.sobrevidas.crud_pacientes.dto.PacientePatchDTO;
import com.sobrevidas.crud_pacientes.dto.PacienteRequestDTO;
import com.sobrevidas.crud_pacientes.dto.PacienteResponseDTO;
import com.sobrevidas.crud_pacientes.entity.Paciente;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PacienteMapper {

    Paciente toEntity(PacienteRequestDTO dto);

    PacienteResponseDTO toResponseDTO(Paciente entity);

    void updateEntityFromDto(PacienteRequestDTO dto, @MappingTarget Paciente entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchEntityFromDto(PacientePatchDTO dto, @MappingTarget Paciente entity);
}
