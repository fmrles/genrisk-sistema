package com.genrisk.sistema.repository;

import com.genrisk.sistema.model.entity.Genotipificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GenotipificacionRepository extends JpaRepository<Genotipificacion, Integer> {
    List<Genotipificacion> findByPacienteID(String pacienteId);
}
