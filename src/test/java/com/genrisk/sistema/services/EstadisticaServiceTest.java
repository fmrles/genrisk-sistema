package com.genrisk.sistema.services;

import com.genrisk.sistema.model.dto.EstadisticaResultado;
import com.genrisk.sistema.model.entity.DatosGenerales;
import com.genrisk.sistema.model.entity.DatosClinicos;
import com.genrisk.sistema.repository.DatosClinicosRepository;
import com.genrisk.sistema.repository.DatosGeneralesRepository;
import com.genrisk.sistema.repository.GenotipificacionRepository;
import com.genrisk.sistema.repository.HabitosPacienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EstadisticaServiceTest {

    @Mock private DatosGeneralesRepository datosGeneralesRepository;
    @Mock private HabitosPacienteRepository habitosPacienteRepository;
    @Mock private DatosClinicosRepository datosClinicosRepository;
    @Mock private GenotipificacionRepository genotipificacionRepository;

    @InjectMocks
    private EstadisticaService estadisticaService;

    /**
     * Prueba los cálculos matemáticos (media, mediana, min, max, count)
     * para una lista simple de números.
     */
    @Test
    void cuandoCalculaEstadisticas_conDatosNumericos_devuelveCalculosCorrectos() {
        DatosGenerales d1 = new DatosGenerales();
        d1.setEdad(10);
        DatosGenerales d2 = new DatosGenerales();
        d2.setEdad(20);
        DatosGenerales d3 = new DatosGenerales();
        d3.setEdad(30);

        when(datosGeneralesRepository.findAll()).thenReturn(List.of(d1, d2, d3));

        EstadisticaResultado resultado = estadisticaService.calcularEstadisticas("datos_genericos", "edad");

        assertThat(resultado.getCount()).isEqualTo(3);
        assertThat(resultado.getMin()).isEqualTo(10.0);
        assertThat(resultado.getMax()).isEqualTo(30.0);
        assertThat(resultado.getMedia()).isEqualTo(20.0); 
        assertThat(resultado.getMediana()).isEqualTo(20.0); 
    }

    /**
     * Prueba el cálculo de la Moda.
     */
    @Test
    void cuandoCalculaEstadisticas_conModa_devuelveModaCorrecta() {

        DatosGenerales d1 = new DatosGenerales(); d1.setEdad(10);
        DatosGenerales d2 = new DatosGenerales(); d2.setEdad(20);
        DatosGenerales d3 = new DatosGenerales(); d3.setEdad(20); // Moda
        DatosGenerales d4 = new DatosGenerales(); d4.setEdad(30);

        when(datosGeneralesRepository.findAll()).thenReturn(List.of(d1, d2, d3, d4));

        EstadisticaResultado resultado = estadisticaService.calcularEstadisticas("datos_genericos", "edad");

        assertThat(resultado.getCount()).isEqualTo(4);
        assertThat(resultado.getMedia()).isEqualTo(20.0); // (10+20+20+30) / 4 = 20
        assertThat(resultado.getMediana()).isEqualTo(20.0); // (20+20) / 2 = 20
        // La moda es 20
        assertThat(resultado.getModa()).hasSize(1);
        assertThat(resultado.getModa().get(0)).isEqualTo(20.0);
    }
    
    /**
     * Prueba que el servicio no falle si la lista de valores está vacía.
     * Debe devolver count: 0.
     */
    @Test
    void cuandoCalculaEstadisticas_conListaVacia_devuelveCountCero() {
        when(datosGeneralesRepository.findAll()).thenReturn(Collections.emptyList());

        EstadisticaResultado resultado = estadisticaService.calcularEstadisticas("datos_genericos", "edad");

        assertThat(resultado).isNotNull();
        // Debe reportar 0
        assertThat(resultado.getCount()).isEqualTo(0);
        assertThat(resultado.getMin()).isNull();
        assertThat(resultado.getMedia()).isNull();
        assertThat(resultado.getMediana()).isNull();
    }

    /**
     * Prueba que el mapeo de snake_case a camelCase funciona.
     * Usamos "anios_resi_actual" -> "aniosResiActual"
     */
    @Test
    void cuandoUsaAtributoSnakeCase_elMapeoFunciona() {
        DatosGenerales d1 = new DatosGenerales();
        d1.setAniosResiActual(5); 

        when(datosGeneralesRepository.findAll()).thenReturn(List.of(d1));

        EstadisticaResultado resultado = estadisticaService.calcularEstadisticas("datos_genericos", "anios_resi_actual");

        assertThat(resultado.getCount()).isEqualTo(1);
        assertThat(resultado.getMedia()).isEqualTo(5.0);
    }

    /**
     * Prueba que el método 'agregarValorSiEsNumero' (probado indirectamente)
     * puede convertir un String a Double.
     */
    @Test
    void cuandoElAtributoEsStringNumerico_loConvierteADouble() {

        DatosClinicos d1 = new DatosClinicos();
        d1.setHpyloriTiempoTest("10.5"); // Un String
        DatosClinicos d2 = new DatosClinicos();
        d2.setHpyloriTiempoTest("N/A"); // Un String no numérico (debe ser ignorado)
        DatosClinicos d3 = new DatosClinicos();
        d3.setHpyloriTiempoTest(null); // Un null (debe ser ignorado)

        when(datosClinicosRepository.findAll()).thenReturn(List.of(d1, d2, d3));

        EstadisticaResultado resultado = estadisticaService.calcularEstadisticas("datos_clinicos", "hpyloriTiempoTest");

        // Solo debe contar el "10.5"
        assertThat(resultado.getCount()).isEqualTo(1);
        assertThat(resultado.getMedia()).isEqualTo(10.5);
    }

    /**
     * Prueba que el servicio lanza IllegalArgumentException 
     * si la entidad no es soportada.
     */
    @Test
    void cuandoEntidadNoEsSoportada_lanzaIllegalArgumentException() {
        String entidadMala = "entidad_desconocida";
        String atributo = "edad";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            estadisticaService.calcularEstadisticas(entidadMala, atributo); 
        });

        String mensajeEsperado = "Entidad no soportada para estadísticas: " + entidadMala;
        String mensajeReal = exception.getMessage();

        assertThat(mensajeReal).isEqualTo(mensajeEsperado);
    }
}
