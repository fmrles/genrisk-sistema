package com.genrisk.sistema.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "formulario")
public class Formulario{

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_formulario", nullable = false, updatable = false)
    private Integer idFormulario;

    @Column(name = "estado_formulario", nullable = true, updatable = true)
    private String estadoFormulario;

    @Column(name = "tipo_formulario", nullable = false, updatable = true)
    private String tipoFormulario;

    @Column(name = "fecha_formulario", nullable = true, updatable = true)
    private LocalDate fechaFormulario;

    @Column(name = "paciente_id", insertable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String paciente_id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Paciente paciente;
}
