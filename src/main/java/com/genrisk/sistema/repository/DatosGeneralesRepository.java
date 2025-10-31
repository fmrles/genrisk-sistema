package com.genrisk.sistema.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import  com.genrisk.sistema.model.entity.DatosGenericos;
import com.genrisk.sistema.model.entity.weakEntityKey.DatosGeneralesID;

@Repository
public interface DatosGeneralesRepository extends JpaRepository<DatosGenericos, DatosGeneralesID> {
    
}
