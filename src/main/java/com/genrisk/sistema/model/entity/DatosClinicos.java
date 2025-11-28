package com.genrisk.sistema.model.entity;

import com.genrisk.sistema.model.entity.weakEntityKey.DatosClinicosID;
import jakarta.validation.constraints.*;
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
    @Pattern(regexp = "^(?i)(S[ií]|No)$", message = "Debe responder Sí o No")
    private String adenoGastrico;

    @Column(name = "fecha_adeno_gastrico", nullable = true, updatable = true)
    @PastOrPresent(message = "La fecha de diagnóstico no es valida")
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
    @Pattern(regexp = "^(?i)(Positivo|Negativo)$", message = "Resultado H. pylori inválido")
    private String hpyloriResultado ;

    @Column(name = "hpylori_tiempo_test", nullable = true, updatable = true)
    @Pattern(regexp = "^(<1 año|1–5 años|>5 años)$", message = "Tiempo del test inválido según CRF")
    private String hpyloriTiempoTest ;

    @Column(name = "positivo_pasado_hpylori", nullable = true, updatable = true)
    @Pattern(regexp = "^(?i)(Positivo|Negativo|Desconocido)$", message = "Resultado H. pylori inválido")
    private String positivoPasadoHPylori;

    @Column(name = "tipo_exa_pasado_hpy", nullable = true, updatable = true)
    private String tipoExamenPasadoHPy;

    @Column(name = "anio_positivopasado_hpylori", nullable = true, updatable = true)
    private Integer anioPositivoPasado;

    @Column(name = "trata_erradicacion", nullable = true, updatable = true)
    @Pattern(regexp = "^(?i)(S[ií]|No|No recuerda)$", message = "Opción ingresada errónea")
    private String trataErradicacion;

    @Column(name = "esquema_trataerradica", nullable = true, updatable = true)
    private String esquemaTratamientoErra;

    @Column(name = "anio_trataerradica", nullable = true, updatable = true)
    private Integer anioTrataEradica;

    @Column(name = "antibioticos_ibp", nullable = true, updatable = true)
    @Pattern(regexp = "^(?i)(S[ií]|No|No recuerda)$", message = "Opción ingresada errónea")
    private String antibioticosIBP;

    @Column(name = "repeticion_examen", nullable = true, updatable = true) //HPylori posterior
    @Pattern(regexp = "^(?i)(S[ií]|No)$", message = "Opción ingresada errónea")
    private String repeticionExamen;

    @Column(name = "fecha_repetiexamen", nullable = true, updatable = true)
    @FutureOrPresent(message = "La fecha debe posterior al primer examen")
    private LocalDate fechaRepetiExamen;

    @Column(name = "resultados_examen", nullable = true, updatable = true)
    private String resultadosExamen;

}
