package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.*;
import com.genrisk.sistema.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class DicotomizacionService {
    private final DicotReglaRepository dicotReglaRepository;
    private final DicotValorRepository dicotValorRepository;
    private final FormularioRepository formularioRepository;
    private final DatosGeneralesRepository datosGeneralesRepository;
    private final HabitosPacienteRepository habitosPacienteRepository;
    private final DatosClinicosRepository datosClinicosRepository;
    private final FactDietariosAmbientalesRepository factDietariosAmbientalesRepository;

    public DicotomizacionService(DicotReglaRepository dicotReglaRepository, 
                                 DicotValorRepository dicotValorRepository, 
                                 FormularioRepository formularioRepository,
                                 DatosGeneralesRepository datosGeneralesRepository,
                                 HabitosPacienteRepository habitosPacienteRepository,
                                 DatosClinicosRepository datosClinicosRepository,
                                 FactDietariosAmbientalesRepository factDietariosAmbientalesRepository) {
        
        this.dicotReglaRepository = dicotReglaRepository;
        this.dicotValorRepository = dicotValorRepository;
        this.formularioRepository = formularioRepository;
        this.datosGeneralesRepository = datosGeneralesRepository;
        this.habitosPacienteRepository = habitosPacienteRepository;
        this.datosClinicosRepository = datosClinicosRepository;
        this.factDietariosAmbientalesRepository = factDietariosAmbientalesRepository;
    }

    @Transactional
    public List<DicotValor> ejecutarDicotomizacion(Integer dicotConjuntoId) {
        // 1. Obtener todas las reglas para el conjunto
        List<DicotRegla> reglas = dicotReglaRepository.findByDicotConjuntoId(dicotConjuntoId);
        
        // 2. Obtener todos los formularios
        List<Formulario> formularios = formularioRepository.findAll();
        
        List<DicotValor> nuevosValores = new ArrayList<>();
        
        // 3. Iterar y aplicar las reglas
        for (Formulario formulario : formularios) {
            for (DicotRegla regla : reglas) {
                
                Optional<?> entidadOptional = obtenerEntidadPorFormulario(formulario.getIdFormulario(), regla.getEntidadObj());
                
                if (entidadOptional.isPresent()) {
                    Object entidad = entidadOptional.get();
                    Object valorAtributo = obtenerValorDeEntidad(entidad, regla.getAtributoObj());
                    
                    if (valorAtributo != null) {
                        Integer valorDicotomizado = aplicarRegla(regla, valorAtributo);

                        if (valorDicotomizado != null) {
                            // Crear y guardar el resultado
                            DicotValor nuevoValor = new DicotValor();
                            nuevoValor.setCategoria(regla.getAtributoObj());
                            nuevoValor.setValordicico(valorDicotomizado);
                            nuevoValor.setFormulario(formulario);
                            nuevoValor.setDicotRegla(regla);
                            nuevoValor.setFormularioID(formulario.getIdFormulario());
                            nuevoValor.setReglaDicotID(regla.getIdDicotRegla());
                            
                            nuevosValores.add(dicotValorRepository.save(nuevoValor));
                        }
                    }
                }
            }
        }
        
        return nuevosValores;
    }

    // Obtengo la entidad de datos (ej: DatosGenerales) por el ID de formulario
    private Optional<?> obtenerEntidadPorFormulario(Integer formularioId, String entidadObj) {
        return switch (entidadObj) {
            case "datos_genericos" -> datosGeneralesRepository.findByIdDatosGen_FormularioId(formularioId);
            case "habitos_paciente" -> habitosPacienteRepository.findByIdHabPaciente_FormularioId(formularioId);
            case "datos_clinicos" -> datosClinicosRepository.findByIdDatosCli_FormularioId(formularioId);
            case "fact_dietario_ambiental" -> factDietariosAmbientalesRepository.findByIdFact_FormularioId(formularioId);
            default -> Optional.empty();
        };
    }
    
    // Uso Reflection para obtener el valor del campo (propiedad) de la entidad
    private Object obtenerValorDeEntidad(Object entidad, String atributoObj) {
        try {
            
            String nombreAtributoJava = switch (atributoObj) {
                
                //Mapeos de DatosGenerales
                case "zona" -> "zonaResidencial"; 
                case "zona_residencial" -> "zonaResidencial"; 
                case "anios_resi_actual" -> "aniosResiActual"; 
                //Mapeos de HabitosPaciente
                case "estado" -> "estadoConsumoTabaco"; 
                case "frecuencia" -> "frecuenciaAlcohol"; 
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

                //Mapeos de DatosClinicos
                case "adeno_gastrico" -> "adenoGastrico"; 
                case "fecha_adeno_gastrico" -> "fechaAdenoGastrico"; 
                case "ant_fam_cancer_gast" -> "antFamCancerGast"; 
                case "otras_enfermedades" -> "otrasEnfermedades"; 
                case "ant_fam_otro_cancer" -> "antFamOtroCancer"; 
                case "cirugia_gastrica_previa" -> "cirugiaGastricaPrevia"; 
                case "hpylori_prueba" -> "hpyloriPrueba"; 
                case "hpylori_resultado" -> "hpyloriResultado"; 
                case "hpylori_tiempo_test" -> "hpyloriTiempoTest"; 

                //Mapeos de FactDietariosAmbientales
                case "trabajo_zona_rural" -> "trabajoZonaRural"; 
                case "agua_consumo_rural" -> "aguaConsumoRural"; 
                case "tratamiento_agua" -> "tratamientoAgua"; 
                case "exposicion_pesticidas" -> "exposicionPesticidas"; 
                case "combus_lena_diario" -> "combusLenaDiario"; 
                case "exposicion_quimicos" -> "exposicionQuimicos"; 
                case "dieta_agregasal" -> "dietaAgregaSal"; 
                case "dieta_frutas_verduras" -> "dietaFrutasVerduras"; 
                case "dieta_frituras" -> "dietaFrituras"; 
                case "dieta_carnes_cecinas" -> "dietaCarnesCecinas"; 
                
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

    // Aplica la lógica de la regla (numérica o de texto)
    private Integer aplicarRegla(DicotRegla regla, Object valorAtributo) {
        String operador = regla.getOperador();

        // 1. Reglas Cuantitativas 
        if (regla.getValorInf() != null || regla.getValorSup() != null) {
            if (!(valorAtributo instanceof Number)) return 0; 
            double valor = ((Number) valorAtributo).doubleValue();
            
            // Lógica Cuantitativa 
            if (">=".equals(operador)) {
                if (regla.getValorInf() != null && valor >= regla.getValorInf()) return 1;
            } else if ("<=".equals(operador)) {
                if (regla.getValorSup() != null && valor <= regla.getValorSup()) return 1;
            } else if ("=".equals(operador)) {
                if (regla.getValorInf() != null && valor == regla.getValorInf()) return 1;
            } 
            
            return 0;

        // 2. Reglas Cualitativas (Texto)
        } else if (regla.getValorCategoria() != null) {
            String valor = valorAtributo.toString();
            String valorRegla = regla.getValorCategoria();

            // Lógica Cualitativa
            if ("=".equals(operador) && valor.equalsIgnoreCase(valorRegla)) {
                return 1;
            } else if ("!=".equals(operador) && !valor.equalsIgnoreCase(valorRegla)) {
                return 1;
            }
            
            return 0;
        }

        return 0; // Regla mal definida
    }
}

