package com.sobrevidas.crud_pacientes.repository;

import com.sobrevidas.crud_pacientes.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    boolean existsByCpf(String cpf);

}
