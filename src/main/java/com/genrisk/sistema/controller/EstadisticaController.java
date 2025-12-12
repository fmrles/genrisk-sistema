package com.genrisk.sistema.controller;

import com.genrisk.sistema.model.dto.EstadisticaResultado;
import com.genrisk.sistema.services.EstadisticaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/estadisticas")
@CrossOrigin(origins = "*")
public class EstadisticaController {

    private final EstadisticaService estadisticaService;
    public EstadisticaController(EstadisticaService estadisticaService) {
        this.estadisticaService = estadisticaService;
    }

    /**
     * Calcula estadísticas (media, mediana, etc.) para un atributo numérico de una entidad.
     * Ejemplo de URL: GET http://localhost:8080/estadisticas/datos_generales/edad
     * Ejemplo de URL: GET http://localhost:8080/estadisticas/habitos_paciente/aniosConsumoAlcohol
     */
    @GetMapping("/{entidadObj}/{atributoObj}")
    public ResponseEntity<EstadisticaResultado> getEstadisticas(
            @PathVariable String entidadObj,
            @PathVariable String atributoObj) {
        
        try {
            EstadisticaResultado resultado = estadisticaService.calcularEstadisticas(entidadObj, atributoObj);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // Si la entidad no existe
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build(); // Otro error
        }
    }
}
