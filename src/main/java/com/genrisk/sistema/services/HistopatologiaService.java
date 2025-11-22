package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.Formulario;
import com.genrisk.sistema.model.entity.Histopatologia;
import com.genrisk.sistema.model.entity.Paciente;
import com.genrisk.sistema.model.entity.weakEntityKey.HistopatologiaID;
import com.genrisk.sistema.repository.FormularioRepository;
import com.genrisk.sistema.repository.HistopatologiaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class HistopatologiaService extends CrudServices<Histopatologia, HistopatologiaID> {
    
    private final FormularioRepository formularioRepository;

    public HistopatologiaService(HistopatologiaRepository repo, FormularioRepository formularioRepository) {
        super(repo);
        this.formularioRepository = formularioRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<Histopatologia> createPost(Histopatologia histopatologia) {
        Integer formularioId = histopatologia.getHistoID().getFormularioId();
        Formulario formulario = formularioRepository.findById(formularioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "No se puede crear histopatología: El Formulario ID " + formularioId + " no existe."));
        Paciente paciente = formulario.getPaciente();

        if (paciente != null && "Control".equalsIgnoreCase(paciente.getTipoPaciente())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Error de Negocio: No está permitido registrar Histopatología (cáncer) a un paciente del grupo 'Control'.");
        }
        return super.createPost(histopatologia);
    }
}
