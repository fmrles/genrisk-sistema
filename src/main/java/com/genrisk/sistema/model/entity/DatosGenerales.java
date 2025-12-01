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
    @NotNull(message = "La edad es obligatoria")
    @Min(value = 18, message = "La edad mínima permitida es 18 años") 
    @Max(value = 120, message = "La edad no puede superar los 120 años")
    private Integer edad;

    @Column(name = "sexo", nullable = true, updatable = true)
    @NotNull(message = "El sexo es obligatorio")
    @Pattern(regexp = "^(?i)(Hombre|Mujer)$", message = "El sexo debe ser 'Hombre' o 'Mujer'")
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
    @Pattern(regexp = "^(?i)(Urbana|Rural)$", message = "Zona debe ser 'Urbana' o 'Rural'")
    private String zonaResidencial;

    @Column(name="anios_resi_actual", nullable = true, updatable = true)
    @Pattern(regexp = "^(<5|5\\s*[-–]\\s*10|>10)$", message = "Años de residencia inválido. Use: <5, 5-10, >10")
    private String aniosResiActual;

    @Column(name = "educacion", nullable = true, updatable = true)
    @Pattern(regexp = "^(?i)(Básico|Medio|Superior)$", message = "Nivel educacional inválido")
    private String educacion;

    @Column(name = "ocupacion", nullable = true, updatable = true)
    private String ocupacion;

    @Column (name = "nacionalidad", nullable = true, updatable = true)
    private String nacionalidad;

    @Column(name = "direccion", nullable = true, updatable = true)
    private String direccion;

    @Column(name = "comuna", nullable = true, updatable = true)
    private String comuna;

    @Column(name = "ciudad", nullable = true, updatable = true)
    private String ciudad;

    @Column(name = "prevision_salud", nullable = true, updatable = true)
    private String previsionSalud;

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

    public Integer getResidenciaUrbana5() {
        boolean esUrbana = "Urbana".equalsIgnoreCase(this.zonaResidencial);
        boolean esRural = "Rural".equalsIgnoreCase(this.zonaResidencial);
        
        boolean masDe5 = false;
        if (this.aniosResiActual != null) {
            if (this.aniosResiActual.contains("5") || this.aniosResiActual.contains(">10")) {
                masDe5 = true; 
            }
        }
        if (esUrbana && masDe5) return 1;
        if (esRural && !masDe5) return 1;
        
        return 0;
    }

    public Integer getResidenciaRural5a() {
        boolean esUrbana = "Urbana".equalsIgnoreCase(this.zonaResidencial);
        boolean esRural = "Rural".equalsIgnoreCase(this.zonaResidencial);
        
        boolean masDe5 = false;
        if (this.aniosResiActual != null) {
            if (this.aniosResiActual.contains("5") || this.aniosResiActual.contains(">10")) {
                masDe5 = true;
            }
        }
        if (esRural && masDe5) return 1;
        if (esUrbana && !masDe5) return 1;
        
        return 0;
    }

}