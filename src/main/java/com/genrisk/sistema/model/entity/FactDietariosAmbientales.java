package com.genrisk.sistema.model.entity;

import com.genrisk.sistema.model.entity.weakEntityKey.FactDietariosAmbientalesID;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Entity

@NoArgsConstructor
@AllArgsConstructor

@Table(name ="fact_dietario_ambiental")
public class FactDietariosAmbientales{

    @EmbeddedId
    private FactDietariosAmbientalesID idFact;

    @Column(name = "agua_consumo_zona", nullable = true, updatable = true)
    @Pattern(regexp = "^(?i)(Red pública|Pozo|Camión aljibe|Otra.*)$", 
             message = "Fuente de agua inválida (Opciones: Red pública, Pozo, Camión aljibe, Otra)")
    private String aguaConsumoZona;

    @Column(name = "tratamiento_agua", nullable = true, updatable = true)
    @Pattern(regexp = "^(?i)(Ninguno|Hervir|Filtro|Cloro)$", 
             message = "Tratamiento de agua inválido (Opciones: Ninguno, Hervir, Filtro, Cloro)")
    private String tratamientoAgua;

    @Column(name = "fumigaciones", nullable = true, updatable = true)
    @Pattern(regexp = "^(?i)(Nunca|Ocasional|Frecuente)$", 
             message = "Fumigaciones debe ser Nunca, Ocasional o Frecuente")
    private String fumigaciones;

    @Column(name = "exposicion_pesticidas", nullable = true, updatable = true)
    @Pattern(regexp = "^(?i)(S[ií]|No)$", message = "Exposición pesticidas debe ser Sí o No")
    private String exposicionPesticidas;

    @Column(name = "combus_lena_diario", nullable = true, updatable = true)
    @Pattern(regexp = "^(?i)(Nunca/Rara vez|Estacional|Diario)$", 
             message = "Humo de leña inválido (Opciones: Nunca/Rara vez, Estacional, Diario)")
    private String combusLenaDiario;

    @Column(name = "exposicion_quimicos", nullable = true, updatable = true)
    @Pattern(regexp = "^(?i)(S[ií]|No)$", message = "Exposición químicos debe ser Sí o No")
    private String exposicionQuimicos;

    @Column(name = "dieta_agregasal", nullable = true, updatable = true)
    @Pattern(regexp = "^(?i)(S[ií]|No)$", message = "Dieta agrega sal debe ser Sí o No")
    private String dietaAgregaSal;

    @Column(name = "dieta_frutas_verduras", nullable = true, updatable = true)
    @Pattern(regexp = "^(≥5 porciones/día|3–4 porciones/día|≤2 porciones/día)$", 
             message = "Porciones frutas/verduras inválido")
    private String dietaFrutasVerduras;

    @Column(name = "dieta_frituras", nullable = true, updatable = true)
    @Pattern(regexp = "^(?i)(S[ií]|No)$", message = "Consumo frituras debe ser Sí o No")
    private String dietaFrituras;

    @Column(name = "dieta_carnes_cecinas", nullable = true, updatable = true)
    @Pattern(regexp = "^(<1/sem|1–2/sem|≥3/sem)$", 
             message = "Consumo carnes/cecinas inválido (<1/sem, 1–2/sem, ≥3/sem)")
    private String dietaCarnesCecinas;

    @Column(name = "ali_condimentado", nullable = true, updatable = true)
    @Pattern(regexp = "^(Casi Nunca/Rara vez|1 a 2 veces por semana|3 o más veces por semana)", 
                        message = "Alimentos condimentados inválido")
    private String aliCondimentado;

    @Column(name = "infusiones_bebidas", nullable = true, updatable = true)
    @Pattern(regexp = "^(Nunca/Rara vez|1-2/sem|≥3/sem)", 
                        message = "Infusiones y bebidas inválido")
    private String infusionesBebidas;
}