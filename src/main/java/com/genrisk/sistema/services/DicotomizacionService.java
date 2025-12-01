package com.genrisk.sistema.services;

import com.genrisk.sistema.model.dto.EstadisticaResultado;
import com.genrisk.sistema.model.entity.*;
import com.genrisk.sistema.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
    private final EstadisticaService estadisticaService;

    public DicotomizacionService(DicotReglaRepository dicotReglaRepository, 
                                 DicotValorRepository dicotValorRepository, 
                                 FormularioRepository formularioRepository,
                                 DatosGeneralesRepository datosGeneralesRepository,
                                 HabitosPacienteRepository habitosPacienteRepository,
                                 DatosClinicosRepository datosClinicosRepository,
                                 FactDietariosAmbientalesRepository factDietariosAmbientalesRepository,
                                 EstadisticaService estadisticaService) {
        
        this.dicotReglaRepository = dicotReglaRepository;
        this.dicotValorRepository = dicotValorRepository;
        this.formularioRepository = formularioRepository;
        this.datosGeneralesRepository = datosGeneralesRepository;
        this.habitosPacienteRepository = habitosPacienteRepository;
        this.datosClinicosRepository = datosClinicosRepository;
        this.factDietariosAmbientalesRepository = factDietariosAmbientalesRepository;
        this.estadisticaService = estadisticaService;
    }
    
@Transactional
    public List<DicotValor> ejecutarDicotomizacion(Integer dicotConjuntoId) {
        List<DicotRegla> reglas = dicotReglaRepository.findByDicotConjuntoId(dicotConjuntoId);
        List<Formulario> formularios = formularioRepository.findAll();
        List<DicotValor> nuevosValores = new ArrayList<>();
        
        for (DicotRegla regla : reglas) {
            if (regla.getMetodo() != null) {
                String metodo = regla.getMetodo().toUpperCase();
                if ("PROMEDIO".equals(metodo) || "MEDIANA".equals(metodo)) {
                    try {
                        EstadisticaResultado stats = estadisticaService.calcularEstadisticas(regla.getEntidadObj(), regla.getAtributoObj());
                        Double valorRef = "PROMEDIO".equals(metodo) ? stats.getMedia() : stats.getMediana();
                        if (valorRef == null) valorRef = 0.0;
                        int valorEntero = (int) Math.round(valorRef);
                        if (regla.getOperador().contains(">")) regla.setValorInf(valorEntero);
                        else if (regla.getOperador().contains("<")) regla.setValorSup(valorEntero);
                    } catch (Exception e) {
                        System.err.println("Error calculando estadística para regla: " + regla.getAtributoObj());
                    }
                }
            }
        }

        for (Formulario formulario : formularios) {
            for (DicotRegla regla : reglas) {
                Optional<?> entidadOpt = obtenerEntidadPorFormulario(formulario.getIdFormulario(), regla.getEntidadObj());
                
                if (entidadOpt.isPresent()) {
                    Object entidad = entidadOpt.get();
                    Object valorAtributo = obtenerValorDeEntidad(entidad, regla.getAtributoObj());
                    
                    if (valorAtributo != null) {
                        Integer resultado = aplicarRegla(regla, valorAtributo);
                        if (resultado != null) {
                            DicotValor nuevoValor = new DicotValor();
                            nuevoValor.setCategoria(regla.getAtributoObj());
                            nuevoValor.setValordicico(resultado);
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
    
    private Object obtenerValorDeEntidad(Object entidad, String atributoObj) {
        String nombreJava = convertirSnakeACamel(atributoObj);

        try {
            Field campo = entidad.getClass().getDeclaredField(nombreJava);
            campo.setAccessible(true);
            return campo.get(entidad);
        } catch (NoSuchFieldException e) {
          try {
                String nombreMetodo = "get" + Character.toUpperCase(nombreJava.charAt(0)) + nombreJava.substring(1);
                Method metodo = entidad.getClass().getMethod(nombreMetodo);
                return metodo.invoke(entidad);
            } catch (Exception ex) {
                return null; 
            }
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    private Integer aplicarRegla(DicotRegla regla, Object valorAtributo) {
        String op = regla.getOperador();
        int si = (regla.getValorSiCumple() != null) ? regla.getValorSiCumple() : 1;
        int no = (regla.getValorNoCumple() != null) ? regla.getValorNoCumple() : 0;

        // Reglas Numéricas
        if (regla.getValorInf() != null || regla.getValorSup() != null) {
            if (!(valorAtributo instanceof Number)) return no;
            double val = ((Number) valorAtributo).doubleValue();
            
            if (">=".equals(op) && regla.getValorInf() != null && val >= regla.getValorInf()) return si;
            if (">".equals(op) && regla.getValorInf() != null && val > regla.getValorInf()) return si;
            if ("<=".equals(op) && regla.getValorSup() != null && val <= regla.getValorSup()) return si;
            if ("<".equals(op) && regla.getValorSup() != null && val < regla.getValorSup()) return si;
            if ("=".equals(op) && regla.getValorInf() != null && val == regla.getValorInf()) return si;
            
            return no;
        } 
        else if (regla.getValorCategoria() != null) {
            String val = valorAtributo.toString();
            String target = regla.getValorCategoria();
            
            if ("=".equals(op) && val.equalsIgnoreCase(target)) return si;
            if ("!=".equals(op) && !val.equalsIgnoreCase(target)) return si;
            return no;
        }
        return no;
    }

    // Helper simple para convertir nombres
    private String convertirSnakeACamel(String snake) {
        if (snake.equals("zona")) return "zonaResidencial";
        if (snake.equals("estado")) return "estadoConsumoTabaco";
        StringBuilder sb = new StringBuilder();
        boolean nextUpper = false;
        for (char c : snake.toCharArray()) {
            if (c == '_') nextUpper = true;
            else {
                if (nextUpper) { sb.append(Character.toUpperCase(c)); nextUpper = false; }
                else sb.append(c);
            }
        }
        return sb.toString();
    }
}

