package com.genrisk.sistema.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import  com.genrisk.sistema.model.entity.Muestra;
import com.genrisk.sistema.model.entity.weakEntityKey.muestraID;

@Repository
public interface MuestraRepository extends JpaRepository<Muestra, muestraID> {
    
}
