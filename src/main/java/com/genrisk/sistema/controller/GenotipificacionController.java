package com.genrisk.sistema.controller;

import com.genrisk.sistema.model.entity.Genotipificacion;
import com.genrisk.sistema.services.GenotipificacionService;
import com.genrisk.sistema.services.PutCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/genotipificaciones")
public class GenotipificacionController {
    
    private final GenotipificacionService genotipificacionService;

    public GenotipificacionController(GenotipificacionService genotipificacionService) {
        this.genotipificacionService = genotipificacionService;
    }

    @GetMapping
    public ResponseEntity<List<Genotipificacion>> getAllGenotipificaciones() {
        return genotipificacionService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genotipificacion> getGenotipificacionById(@PathVariable Integer id) {
        return genotipificacionService.getById(id);
    }

    @PostMapping
    public ResponseEntity<Genotipificacion> createGenotipificacion(@RequestBody Genotipificacion genotipificacion) {
        // Esta entidad no tiene relaciones muy complejas (espero)
        // por lo que el JSON es directo.
        return genotipificacionService.createPost(genotipificacion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Genotipificacion> updateGenotipificacion(
            @PathVariable Integer id,
            @RequestBody Genotipificacion genotipificacionDetails) {
        
        PutCommand<Genotipificacion, Integer> command = new PutCommand<>();
        command.setId(id);
        command.setNewData(genotipificacionDetails);
        
        return genotipificacionService.updatePut(command);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenotipificacion(@PathVariable Integer id) {
        return genotipificacionService.deleteById(id);
    }
}
