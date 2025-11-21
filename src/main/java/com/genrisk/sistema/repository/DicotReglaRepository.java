package com.genrisk.sistema.repository;

import com.genrisk.sistema.model.entity.DicotRegla;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DicotReglaRepository extends JpaRepository<DicotRegla, Integer> {
    
    /**
     * Consulta que encuentra todas las DicotRegla que pertenecen a un DicotConjunto específico.
     * JPQL: Selecciona las reglas (r) donde el ID del objeto DicotConjunto (r.dicotConjunto.idDicotConjunto)
     * coincide con el ID proporcionado.
     */
    @Query("SELECT r FROM DicotRegla r WHERE r.dicotConjunto.idDicotConjunto = :conjuntoId")
    List<DicotRegla> findByDicotConjuntoId(@Param("conjuntoId") Integer conjuntoId); 
}
