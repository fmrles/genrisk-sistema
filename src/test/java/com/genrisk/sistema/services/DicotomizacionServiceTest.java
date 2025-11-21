package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.*;
import com.genrisk.sistema.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DicotomizacionServiceTest {

    @Mock private DicotReglaRepository dicotReglaRepository;
    @Mock private DicotValorRepository dicotValorRepository;
    @Mock private FormularioRepository formularioRepository;
    @Mock private DatosGeneralesRepository datosGeneralesRepository;
    @Mock private HabitosPacienteRepository habitosPacienteRepository;
    @Mock private DatosClinicosRepository datosClinicosRepository;
    @Mock private FactDietariosAmbientalesRepository factDietariosAmbientalesRepository;
    @Mock private HistopatologiaRepository histopatologiaRepository;

    @InjectMocks
    private DicotomizacionService dicotomizacionService;

    private Paciente paciente1;
    private Formulario formulario101;
    private DatosGenerales datosGenerales101;
    private HabitosPaciente habitos101;
    private DicotRegla reglaEdad;
    private DicotRegla reglaSexo;
    private DicotRegla reglaSexoInversa;
    private DicotRegla reglaTraductor;

    @BeforeEach
    void setUp() {

        paciente1 = new Paciente("pac1", "Paciente 1", "p1@t.cl", "dir1", "Caso");
        formulario101 = new Formulario(101, "Activo", "Caso", null, "pac1", paciente1);
        
        datosGenerales101 = new DatosGenerales();
        datosGenerales101.setEdad(55);
        datosGenerales101.setSexo("Femenino");
        datosGenerales101.setZonaResidencial("Urbana"); 

        habitos101 = new HabitosPaciente();
        habitos101.setEstadoConsumoTabaco("Fumador"); 

        reglaEdad = new DicotRegla();
        reglaEdad.setEntidadObj("datos_genericos");
        reglaEdad.setAtributoObj("edad");
        reglaEdad.setOperador(">=");
        reglaEdad.setValorInf(50);
        reglaEdad.setValorSiCumple(1);
        reglaEdad.setValorNoCumple(0);

        reglaSexo = new DicotRegla();
        reglaSexo.setEntidadObj("datos_genericos");
        reglaSexo.setAtributoObj("sexo");
        reglaSexo.setOperador("=");
        reglaSexo.setValorCategoria("Femenino");
        reglaSexo.setValorSiCumple(1);
        reglaSexo.setValorNoCumple(0);

        reglaSexoInversa = new DicotRegla();
        reglaSexoInversa.setEntidadObj("datos_genericos");
        reglaSexoInversa.setAtributoObj("sexo");
        reglaSexoInversa.setOperador("=");
        reglaSexoInversa.setValorCategoria("Femenino");
        reglaSexoInversa.setValorSiCumple(0);
        reglaSexoInversa.setValorNoCumple(1);
        
        reglaTraductor = new DicotRegla();
        reglaTraductor.setEntidadObj("habitos_paciente");
        reglaTraductor.setAtributoObj("estado_cons_tabaco");
        reglaTraductor.setOperador("=");
        reglaTraductor.setValorCategoria("Fumador");
        reglaTraductor.setValorSiCumple(1);
        reglaTraductor.setValorNoCumple(0);
    }

    //
    @Test
    public void cuandoSeEjecutaElFlujoCompleto_DebeGuardarValoresCorrectos() {

        when(dicotReglaRepository.findByDicotConjuntoId(1))
                .thenReturn(List.of(reglaEdad, reglaSexo));

        when(formularioRepository.findAll()).thenReturn(List.of(formulario101));

        when(datosGeneralesRepository.findByIdDatosGen_FormularioId(101))
                .thenReturn(Optional.of(datosGenerales101));

        when(dicotValorRepository.save(any(DicotValor.class))).thenAnswer(i -> i.getArgument(0));

        List<DicotValor> resultados = dicotomizacionService.ejecutarDicotomizacion(1);

        assertThat(resultados).hasSize(2);

        assertThat(resultados.get(0).getValordicico()).isEqualTo(1);
        assertThat(resultados.get(0).getCategoria()).isEqualTo("edad");

        assertThat(resultados.get(1).getValordicico()).isEqualTo(1);
        assertThat(resultados.get(1).getCategoria()).isEqualTo("sexo");
      
        verify(dicotValorRepository,
                org.mockito.Mockito.times(2)).save(any(DicotValor.class));
    }

    // Pruebas para la regla inversa
    @Test
    public void cuandoSeUsaReglaInversa_DebeDevolverValoresInversos() {
  
        when(dicotReglaRepository.findByDicotConjuntoId(1))
                .thenReturn(List.of(reglaSexoInversa)); 
        when(formularioRepository.findAll()).thenReturn(List.of(formulario101));
        when(datosGeneralesRepository.findByIdDatosGen_FormularioId(101))
                .thenReturn(Optional.of(datosGenerales101));
        when(dicotValorRepository.save(any(DicotValor.class))).thenAnswer(i -> i.getArgument(0));

        List<DicotValor> resultados = dicotomizacionService.ejecutarDicotomizacion(1);

        assertThat(resultados).hasSize(1);

        assertThat(resultados.get(0).getValordicico()).isEqualTo(0);
    }
}
