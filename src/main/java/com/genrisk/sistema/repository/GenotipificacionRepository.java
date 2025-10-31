package com.genrisk.sistema.repository;

import com.genrisk.sistema.model.entity.Genotipificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenotipificacionRepository extends JpaRepository<Genotipificacion, Integer> {
    
}
