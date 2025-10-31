package com.genrisk.sistema.repository;

import com.genrisk.sistema.model.entity.DicotValor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DicotValorRepository extends JpaRepository<DicotValor, Integer> {
    
}
