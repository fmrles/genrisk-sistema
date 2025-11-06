package com.genrisk.sistema.services;

import com.genrisk.sistema.model.dto.FormularioDetalladoDTO;
import com.genrisk.sistema.model.dto.PacReclutadorDTO;
import com.genrisk.sistema.model.dto.PacienteDetalladoDTO;
import com.genrisk.sistema.model.entity.*;
import com.genrisk.sistema.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PacienteDetalladoService {

    @Autowired private PacienteRepository pacienteRepository;
    @Autowired private FormularioRepository formularioRepository;
    @Autowired private MuestraRepository muestraRepository;
    @Autowired private GenotipificacionRepository genotipificacionRepository;
    @Autowired private DatosGeneralesRepository datosGeneralesRepository;
    @Autowired private DatosClinicosRepository datosClinicosRepository;
    @Autowired private HabitosPacienteRepository habitosPacienteRepository;
    @Autowired private FactDietariosAmbientalesRepository factDietariosAmbientalesRepository;
    @Autowired private HistopatologiaRepository histopatologiaRepository;

    @Transactional(readOnly = true) 
    public PacienteDetalladoDTO obtenerDatosCompletosPaciente(String pacienteId) {
        
        // 1. Obtenengo Paciente (o fallar si no existe)
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + pacienteId));

        // 2. Obtenengo listas de Muestras y Genotipificaciones
        List<Muestra> muestras = muestraRepository.findByIdPacienteId(pacienteId);
        List<Genotipificacion> genotipificaciones = genotipificacionRepository.findByPacienteID(pacienteId);

        // 3. Obtenengo sus Formularios
        List<Formulario> formularios = formularioRepository.findByPacienteIdPaciente(pacienteId);

        // 4. Para cada formulario, obtenengo todos sus datos
        List<FormularioDetalladoDTO> formulariosDetallados = formularios.stream().map(form -> {
            FormularioDetalladoDTO dto = new FormularioDetalladoDTO();
            dto.setFormulario(form);
            
            Integer formId = form.getIdFormulario();

            datosGeneralesRepository.findByIdDatosGen_FormularioId(formId).ifPresent(dto::setDatosGenerales);
            datosClinicosRepository.findByIdDatosCli_FormularioId(formId).ifPresent(dto::setDatosClinicos);
            habitosPacienteRepository.findByIdHabPaciente_FormularioId(formId).ifPresent(dto::setHabitosPaciente);
            factDietariosAmbientalesRepository.findByIdFact_FormularioId(formId).ifPresent(dto::setFactDietariosAmbientales);
            histopatologiaRepository.findByHistoID_FormularioId(formId).ifPresent(dto::setHistopatologia);
            
            return dto;
        }).collect(Collectors.toList());

        // 5. Armo el DTO final
        PacienteDetalladoDTO dtoFinal = new PacienteDetalladoDTO();
        dtoFinal.setPaciente(paciente);
        dtoFinal.setMuestras(muestras);
        dtoFinal.setGenotipificaciones(genotipificaciones);
        dtoFinal.setFormularios(formulariosDetallados);

        return dtoFinal;
    }


    @Transactional(readOnly = true) 
    public PacienteDetalladoDTO obtenerDatosPacienteReclutador(String pacienteId){


        Paciente paciente2= pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + pacienteId));
        
        // 2. Obtenengo listas de Muestras y Genotipificaciones
        List<Muestra> muestras = muestraRepository.findByIdPacienteId(pacienteId);
        List<Genotipificacion> genotipificaciones = genotipificacionRepository.findByPacienteID(pacienteId);

        // 3. Obtenengo sus Formularios
        List<Formulario> formularios = formularioRepository.findByPacienteIdPaciente(pacienteId);

        // 4. Para cada formulario, obtenengo todos sus datos
        List<FormularioDetalladoDTO> formulariosDetallados = formularios.stream().map(form -> {
            FormularioDetalladoDTO dto = new FormularioDetalladoDTO();
            dto.setFormulario(form);
            
            Integer formId = form.getIdFormulario();

            datosGeneralesRepository.findByIdDatosGen_FormularioId(formId).ifPresent(dto::setDatosGenerales);
            datosClinicosRepository.findByIdDatosCli_FormularioId(formId).ifPresent(dto::setDatosClinicos);
            habitosPacienteRepository.findByIdHabPaciente_FormularioId(formId).ifPresent(dto::setHabitosPaciente);
            factDietariosAmbientalesRepository.findByIdFact_FormularioId(formId).ifPresent(dto::setFactDietariosAmbientales);
            histopatologiaRepository.findByHistoID_FormularioId(formId).ifPresent(dto::setHistopatologia);
            
            return dto;
        }).collect(Collectors.toList());

        // 5. Armo el DTO final
        PacReclutadorDTO pacienteDTO = new PacReclutadorDTO(paciente2.getIdPaciente(), paciente2.getTipoPaciente());

        // 6. Armo el DTO final
        PacienteDetalladoDTO dtoFinal2 = new PacienteDetalladoDTO();
        dtoFinal2.setPaciente(pacienteDTO);  
        dtoFinal2.setMuestras(muestras);
        dtoFinal2.setGenotipificaciones(genotipificaciones);
        dtoFinal2.setFormularios(formulariosDetallados);
    
        return dtoFinal2;
    }
}