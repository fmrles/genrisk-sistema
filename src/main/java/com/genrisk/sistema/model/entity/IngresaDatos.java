package com.genrisk.sistema.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ingresa_datos")
public class IngresaDatos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ingreso")
    private Integer idIngreso;

    @Column(name = "campo")
    private String campo;

    @Column(name = "valor_anterior", nullable = true, updatable = true)
    private String valorAnterior;

    @Column(name = "valor_nuevo", nullable = true, updatable = true)
    private String valorNuevo;

    @Column(name = "fecha_cambio", nullable = true, updatable = true)
    private LocalDate fechaCambio;
    
    //Relación con Formulario
    @Column(name = "formulario_id", insertable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer formulario_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formulario_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Formulario formulario;

    //Relación con MiembroEquipo
    @Column(name = "miembro_id", insertable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer miembro_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "miembro_id") 
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private MiembroEquipo miembroEquipo;
}