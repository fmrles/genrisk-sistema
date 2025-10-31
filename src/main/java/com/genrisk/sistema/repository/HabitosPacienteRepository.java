package com.genrisk.sistema.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import  com.genrisk.sistema.model.entity.HabitosPaciente;
import com.genrisk.sistema.model.entity.weakEntityKey.HabitosPacienteID;

@Repository
public interface HabitosPacienteRepository extends JpaRepository<HabitosPaciente, HabitosPacienteID> {
    
}