package com.genrisk.sistema.model.entity;

import com.genrisk.sistema.model.entity.weakEntityKey.DatosClinicosID;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Data
@Entity

@NoArgsConstructor
@AllArgsConstructor

@Table(name ="datos_clinicos")
public class DatosClinicos{

    @EmbeddedId
    private DatosClinicosID idDatosCli;

    @Column(name = "adeno_gastrico", nullable = true, updatable = true)
    private String adenoGastrico;

    @Column(name = "fecha_adeno_gastrico", nullable = true, updatable = true)
    private LocalDate fechaAdenoGastrico;

    @Column(name = "ant_fam_cancer_gast", nullable = true, updatable = true)
    private String antFamCancerGast;

    @Column(name = "medicamentos", nullable = true, updatable = true)
    private String medicamentos;

    @Column(name = "otras_enfermedades", nullable = true, updatable = true)
    private String otrasEnfermedades;

    @Column(name = "ant_fam_otro_cancer", nullable = true, updatable = true)
    private String antFamOtroCancer;

    @Column(name = "cirugia_gastrica_previa", nullable = true, updatable = true)
    private String cirugiaGastricaPrevia;

    @Column(name = "hpylori_prueba", nullable = true, updatable = true)
    private String hpyloriPrueba;

    @Column(name = "hpylori_resultado", nullable = true, updatable = true)
    private String hpyloriResultado ;

    @Column(name = "hpylori_tiempo_test", nullable = true, updatable = true)
    private String hpyloriTiempoTest ;
}
