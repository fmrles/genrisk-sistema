package com.genrisk.sistema.model.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Table (name = "recuperacion_clave")
public class RecuperacionClave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recuperacion", nullable = false, updatable = false)
    private Integer idRecuperacion;

    @Column(name = "clave_momentanea", nullable = false, updatable = true)
    private String claveMomentanea;

    @Column(name = "fecha_expiracion", nullable = false, updatable = false)
    private LocalDateTime fechaExpiracion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "miembro_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private MiembroEquipo miembro;

    public boolean claveExpirada(){
        return LocalDateTime.now().isAfter(fechaExpiracion);
    }
}