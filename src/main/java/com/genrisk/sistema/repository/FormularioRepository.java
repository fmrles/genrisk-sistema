package com.genrisk.sistema.repository;

import com.genrisk.sistema.model.entity.Formulario;
import com.genrisk.sistema.model.entity.Paciente;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FormularioRepository extends JpaRepository<Formulario, Integer> {
    List<Formulario> findByPacienteIdPaciente(String pacienteId);
    Optional<Formulario> findTopByPacienteOrderByIdFormularioDesc(Paciente paciente);
}
