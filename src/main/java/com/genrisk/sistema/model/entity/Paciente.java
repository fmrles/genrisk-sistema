package com.genrisk.sistema.model.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "paciente")
@Data

public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente")
    private Integer idPaciente;

    @Column(name = "nombre_paciente")
    private String nombrePaciente;

    @Column(name = "correo_paciente")
    private String correoPaciente;

    @Column(name = "direccion_paciente")
    private String direccionPaciente;

    @Column(name = "tipo_paciente")
    private String tipoPaciente;
}
