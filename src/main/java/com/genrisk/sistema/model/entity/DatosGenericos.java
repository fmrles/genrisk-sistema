package com.genrisk.sistema.model.entity;

import com.genrisk.sistema.model.entity.weakEntityKey.DatosGeneralesID;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity

@NoArgsConstructor
@AllArgsConstructor

@Table(name ="datos_generales")
public class DatosGenericos{

    @EmbeddedId
    private DatosGeneralesID idDatosGen;

    @Column(name = "edad", nullable = true, updatable = true)
    private Integer edad;

    @Column(name = "sexo", nullable = true, updatable = true)
    private String sexo;

    @Column(name = "peso", nullable = true, updatable = true)
    private Double peso;

    @Column(name = "imc", nullable = true, updatable = true)
    private Double imc;

    @Column(name = "estatura", nullable = true, updatable = true)
    private Double estatura;

    @Column(name = "zona_residencial", nullable = true, updatable = true)
    private String zonaResidencial;

    @Column(name = "anios_resi_actual", nullable = true, updatable = true)
    private Integer aniosResiActual;

    @Column(name = "educacion", nullable = true, updatable = true)
    private String educacion;

    @Column(name = "ocupacion", nullable = true, updatable = true)
    private String ocupacion;

}