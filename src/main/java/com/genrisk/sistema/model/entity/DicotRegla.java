package com.genrisk.sistema.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity

@NoArgsConstructor
@AllArgsConstructor

@Table(name = "dicot_regla")
public class DicotRegla {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dicotregla")
    private Integer idDicotRegla;

    @Column(name = "entidad_obj", nullable = true, updatable = true)
    private String entidadObj;

    @Column(name = "atributo_obj", nullable = true, updatable = true)
    private String atributoObj;

    @Column(name = "metodo", nullable = true, updatable = true)
    private String metodo;

    @Column(name = "valor_inf", nullable = true, updatable = true)
    private Integer valorInf;

    @Column(name = "valor_sup", nullable = true, updatable = true)
    private Integer valorSup;

    @Column(name = "operador", nullable = true, updatable = true)
    private String operador;

    @Column(name = "valor_categoria", nullable = true, updatable = true)
    private String valorCategoria;

    @Column(name = "dicotconjunto_id", insertable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer dicotConjuntoID; 
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dicotconjunto_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private DicotConjunto dicotConjunto; 
}