package com.genrisk.sistema.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Data
@Entity

@NoArgsConstructor
@AllArgsConstructor

@Table(name ="genotipificacion")
public class Genotipificacion{

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_genotipificacion")
    private Integer idGenotipificacion;

    @Column(name = "paciente_id")
    private Integer pacienteID;

    @Column(name = "muestra_nro")
    private Integer muestraNRO;

    @Column(name = "fecha_genoti", nullable = true, updatable = true)
    private LocalDate fechaGenoti;

    @Column(name = "estado_genoti", nullable = true, updatable = true)
    private String estadoGenoti;

    @Column(name = "tlr9rs5743836", nullable = true, updatable = true)
    private String var1;

    @Column(name = "miR_146ars2910164", nullable = true, updatable = true)
    private String var2;

    @Column(name = "miR_196a2rs11614913", nullable = true, updatable = true)
    private String var3;

    @Column(name = "MTHFRrs1801133", nullable = true, updatable = true)
    private String var4;

    @Column(name = "DNMT3Brs1569686", nullable = true, updatable = true)
    private String var5;

    @Column(name = "TLR9rs187084", nullable = true, updatable = true)
    private String var6;

}