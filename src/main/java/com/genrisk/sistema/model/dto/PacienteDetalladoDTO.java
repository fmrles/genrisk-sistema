package com.genrisk.sistema.model.dto;

import com.genrisk.sistema.model.entity.*;
import lombok.Data;
import java.util.List;

@Data
public class PacienteDetalladoDTO {
    
    private Paciente paciente;
    private List<Muestra> muestras;
    private List<Genotipificacion> genotipificaciones;
    private List<FormularioDetalladoDTO> formularios;
}
