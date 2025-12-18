package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.Formulario;
import com.genrisk.sistema.repository.FormularioRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class FormularioService extends CrudServices<Formulario, Integer> {

    public FormularioService(FormularioRepository repo) {
        super(repo);
    }

    public List<Formulario> getFormulariosByPaciente(String pacienteId) {
        return ((FormularioRepository) repository).findByPacienteIdPaciente(pacienteId);
    }

}
