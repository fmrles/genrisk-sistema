package com.genrisk.sistema.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import  com.genrisk.sistema.model.entity.Histopatologia;
import com.genrisk.sistema.model.entity.weakEntityKey.HistopatologiaID;

@Repository
public interface HistopatologiaRepository extends JpaRepository<Histopatologia, HistopatologiaID> {
    
}
