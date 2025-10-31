package com.genrisk.sistema.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import  com.genrisk.sistema.model.entity.DatosClinicos;
import com.genrisk.sistema.model.entity.weakEntityKey.DatosClinicosID;

@Repository
public interface DatosClinicosRepository extends JpaRepository<DatosClinicos, DatosClinicosID> {
    
}
