package com.genrisk.sistema.repository;

import com.genrisk.sistema.model.entity.Formulario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormularioRepository extends JpaRepository<Formulario, Integer> {
    List<Formulario> findByPacienteIdPaciente(String pacienteId);
}
