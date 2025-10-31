package com.genrisk.sistema.model.entity;

import com.genrisk.sistema.model.entity.weakEntityKey.IngresaDatosID;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Data
@Entity

@NoArgsConstructor
@AllArgsConstructor

@Table (name = "ingresa_datos")
public class IngresaDatos {

    @EmbeddedId
    private IngresaDatosID idDatos;

    @Column(name = "formulario_id", nullable = true, updatable = true)
    private Integer formularioID;

    @Column(name = "miembro_id", nullable = true, updatable = true)
    private Integer miembroID;

    @Column(name = "campo")
    private String campo;

    @Column(name = "valor_anterior", nullable = true, updatable = true)
    private String valorAnterior;

    @Column(name = "valor_nuevo", nullable = true, updatable = true)
    private String valorNuevo;

    @Column(name = "fecha_cambio", nullable = true, updatable = true)
    private LocalDate fechaCambio;
    
}