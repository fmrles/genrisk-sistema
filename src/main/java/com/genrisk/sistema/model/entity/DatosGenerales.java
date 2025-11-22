package com.genrisk.sistema.model.entity;

import com.genrisk.sistema.model.entity.weakEntityKey.DatosGeneralesID;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

@Table(name ="datos_generales")
public class DatosGenerales{

    @EmbeddedId
    private DatosGeneralesID idDatosGen;

    @Column(name = "edad", nullable = true, updatable = true)
    @Min(value = 18, message = "La edad mínima permitida es 18 años") // Validación
    @Max(value = 120, message = "La edad no puede superar los 120 años") // Validación
    private Integer edad;

    @Column(name = "sexo", nullable = true, updatable = true)
    // Validación con expresión regular para asegurar consistencia
    @Pattern(regexp = "^(?i)(Masculino|Femenino)$", message = "El sexo debe ser 'Masculino' o 'Femenino'")
    private String sexo;

    @Column(name = "peso", nullable = true, updatable = true)
    @Min(value = 30, message = "El peso mínimo aceptado es 30 kg")
    @Max(value = 300, message = "El peso máximo aceptado es 300 kg")
    private Double peso;

    @Column(name = "imc", nullable = true, updatable = true)
    private Double imc;

    @Column(name = "estatura", nullable = true, updatable = true)
    @Min(value = 100, message = "La estatura mínima es 100 cm")
    @Max(value = 250, message = "La estatura máxima es 250 cm")
    private Double estatura;

    @Column(name = "zona_residencial", nullable = true, updatable = true)
    private String zonaResidencial;

    @Column(name = "anios_resi_actual", nullable = true, updatable = true)
    @Min(value = 0, message = "Los años de residencia no pueden ser negativos")
    private Integer aniosResiActual;

    @Column(name = "educacion", nullable = true, updatable = true)
    private String educacion;

    @Column(name = "ocupacion", nullable = true, updatable = true)
    private String ocupacion;

    //Método para el cálculo automático del IMC
    // @PrePersist: Se ejecuta antes de crear un nuevo registro (SQL INSERT)
    // @PreUpdate: Se ejecuta antes de actualizar un registro existente (SQL UPDATE)
    @PrePersist
    @PreUpdate
    public void calcularIMC() {
        if (this.peso != null && this.estatura != null && this.estatura > 0) {
            double estaturaMetros = this.estatura / 100.0;
            double imcCalculado = this.peso / (estaturaMetros * estaturaMetros);
            this.imc = Math.round(imcCalculado * 10.0) / 10.0;
        }
    }

}