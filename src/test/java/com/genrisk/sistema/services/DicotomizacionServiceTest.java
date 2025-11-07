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

    @InjectMocks
    private DicotomizacionService dicotomizacionService;


    private Formulario formularioPrueba;
    private DicotRegla reglaCuantitativa;
    private DicotRegla reglaCualitativa;
    private DicotRegla reglaInvertida;
    private DatosGenerales datosGeneralesPrueba;
    private HabitosPaciente habitosPacientePrueba;

    @BeforeEach
    void setUp() {
     
        formularioPrueba = new Formulario();
        formularioPrueba.setIdFormulario(1);

        reglaCuantitativa = new DicotRegla();
        reglaCuantitativa.setIdDicotRegla(10);
        reglaCuantitativa.setEntidadObj("datos_genericos");
        reglaCuantitativa.setAtributoObj("edad");           
        reglaCuantitativa.setOperador(">=");
        reglaCuantitativa.setValorInf(50);
        reglaCuantitativa.setValorSiCumple(1); 
        reglaCuantitativa.setValorNoCumple(0); 

        reglaCualitativa = new DicotRegla();
        reglaCualitativa.setIdDicotRegla(20);
        reglaCualitativa.setEntidadObj("datos_genericos");  
        reglaCualitativa.setAtributoObj("zona_residencial");
        reglaCualitativa.setOperador("=");
        reglaCualitativa.setValorCategoria("Urbana");
        reglaCualitativa.setValorSiCumple(1);
        reglaCualitativa.setValorNoCumple(0);

        reglaInvertida = new DicotRegla();
        reglaInvertida.setIdDicotRegla(30);
        reglaInvertida.setEntidadObj("habitos_paciente");     
        reglaInvertida.setAtributoObj("estado_consumo_tabaco");
        reglaInvertida.setOperador("=");
        reglaInvertida.setValorCategoria("Nunca");
        reglaInvertida.setValorSiCumple(0); 
        reglaInvertida.setValorNoCumple(1);

        // --- Entidades de Prueba ---
        
        datosGeneralesPrueba = new DatosGenerales();
        datosGeneralesPrueba.setEdad(60);                 
        datosGeneralesPrueba.setZonaResidencial("Urbana"); 
        

        habitosPacientePrueba = new HabitosPaciente();
        habitosPacientePrueba.setEstadoConsumoTabaco("Nunca"); 
    }

    /**
     * Prueba que una regla cuantitativa (ej. edad >= 50) funcione.
     * También prueba el mapeo de entidad "datos_genericos".
     */
    @Test
    void cuandoEjecutaReglaCuantitativa_yCumple_guardaValorSi() {
     
        when(dicotReglaRepository.findByDicotConjuntoId(1)).thenReturn(List.of(reglaCuantitativa));

        when(formularioRepository.findAll()).thenReturn(List.of(formularioPrueba));
 
        when(datosGeneralesRepository.findByIdDatosGen_FormularioId(1)).thenReturn(Optional.of(datosGeneralesPrueba));
     
        when(dicotValorRepository.save(any(DicotValor.class))).thenAnswer(i -> i.getArgument(0));

        List<DicotValor> resultados = dicotomizacionService.ejecutarDicotomizacion(1);

        assertThat(resultados).hasSize(1);
 
        assertThat(resultados.get(0).getValordicico()).isEqualTo(1); 
        assertThat(resultados.get(0).getFormularioID()).isEqualTo(1);
        assertThat(resultados.get(0).getReglaDicotID()).isEqualTo(10);
    }

    /**
     * Prueba que una regla cualitativa (ej. zona_residencial = "Urbana") funcione.
     * También prueba el mapeo de atributo "zona_residencial" -> "zonaResidencial".
     */
    @Test
    void cuandoEjecutaReglaCualitativa_yMapeoAtributo_guardaValorSi() {

        when(dicotReglaRepository.findByDicotConjuntoId(1)).thenReturn(List.of(reglaCualitativa));
        when(formularioRepository.findAll()).thenReturn(List.of(formularioPrueba));
        when(datosGeneralesRepository.findByIdDatosGen_FormularioId(1)).thenReturn(Optional.of(datosGeneralesPrueba));
        when(dicotValorRepository.save(any(DicotValor.class))).thenAnswer(i -> i.getArgument(0));

        List<DicotValor> resultados = dicotomizacionService.ejecutarDicotomizacion(1);

        assertThat(resultados).hasSize(1);
        assertThat(resultados.get(0).getValordicico()).isEqualTo(1); 
    }

    /**
     * Prueba que la lógica de valores (Si=0, No=1) se respete.
     * También prueba el mapeo de entidad "habitos_paciente" y el atributo "estado_consumo_tabaco".
     */
    @Test
    void cuandoEjecutaReglaLogicaInvertida_yCumple_guardaValorSiInvertido() {
        when(dicotReglaRepository.findByDicotConjuntoId(1)).thenReturn(List.of(reglaInvertida));
        when(formularioRepository.findAll()).thenReturn(List.of(formularioPrueba));
        when(habitosPacienteRepository.findByIdHabPaciente_FormularioId(1)).thenReturn(Optional.of(habitosPacientePrueba));
        when(dicotValorRepository.save(any(DicotValor.class))).thenAnswer(i -> i.getArgument(0));

        List<DicotValor> resultados = dicotomizacionService.ejecutarDicotomizacion(1);


        assertThat(resultados).hasSize(1);

        assertThat(resultados.get(0).getValordicico()).isEqualTo(0); 
    }
    
    /**
     * Prueba que el servicio no falle si un formulario no tiene datos
     * (ej. el formulario 1 no tiene datos en 'habitos_paciente')
     */
    @Test
    void cuandoFormularioNoTieneEntidad_noGuardaValor_yNoFalla() {

        when(dicotReglaRepository.findByDicotConjuntoId(1)).thenReturn(List.of(reglaInvertida));
        when(formularioRepository.findAll()).thenReturn(List.of(formularioPrueba));
        when(habitosPacienteRepository.findByIdHabPaciente_FormularioId(1)).thenReturn(Optional.empty()); 

        List<DicotValor> resultados = dicotomizacionService.ejecutarDicotomizacion(1);

        assertThat(resultados).isEmpty();
    }
}
