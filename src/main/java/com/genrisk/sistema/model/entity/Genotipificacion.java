package com.genrisk.sistema.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import jakarta.validation.constraints.Pattern;
import com.fasterxml.jackson.annotation.JsonFormat;

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

    @Column(name = "paciente_id", nullable = false)
    private String pacienteID;

    @Column(name = "muestra_nro", nullable = false)
    private Integer muestraNRO;

    @JsonFormat(pattern = "yyy-MM-dd")
    @Column(name = "fecha_genoti", nullable = true, updatable = true)
    private LocalDate fechaGenoti;

    @Column(name = "estado_genoti", nullable = true, updatable = true)
    private String estadoGenoti;

    @Column(name = "tlr9rs5743836", nullable = true, updatable = true)
    @Pattern(regexp = "^(TT|TC|CC)$", message = "Genotipo TLR9-rs5743836 inválido (Solo TT, TC, CC)")
    private String var1;

    @Column(name = "miR_146ars2910164", nullable = true, updatable = true)
    @Pattern(regexp = "^(GG|GC|CC)$", message = "Genotipo miR-146a inválido (Solo GG, GC, CC)")
    private String var2;

    @Column(name = "miR_196a2rs11614913", nullable = true, updatable = true)
    @Pattern(regexp = "^(CC|CT|TT)$", message = "Genotipo miR-196a2 inválido (Solo CC, CT, TT)")
    private String var3;

    @Column(name = "MTHFRrs1801133", nullable = true, updatable = true)
    @Pattern(regexp = "^(CC|CT|TT)$", message = "Genotipo MTHFR inválido (Solo CC, CT, TT)")
    private String var4;

    @Column(name = "DNMT3Brs1569686", nullable = true, updatable = true)
    @Pattern(regexp = "^(GG|GT|TT)$", message = "Genotipo DNMT3B inválido (Solo GG, GT, TT)")
    private String var5;

    @Column(name = "TLR9rs187084", nullable = true, updatable = true)
    @Pattern(regexp = "^(TT|TC|CC)$", message = "Genotipo TLR9-rs5743836 inválido (Solo TT, TC, CC)")
    private String var6;
}