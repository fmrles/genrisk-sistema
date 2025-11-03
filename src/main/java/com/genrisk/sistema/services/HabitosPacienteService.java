package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.HabitosPaciente;
import com.genrisk.sistema.model.entity.weakEntityKey.HabitosPacienteID;
import com.genrisk.sistema.repository.HabitosPacienteRepository;
import org.springframework.stereotype.Service;

@Service
public class HabitosPacienteService extends CrudServices<HabitosPaciente, HabitosPacienteID> {
    public HabitosPacienteService(HabitosPacienteRepository repo) {
        super(repo);
    }
}