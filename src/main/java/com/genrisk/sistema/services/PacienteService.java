package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.Paciente;
import com.genrisk.sistema.repository.PacienteRepository;
import org.springframework.stereotype.Service;

@Service
public class PacienteService extends CrudServices<Paciente, String> {
    public PacienteService(PacienteRepository repo) {
        super(repo);
    }
    
}
