package com.genrisk.sistema.model.dto;

import com.genrisk.sistema.model.entity.*;
import lombok.Data;
import java.util.List;

@Data
public class PacienteDetalladoDTO {
    
    private Object paciente;
    private PacReclutadorDTO paciente2; 
    private List<Muestra> muestras;
    private List<Genotipificacion> genotipificaciones;
    private List<FormularioDetalladoDTO> formularios;
}
