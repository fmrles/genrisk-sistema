package com.genrisk.sistema.repository;

import com.genrisk.sistema.model.entity.DicotValor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DicotValorRepository extends JpaRepository<DicotValor, Integer> {
    List<DicotValor> findByFormularioPacienteIdPaciente(String pacienteId);
    List<DicotValor> findByDicotReglaDicotConjuntoIdDicotConjunto(Integer conjuntoId);
}
