package com.genrisk.sistema.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombreMiembro;

    @Column(name = "correo_miembro")
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Formato de correo inválido")
    private String correoMiembro;

    @Column(name = "clave")
    @NotBlank(message = "La clave es obligatoria")
    @Size(min = 6, message = "La clave debe tener al menos 6 caracteres")
    private String clave;

    @Column(name = "rol_miembro")
    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "^(Investigador|Administrador|Reclutador|Informatico)$", 
             message = "Rol inválido. Roles permitidos: Investigador, Administrador, Reclutador, Informatico")
    private String rolMiembro;
}
