package com.genrisk.sistema.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import  com.genrisk.sistema.model.entity.IngresaDatos;
import com.genrisk.sistema.model.entity.weakEntityKey.IngresaDatosID;

@Repository
public interface IngresaDatosRepository extends JpaRepository<IngresaDatos, IngresaDatosID> {
    
}
