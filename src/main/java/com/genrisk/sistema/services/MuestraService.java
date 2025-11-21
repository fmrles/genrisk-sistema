package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.Muestra;
import com.genrisk.sistema.model.entity.weakEntityKey.muestraID;
import com.genrisk.sistema.repository.MuestraRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class MuestraService extends CrudServices<Muestra, muestraID> {

    private final MuestraRepository muestraRepository;

    public MuestraService(MuestraRepository repo) {
        super(repo);
        this.muestraRepository = repo;
    }

    @Override
    @Transactional
    public ResponseEntity<Muestra> createPost(Muestra muestra) {
        
        // 1. Extraer el ID del paciente del objeto EmbeddedId
        String pacienteId = muestra.getId().getPacienteId();

        if (pacienteId == null || pacienteId.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        // 2. Encontrar la muestra con el número más alto para este paciente
        Optional<Muestra> ultimaMuestra = muestraRepository.findTopByIdPacienteIdOrderByIdNroMuestraDesc(pacienteId);

        int nuevoNroMuestra = 1;
        if (ultimaMuestra.isPresent()) {
            // 3. Si existe, incrementar el número de muestra
            nuevoNroMuestra = ultimaMuestra.get().getId().getNroMuestra() + 1;
        }

        // 4. Asignar el nuevo número al objeto EmbeddedId
        muestraID newId = new muestraID(pacienteId, nuevoNroMuestra);
        muestra.setId(newId); // Sobreescribe el ID con el número generado

        // 5. Guardar la entidad (usando el repositorio)
        Muestra muestraGuardada = muestraRepository.save(muestra);
        return ResponseEntity.status(201).body(muestraGuardada);
    }
}
