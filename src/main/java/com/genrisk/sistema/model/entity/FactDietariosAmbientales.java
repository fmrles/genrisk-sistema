package com.genrisk.sistema.model.entity;

import com.genrisk.sistema.model.entity.weakEntityKey.FactDietariosAmbientalesID;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity

@NoArgsConstructor
@AllArgsConstructor

@Table(name ="fact_dietario_ambienta")
public class FactDietariosAmbientales{

    @EmbeddedId
    private FactDietariosAmbientalesID idFact;

    @Column(name = "trabajo_zona_rural", nullable = true, updatable = true)
    private String trabajoZonaRural;

    @Column(name = "agua_consumo_rural", nullable = true, updatable = true)
    private String aguaConsumoRural;

    @Column(name = "tratamiento_agua", nullable = true, updatable = true)
    private String tratamientoAgua;

    @Column(name = "fumigaciones", nullable = true, updatable = true)
    private String fumigaciones;

    @Column(name = "exposicion_pesticidas", nullable = true, updatable = true)
    private String exposicionPesticidas;

    @Column(name = "combus_lena_diario", nullable = true, updatable = true)
    private String combusLenaDiario;

    @Column(name = "exposicion_quimicos", nullable = true, updatable = true)
    private String exposicionQuimicos;

    @Column(name = "dieta_agregasal", nullable = true, updatable = true)
    private String dietaAgregaSal;

    @Column(name = "dieta_frutas_verduras", nullable = true, updatable = true)
    private String dietaFrutasVerduras;

    @Column(name = "dieta_frituras", nullable = true, updatable = true)
    private String dietaFrituras;

    @Column(name = "dieta_carnes_cecinas", nullable = true, updatable = true)
    private String dietaCarnesCecinas;

}
