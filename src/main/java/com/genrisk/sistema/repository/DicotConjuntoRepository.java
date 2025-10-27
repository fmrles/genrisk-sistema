package com.genrisk.sistema.repository;

import com.genrisk.sistema.model.entity.DicotConjunto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DicotConjuntoRepository extends JpaRepository<DicotConjunto, Integer> {
    
}
