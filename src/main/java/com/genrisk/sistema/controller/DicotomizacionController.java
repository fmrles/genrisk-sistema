package com.genrisk.sistema.controller;

import com.genrisk.sistema.model.entity.DicotValor;
import com.genrisk.sistema.services.DicotomizacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dicotomizacion")
public class DicotomizacionController {
    
    private final DicotomizacionService dicotomizacionService;

    public DicotomizacionController(DicotomizacionService dicotomizacionService) {
        this.dicotomizacionService = dicotomizacionService;
    }

    /**
     * Endpoint para ejecutar el proceso de dicotomización para un conjunto de reglas dado.
     * URL: POST http://localhost:8081/api/v1/dicotomizacion/ejecutar/{idDicotConjunto}
     * @param idDicotConjunto El ID del conjunto de reglas a ejecutar.
     * @return Una lista de los DicotValor generados.
     */
    @PostMapping("/ejecutar/{idDicotConjunto}")
    public ResponseEntity<List<DicotValor>> ejecutar(@PathVariable Integer idDicotConjunto) {
        List<DicotValor> resultados = dicotomizacionService.ejecutarDicotomizacion(idDicotConjunto);
        return ResponseEntity.ok(resultados);
    }
}
