package com.genrisk.sistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.genrisk.sistema.model.entity.RecuperacionClave;
import java.util.Optional;
import java.time.LocalDateTime;


public interface RecuperacionClaveRepository extends JpaRepository<RecuperacionClave, Integer> {
    Optional<RecuperacionClave> findByClaveMomentanea(String claveMomentanea);

    void deleteByFechaExpiracionBefore(LocalDateTime fecha); // este método elimina los tokens expirados automáticamente
}
