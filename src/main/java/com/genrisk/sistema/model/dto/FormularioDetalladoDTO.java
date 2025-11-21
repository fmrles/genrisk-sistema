package com.genrisk.sistema.model.dto;

import com.genrisk.sistema.model.entity.*;
import lombok.Data;

@Data
public class FormularioDetalladoDTO {
    
    private Formulario formulario;
    private DatosGenerales datosGenerales;
    private DatosClinicos datosClinicos;
    private HabitosPaciente habitosPaciente;
    private FactDietariosAmbientales factDietariosAmbientales;
    private Histopatologia histopatologia;
}
