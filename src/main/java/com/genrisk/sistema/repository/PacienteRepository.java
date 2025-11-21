package com.genrisk.sistema.repository;

import com.genrisk.sistema.model.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, String> {

    Optional<Paciente> findTopByIdPacienteStartingWithOrderByIdPacienteDesc(String prefijo);
}
