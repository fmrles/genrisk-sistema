package com.genrisk.sistema.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "dicot_conjunto")
@Data
public class DicotConjunto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dicotconjunto")
    private Integer idDicotConjunto;

    @Column(name = "nombre_dicot_conjunto")
    private String nombreDicotConjunto;

    @Column(name = "descripcion_dicot_conjunto")
    private String descripcionDicotConjunto;
}