package com.genrisk.sistema.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity

@NoArgsConstructor
@AllArgsConstructor

@Table(name = "dicot_valor")
public class DicotValor {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dicotvalor")
    private Integer idDicotValor;

    @Column(name = "categoria", nullable = true, updatable = true)
    private String categoria;

    @Column(name = "valordicico")
    private Integer valordicico;

    @Column(name = "formulario_id", insertable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer formularioID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formulario_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Formulario formulario;

    @Column(name = "regladicot_id", insertable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer reglaDicotID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regladicot_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private DicotRegla dicotRegla;
}
