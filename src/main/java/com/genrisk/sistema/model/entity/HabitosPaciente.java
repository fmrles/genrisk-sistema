package com.genrisk.sistema.model.entity;
import com.genrisk.sistema.model.entity.weakEntityKey.HabitosPacienteID;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity

@NoArgsConstructor
@AllArgsConstructor

@Table(name = "habitos_paciente")
public class HabitosPaciente {
    
    @EmbeddedId
    private HabitosPacienteID idHabPaciente;

    @Column(name = "estado_consumo_tabaco", nullable = true, updatable = true)
    private String estadoConsumoTabaco;

    @Column(name = "edad_inicio_tabaco", nullable = true, updatable = true)
    private Integer edadInicioTabaco;

    @Column(name = "cant_prom_tabaco", nullable = true, updatable = true)
    private Integer cantPromTabaco;

    @Column(name = "tiempo_tabaco", nullable = true, updatable = true)
    private Integer tiempoTabaco;

    @Column(name = "ex_consumidor_tabaco", nullable = true, updatable = true)
    private Integer exConsumidorTabaco;

    @Column(name = "estado_consumo_alcohol", nullable = true, updatable = true)
    private String estadoConsumoAlcohol;

    @Column(name = "frecuencia_alcohol", nullable = true, updatable = true)
    private String frecuenciaAlcohol;

    @Column(name = "cantidad_alcohol", nullable = true, updatable = true)
    private Integer cantidadAlcohol;

    @Column(name = "anios_consumo_alcohol", nullable = true, updatable = true)
    private Integer aniosConsumoAlcohol;

    @Column(name = "ex_consumidor_alcohol", nullable = true, updatable = true)
    private Integer exConsumidorAlcohol;

    @Column(name = "ejercicio", nullable = true, updatable = true)
    private String ejercicio;

    @Column(name = "frecuencia_ejercicio", nullable = true, updatable = true)
    private String frecuenciaEjercicio;
}
