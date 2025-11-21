package com.genrisk.sistema.repository;

import com.genrisk.sistema.model.entity.Muestra;
import com.genrisk.sistema.model.entity.weakEntityKey.muestraID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MuestraRepository extends JpaRepository<Muestra, muestraID> {
    List<Muestra> findByIdPacienteId(String pacienteId);
    Optional<Muestra> findTopByIdPacienteIdOrderByIdNroMuestraDesc(String pacienteId);
}
