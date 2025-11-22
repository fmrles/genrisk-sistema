package com.genrisk.sistema.model.entity;

import com.genrisk.sistema.model.entity.weakEntityKey.HistopatologiaID;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Entity

@NoArgsConstructor
@AllArgsConstructor

@Table(name = "histopatologia")
public class Histopatologia {

    @EmbeddedId 
    private HistopatologiaID histoID;

    @Column(name = "tipo", nullable = true, updatable = true)
    @Pattern(regexp = "^(?i)(Intestinal|Difuso|Mixto|Otro.*)$", 
             message = "Tipo histológico inválido (Opciones: Intestinal, Difuso, Mixto, Otro)")
    private String tipo;

    @Column(name = "estado_clinico", nullable = true, updatable = true)
    @NotBlank(message = "El estadio clínico (TNM) es obligatorio para los casos")
    @Size(max = 50, message = "El estadio clínico no debe superar los 50 caracteres")
    private String estadoClinico;

    @Column(name = "locali_tumoral", nullable = true, updatable = true)
    @Pattern(regexp = "^(?i)(Cardias|Cuerpo|Antro|Difuso)$", 
             message = "Localización tumoral inválida (Opciones: Cardias, Cuerpo, Antro, Difuso)")
    private String localiTumoral;
}
