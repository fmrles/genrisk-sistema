package com.genrisk.sistema.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @NotBlank(message = "El nombre del paciente es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombrePaciente;

    @Column(name = "correo_paciente")
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El formato del correo electrónico no es válido")
    private String correoPaciente;

    @Column(name = "direccion_paciente")
    private String direccionPaciente;

    @Column(name = "tipo_paciente")
    @NotBlank(message = "El tipo de paciente es obligatorio")
    @Pattern(regexp = "^(?i)(Caso|Control)$", message = "El tipo de paciente debe ser 'Caso' o 'Control'")
    private String tipoPaciente;
}
