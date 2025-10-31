package com.genrisk.sistema.model.entity;

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

    @Column(name = "paciente_id", nullable = false, updatable = false)
    private Integer paciente_id;    
}
