package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.Paciente;
import com.genrisk.sistema.repository.PacienteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class PacienteService extends CrudServices<Paciente, String> {
    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository repo) {
        super(repo);
        this.pacienteRepository = repo;
    }

   @Override
    @Transactional
    public ResponseEntity<Paciente> createPost(Paciente paciente) {
        
        String tipo = paciente.getTipoPaciente();
        if (tipo == null || tipo.isBlank()) {
            // No se puede generar un ID sin un tipo.
            return ResponseEntity.badRequest().body(null);
        }

        String prefijo;
        if ("Caso".equalsIgnoreCase(tipo)) {
            prefijo = "CA";
        } else if ("Control".equalsIgnoreCase(tipo)) {
            prefijo = "CR";
        } else {
            // El tipo no es ni "Caso" ni "Control"
            return ResponseEntity.badRequest().body(null); 
        }

        Optional<Paciente> ultimoPaciente = pacienteRepository.findTopByIdPacienteStartingWithOrderByIdPacienteDesc(prefijo);

        int nuevoNumero = 1;
        if (ultimoPaciente.isPresent()) {
            String ultimoId = ultimoPaciente.get().getIdPaciente();
            String numeroStr = ultimoId.substring(prefijo.length()); 
            
            try {
                int ultimoNumero = Integer.parseInt(numeroStr);
                nuevoNumero = ultimoNumero + 1;
            } catch (NumberFormatException e) {
                return ResponseEntity.internalServerError().build();
            }
        }

        
        String nuevoIdFormateado = String.format("%s%04d", prefijo, nuevoNumero); 

        paciente.setIdPaciente(nuevoIdFormateado);

        Paciente pacienteGuardado = pacienteRepository.save(paciente);
        
        return ResponseEntity.status(201).body(pacienteGuardado);
    }
    
}
