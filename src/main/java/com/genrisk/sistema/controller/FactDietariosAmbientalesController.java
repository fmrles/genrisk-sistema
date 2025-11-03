package com.genrisk.sistema.controller;

import com.genrisk.sistema.model.entity.FactDietariosAmbientales;
import com.genrisk.sistema.model.entity.weakEntityKey.FactDietariosAmbientalesID;
import com.genrisk.sistema.services.FactDietariosAmbientalesService;
import com.genrisk.sistema.services.PutCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/factores-dietarios-hambientales")
public class FactDietariosAmbientalesController {

    private final FactDietariosAmbientalesService factDietariosService;

    public FactDietariosAmbientalesController(FactDietariosAmbientalesService factDietariosService) {
        this.factDietariosService = factDietariosService;
    }

    @GetMapping
    public ResponseEntity<List<FactDietariosAmbientales>> getAllFactores() {
        return factDietariosService.getAll();
    }

    @PostMapping
    public ResponseEntity<FactDietariosAmbientales> createFactor(@RequestBody FactDietariosAmbientales factor) {
        return factDietariosService.createPost(factor);
    }
    
    // los metodos estan adaptados para la Clave Compuesta 
    @GetMapping("/{itemFormu}/{formularioId}")
    public ResponseEntity<FactDietariosAmbientales> getFactorById(
            @PathVariable Integer itemFormu,
            @PathVariable Integer formularioId) {
        
        FactDietariosAmbientalesID id = new FactDietariosAmbientalesID(itemFormu, formularioId);
        return factDietariosService.getById(id);
    }

    @PutMapping("/{itemFormu}/{formularioId}")
    public ResponseEntity<FactDietariosAmbientales> updateFactor(
            @PathVariable Integer itemFormu,
            @PathVariable Integer formularioId,
            @RequestBody FactDietariosAmbientales factorDetails) {
        
        FactDietariosAmbientalesID id = new FactDietariosAmbientalesID(itemFormu, formularioId);
        PutCommand<FactDietariosAmbientales, FactDietariosAmbientalesID> command = new PutCommand<>();
        command.setId(id);
        command.setNewData(factorDetails);
        
        return factDietariosService.updatePut(command);
    }

    @DeleteMapping("/{itemFormu}/{formularioId}")
    public ResponseEntity<Void> deleteFactor(
            @PathVariable Integer itemFormu,
            @PathVariable Integer formularioId) {
        
        FactDietariosAmbientalesID id = new FactDietariosAmbientalesID(itemFormu, formularioId);
        return factDietariosService.deleteById(id);
    }
}
