package com.genrisk.sistema.services;

import com.genrisk.sistema.model.dto.EstadisticaResultado;
import com.genrisk.sistema.model.entity.DatosClinicos;
import com.genrisk.sistema.model.entity.DatosGenerales;
import com.genrisk.sistema.model.entity.Genotipificacion;
import com.genrisk.sistema.model.entity.HabitosPaciente;
import com.genrisk.sistema.repository.DatosClinicosRepository;
import com.genrisk.sistema.repository.DatosGeneralesRepository;
import com.genrisk.sistema.repository.GenotipificacionRepository;
import com.genrisk.sistema.repository.HabitosPacienteRepository;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

@Service
public class EstadisticaService {

    // 1. Inyectamos TODOS los repositorios que tienen datos numéricos
    private final DatosGeneralesRepository datosGeneralesRepository;
    private final HabitosPacienteRepository habitosPacienteRepository;
    private final DatosClinicosRepository datosClinicosRepository;
    private final GenotipificacionRepository genotipificacionRepository;
    // Nota: Histopatologia y FactDietariosAmbientales se omiten porque solo tienen Strings.

    // 2. Actualizamos el constructor
    public EstadisticaService(DatosGeneralesRepository datosGeneralesRepository,
                              HabitosPacienteRepository habitosPacienteRepository,
                              DatosClinicosRepository datosClinicosRepository,
                              GenotipificacionRepository genotipificacionRepository) {
        this.datosGeneralesRepository = datosGeneralesRepository;
        this.habitosPacienteRepository = habitosPacienteRepository;
        this.datosClinicosRepository = datosClinicosRepository;
        this.genotipificacionRepository = genotipificacionRepository;
    }

    /**
     * Calcula estadísticas para un atributo numérico de una entidad.
     * Asume que atributoObj está en camelCase (ej. "edad", "peso", "imc", "aniosConsumoAlcohol")
     */
    public EstadisticaResultado calcularEstadisticas(String entidadObj, String atributoObj) {
        
        // 1. Obtener la lista de valores
        List<Double> valores = obtenerValoresNumericos(entidadObj, atributoObj);

        // 2. Calcular estadísticas
        EstadisticaResultado resultado = new EstadisticaResultado();
        resultado.setEntidad(entidadObj);
        resultado.setAtributo(atributoObj);

        if (valores == null || valores.isEmpty()) {
            resultado.setCount(0);
            return resultado; // Devolvemos un objeto vacío si no hay datos
        }
        
        // Ordenamos la lista para mediana, min y max
        Collections.sort(valores);

        // 3. Setear los valores calculados
        resultado.setCount(valores.size());
        resultado.setMin(valores.get(0)); // Cálculo de Min
        resultado.setMax(valores.get(valores.size() - 1)); // Cálculo de Max
        resultado.setMedia(calcularMedia(valores));
        resultado.setMediana(calcularMediana(valores));
        resultado.setModa(calcularModa(valores));

        return resultado;
    }

    /**
     * Helper para obtener la lista de valores usando Reflection.
     */
    private List<Double> obtenerValoresNumericos(String entidadObj, String atributoObj) {
        List<Double> valores = new ArrayList<>();
        
        switch (entidadObj) {
            case "datos_generales":
                List<DatosGenerales> datosGen = datosGeneralesRepository.findAll();
                for (DatosGenerales dato : datosGen) {
                    Object valor = obtenerValorDeEntidad(dato, atributoObj);
                    agregarValorSiEsNumero(valores, valor);
                }
                break;
            
            case "habitos_paciente":
                List<HabitosPaciente> datosHab = habitosPacienteRepository.findAll();
                for (HabitosPaciente dato : datosHab) {
                    Object valor = obtenerValorDeEntidad(dato, atributoObj);
                    agregarValorSiEsNumero(valores, valor);
                }
                break;
            
            // --- NUEVO CASE ---
            case "datos_clinicos":
                List<DatosClinicos> datosCli = datosClinicosRepository.findAll();
                for (DatosClinicos dato : datosCli) {
                    Object valor = obtenerValorDeEntidad(dato, atributoObj);
                    agregarValorSiEsNumero(valores, valor);
                }
                break;

            // --- NUEVO CASE ---
            case "genotipificacion":
                List<Genotipificacion> datosGeno = genotipificacionRepository.findAll();
                for (Genotipificacion dato : datosGeno) {
                    Object valor = obtenerValorDeEntidad(dato, atributoObj);
                    agregarValorSiEsNumero(valores, valor);
                }
                break;
            
            default:
                throw new IllegalArgumentException("Entidad no soportada para estadísticas: " + entidadObj);
        }
        return valores;
    }

