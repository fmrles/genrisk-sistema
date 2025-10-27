package com.genrisk.sistema.repository;

import com.genrisk.sistema.model.entity.MiembroEquipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MiembroEquipoRepository extends JpaRepository<MiembroEquipo, Integer> {
    
}
