package com.genrisk.sistema.services;

import com.genrisk.sistema.model.dto.EstadisticaResultado;
import com.genrisk.sistema.model.entity.DatosClinicos;
import com.genrisk.sistema.model.entity.DatosGenerales;
import com.genrisk.sistema.model.entity.HabitosPaciente;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EstadisticaServiceTest {

    @Mock private DatosGeneralesRepository datosGeneralesRepository;
    @Mock private HabitosPacienteRepository habitosPacienteRepository;
    @Mock private DatosClinicosRepository datosClinicosRepository;
    @Mock private GenotipificacionRepository genotipificacionRepository;

    @InjectMocks
    private EstadisticaService estadisticaService;

    @Test
    public void cuandoCalculaEstadisticas_debeDevolverResultadosCorrectos() {
    
        DatosGenerales d1 = new DatosGenerales(); d1.setEdad(20);
        DatosGenerales d2 = new DatosGenerales(); d2.setEdad(30);
        DatosGenerales d3 = new DatosGenerales(); d3.setEdad(30); // Para la moda
        DatosGenerales d4 = new DatosGenerales(); d4.setEdad(40);
        DatosGenerales d5 = new DatosGenerales(); d5.setEdad(50);
        List<DatosGenerales> datosDePrueba = List.of(d1, d2, d3, d4, d5);
  
        when(datosGeneralesRepository.findAll()).thenReturn(datosDePrueba);

        EstadisticaResultado resultado = estadisticaService.calcularEstadisticas("datos_genericos", "edad");

        assertThat(resultado.getCount()).isEqualTo(5);
        assertThat(resultado.getMedia()).isEqualTo(34.0);
        assertThat(resultado.getMediana()).isEqualTo(30.0);
        assertThat(resultado.getModa()).containsExactly(30.0);
        assertThat(resultado.getMin()).isEqualTo(20.0);
        assertThat(resultado.getMax()).isEqualTo(50.0);
    }
    
    @Test
    public void cuandoNoHayDatos_debeDevolverCountCero() {
        
        when(datosGeneralesRepository.findAll()).thenReturn(Collections.emptyList());
       
        EstadisticaResultado resultado = estadisticaService.calcularEstadisticas("datos_genericos", "edad");
        
        assertThat(resultado.getCount()).isEqualTo(0);
        assertThat(resultado.getMedia()).isNull();
        assertThat(resultado.getMediana()).isNull();
    }
    
    @Test
    public void cuandoConvierteStringANumero_debeFuncionar() {
        
        DatosClinicos datoPrueba = new DatosClinicos();
        datoPrueba.setHpyloriTiempoTest("12");
        
        when(datosClinicosRepository.findAll()).thenReturn(List.of(datoPrueba));
        
        EstadisticaResultado resultado = estadisticaService.calcularEstadisticas("datos_clinicos", "hpyloriTiempoTest");
    
        assertThat(resultado.getCount()).isEqualTo(1);
        assertThat(resultado.getMin()).isEqualTo(12.0);
    }
}
