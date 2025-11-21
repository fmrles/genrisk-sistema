package com.genrisk.sistema.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class EstadisticaResultado {
    private String entidad;
    private String atributo;
    private long count; // Cantidad de registros
    private Double media;
    private Double mediana;
    private List<Double> moda;
    private Double min;
    private Double max;
}
