package com.genrisk.sistema.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "paciente")
@Data
@NoArgsConstructor 
@AllArgsConstructor
public class Paciente {

    @Id
    @Column(name = "id_paciente")
    private String idPaciente;

    @Column(name = "nombre_paciente")
    private String nombrePaciente;

    @Column(name = "correo_paciente")
    private String correoPaciente;

    @Column(name = "direccion_paciente")
    private String direccionPaciente;

    @Column(name = "tipo_paciente")
    private String tipoPaciente;
}
