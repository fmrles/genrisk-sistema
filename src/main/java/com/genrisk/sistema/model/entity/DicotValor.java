package com.genrisk.sistema.model.entity;

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

    @Column(name = "formulario_id", nullable = false)
    private Integer formularioID;

    @Column(name = "regladicot_id")
    private Integer reglaDicotID;
}
