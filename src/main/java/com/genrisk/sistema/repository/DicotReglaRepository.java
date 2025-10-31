package com.genrisk.sistema.repository;

import com.genrisk.sistema.model.entity.DicotRegla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DicotReglaRepository extends JpaRepository<DicotRegla, Integer> {
    
}
