package com.genrisk.sistema.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "miembro_equipo")
@Data
public class MiembroEquipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_miembro")
    private Integer idMiembroEquipo;

    @Column(name = "nombre_miembro")
    private String nombreMiembro;

    @Column(name = "correo_miembro")
    private String correoMiembro;

    @Column(name = "clave")
    private String clave;

    @Column(name = "rol_miembro")
    private String rolMiembro;
}
