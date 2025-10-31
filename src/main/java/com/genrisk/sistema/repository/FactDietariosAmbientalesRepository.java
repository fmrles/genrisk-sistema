package com.genrisk.sistema.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import  com.genrisk.sistema.model.entity.FactDietariosAmbientales;
import com.genrisk.sistema.model.entity.weakEntityKey.FactDietariosAmbientalesID;

@Repository
public interface FactDietariosAmbientalesRepository extends JpaRepository<FactDietariosAmbientales, FactDietariosAmbientalesID> {
    
}
