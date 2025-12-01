package com.genrisk.sistema.model.entity;

import com.genrisk.sistema.model.entity.weakEntityKey.HabitosPacienteID;
import jakarta.validation.constraints.*;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "habitos_paciente")
public class HabitosPaciente {
    
    @EmbeddedId
    private HabitosPacienteID idHabPaciente;

    @Column(name = "estado_consumo_tabaco", nullable = true, updatable = true)
    @Pattern(regexp = "^(?i)(Nunca fumó|Exfumador|Fumador actual)$", message = "Estado de tabaquismo inválido")
    private String estadoConsumoTabaco;

    @Column(name = "cant_prom_tabaco", nullable = true, updatable = true)
    private Integer cantPromTabaco;

    @Column(name = "tiempo_tabaco", nullable = true, updatable = true)
    private String tiempoTabaco;

    @Column(name = "ex_consumidor_tabaco", nullable = true, updatable = true)
    private Integer exConsumidorTabaco;

    @Column(name = "estado_consumo_alcohol", nullable = true, updatable = true)
    private String estadoConsumoAlcohol;

    @Column(name = "frecuencia_alcohol", nullable = true, updatable = true)
    private String frecuenciaAlcohol;

    @Column(name = "cantidad_alcohol", nullable = true, updatable = true)
    private Integer cantidadAlcohol;

    @Column(name = "anios_consumo_alcohol", nullable = true, updatable = true)
    @PositiveOrZero(message = "Años de consumo no pueden ser negativos")
    private Integer aniosConsumoAlcohol;

    @Column(name = "ex_consumidor_alcohol", nullable = true, updatable = true)
    private Integer exConsumidorAlcohol;

    // --- TABACO: Carga Moderada ---
    public Integer getTabacoModeradaCarga() {
        boolean fumaActual = "Fumador actual".equalsIgnoreCase(this.estadoConsumoTabaco);
        boolean exFumador = "Exfumador".equalsIgnoreCase(this.estadoConsumoTabaco);
        int cant = (cantPromTabaco != null) ? cantPromTabaco : 0;
        int aniosFuma = parsearAnios(this.tiempoTabaco);
        int aniosDejo = (exConsumidorTabaco != null) ? exConsumidorTabaco : 999;

        // Condición 1: Riesgo alto (Categoría 1)
        // - Fuma actual >= 20 cig/dia
        // - O Fuma 1-19 cig/dia POR >= 10 años
        // - O Dejó hace < 10 años
        if (fumaActual && cant >= 20) return 1;
        if (fumaActual && cant >= 1 && aniosFuma >= 10) return 1;
        if (exFumador && aniosDejo < 10) return 1;

        return 0; 
    }

    // --- TABACO: Carga Grave ---
    public Integer getTabacoGraveCarga() {
        boolean fumaActual = "Fumador actual".equalsIgnoreCase(this.estadoConsumoTabaco);
        boolean exFumador = "Exfumador".equalsIgnoreCase(this.estadoConsumoTabaco);
        int cant = (cantPromTabaco != null) ? cantPromTabaco : 0;
        int aniosFuma = parsearAnios(this.tiempoTabaco);
        int aniosDejo = (exConsumidorTabaco != null) ? exConsumidorTabaco : 999;

        // Categoría 1 (Riesgo):
        // - Fuma actual >= 10 cig/dia POR >= 10 años
        // - O Fuma actual >= 20 cig/dia
        // - O Dejó hace < 10 años
        if (fumaActual && cant >= 10 && aniosFuma >= 10) return 1;
        if (fumaActual && cant >= 20) return 1;
        if (exFumador && aniosDejo < 10) return 1;

        return 0;
    }

    // --- ALCOHOL: Carga Moderada ---
    public Integer getAlcoholModeradaCarga() {
        boolean bebeActual = "Consumidor actual".equalsIgnoreCase(this.estadoConsumoAlcohol);
        boolean exBebedor = "Exconsumidor".equalsIgnoreCase(this.estadoConsumoAlcohol);
        int tragos = (cantidadAlcohol != null) ? cantidadAlcohol : 0;
        int aniosBebe = (aniosConsumoAlcohol != null) ? aniosConsumoAlcohol : 0;
        int aniosDejo = (exConsumidorAlcohol != null) ? exConsumidorAlcohol : 999;
        boolean frecuenciaAlta = "Frecuente".equalsIgnoreCase(frecuenciaAlcohol); 

        // Categoría 1 (Riesgo):
        // - Frecuencia >= 4 veces/sem ("Frecuente")
        // - O >= 5 tragos por ocasión
        // - O >= 10 años bebiendo
        // - O Dejó hace < 10 años
        if (bebeActual && frecuenciaAlta) return 1;
        if (bebeActual && tragos >= 5) return 1;
        if (bebeActual && aniosBebe >= 10) return 1;
        if (exBebedor && aniosDejo < 10) return 1;

        return 0;
    }

    // Helper para leer el string de años del CRF (ej: "10–20 años" -> devuelve 10)
    private int parsearAnios(String texto) {
        if (texto == null) return 0;
        if (texto.contains(">20")) return 21;
        if (texto.contains("10–20") || texto.contains("10-20")) return 10; 
        if (texto.matches("\\d+")) return Integer.parseInt(texto); 
        return 0;
    }
}