    /**
     * Método de Reflection (robusto, con mapeo)
     */
    private Object obtenerValorDeEntidad(Object entidad, String atributoObj) {
        try {
            // Mapeo manual de nombres de BBDD (snake_case) a nombres de Atributos Java (camelCase)
            String nombreAtributoJava = switch (atributoObj) {
                
                // --- Mapeos de DatosGenerales ---
                case "zona_residencial" -> "zonaResidencial";
                case "anios_resi_actual" -> "aniosResiActual";

                // --- Mapeos de HabitosPaciente ---
                case "estado_consumo_tabaco" -> "estadoConsumoTabaco";
                case "edad_inicio_tabaco" -> "edadInicioTabaco";
                case "cant_prom_tabaco" -> "cantPromTabaco";
                case "tiempo_tabaco" -> "tiempoTabaco";
                case "ex_consumidor_tabaco" -> "exConsumidorTabaco";
                case "estado_consumo_alcohol" -> "estadoConsumoAlcohol";
                case "frecuencia_alcohol" -> "frecuenciaAlcohol";
                case "cantidad_alcohol" -> "cantidadAlcohol";
                case "anios_consumo_alcohol" -> "aniosConsumoAlcohol";
                case "ex_consumidor_alcohol" -> "exConsumidorAlcohol";
                case "frecuencia_ejercicio" -> "frecuenciaEjercicio";

                // --- Mapeos de DatosClinicos ---
                case "adeno_gastrico" -> "adenoGastrico";
                case "fecha_adeno_gastrico" -> "fechaAdenoGastrico";
                case "ant_fam_cancer_gast" -> "antFamCancerGast";
                case "otras_enfermedades" -> "otrasEnfermedades";
                case "ant_fam_otro_cancer" -> "antFamOtroCancer";
                case "cirugia_gastrica_previa" -> "cirugiaGastricaPrevia";
                case "hpylori_prueba" -> "hpyloriPrueba";
                case "hpylori_resultado" -> "hpyloriResultado";
                case "hpylori_tiempo_test" -> "hpyloriTiempoTest";

                //Mapeos de Genotipificacion
                case "paciente_id" -> "pacienteID";
                case "muestra_nro" -> "muestraNRO";
                case "fecha_genoti" -> "fechaGenoti";
                case "estado_genoti" -> "estadoGenoti";
                
                // Si el nombre ya está en camelCase o es correcto (ej: "edad", "sexo")
                default -> atributoObj; 
            };

            Field campo = entidad.getClass().getDeclaredField(nombreAtributoJava);
            campo.setAccessible(true);
            return campo.get(entidad);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Advertencia: Atributo '" + atributoObj + "' (mapeado a '" + atributoObj + "') no encontrado en la entidad " + entidad.getClass().getSimpleName());
            return null;
        }
    }

    /**
     * Convierte y añade el valor a la lista si es un número.
     * Maneja Integers, Doubles y Strings que se pueden parsear.
     */
    private void agregarValorSiEsNumero(List<Double> lista, Object valor) {
        if (valor instanceof Number num) {
            lista.add(num.doubleValue());
        } else if (valor instanceof String str) {
            try {
                // Intenta convertir un String a número (útil para hpyloriTiempoTest)
                lista.add(Double.parseDouble(str));
            } catch (NumberFormatException e) {
                // Ignora el valor si no es un número (ej. "N/A")
            }
        }
    }

    // --- Métodos de Cálculo ---

    private Double calcularMedia(List<Double> valores) {
        return valores.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    private Double calcularMediana(List<Double> valores) {
        int size = valores.size();
        int mitad = size / 2;
        if (size % 2 == 1) {
            return valores.get(mitad); // Impar
        } else {
            return (valores.get(mitad - 1) + valores.get(mitad)) / 2.0; // Par
        }
    }

    private List<Double> calcularModa(List<Double> valores) {
        Map<Double, Integer> frecuencias = new HashMap<>();
        for (Double valor : valores) {
            frecuencias.put(valor, frecuencias.getOrDefault(valor, 0) + 1);
        }

        int maxFreq = 0;
        for (Integer freq : frecuencias.values()) {
            if (freq > maxFreq) {
                maxFreq = freq;
            }
        }

        if (maxFreq <= 1) {
            return new ArrayList<>(); // No hay moda (todos los valores son únicos)
        }

        List<Double> moda = new ArrayList<>();
        for (Map.Entry<Double, Integer> entry : frecuencias.entrySet()) {
            if (entry.getValue() == maxFreq) {
                moda.add(entry.getKey());
            }
        }
        return moda;
    }
}

