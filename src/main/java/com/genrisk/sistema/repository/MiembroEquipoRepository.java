package com.genrisk.sistema.repository;

import com.genrisk.sistema.model.entity.MiembroEquipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MiembroEquipoRepository extends JpaRepository<MiembroEquipo, Integer> {
    Optional<MiembroEquipo> findByCorreoMiembro(String correoMiembro);
}
