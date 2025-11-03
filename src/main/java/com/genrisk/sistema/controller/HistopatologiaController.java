package com.genrisk.sistema.controller;

import com.genrisk.sistema.model.entity.Histopatologia;
import com.genrisk.sistema.model.entity.weakEntityKey.HistopatologiaID;
import com.genrisk.sistema.services.HistopatologiaService;
import com.genrisk.sistema.services.PutCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/histopatologia")
public class HistopatologiaController {
    
    private final HistopatologiaService histopatologiaService;

    public HistopatologiaController(HistopatologiaService histopatologiaService) {
        this.histopatologiaService = histopatologiaService;
    }

    @GetMapping
    public ResponseEntity<List<Histopatologia>> getAllHistopatologias() {
        return histopatologiaService.getAll();
    }

    @PostMapping
    public ResponseEntity<Histopatologia> createHistopatologia(@RequestBody Histopatologia histopatologia) {
        return histopatologiaService.createPost(histopatologia);
    }
    
    // los metodos estan adaptados para la Clave Compuesta 

    @GetMapping("/{itemFormu}/{formularioId}")
    public ResponseEntity<Histopatologia> getHistopatologiaById(
            @PathVariable Integer itemFormu,
            @PathVariable Integer formularioId) {
        
        HistopatologiaID id = new HistopatologiaID(itemFormu, formularioId);
        return histopatologiaService.getById(id);
    }

    @PutMapping("/{itemFormu}/{formularioId}")
    public ResponseEntity<Histopatologia> updateHistopatologia(
            @PathVariable Integer itemFormu,
            @PathVariable Integer formularioId,
            @RequestBody Histopatologia histoDetails) {
        
        HistopatologiaID id = new HistopatologiaID(itemFormu, formularioId);
        PutCommand<Histopatologia, HistopatologiaID> command = new PutCommand<>();
        command.setId(id);
        command.setNewData(histoDetails);
        
        return histopatologiaService.updatePut(command);
    }

    @DeleteMapping("/{itemFormu}/{formularioId}")
    public ResponseEntity<Void> deleteHistopatologia(
            @PathVariable Integer itemFormu,
            @PathVariable Integer formularioId) {
        
        HistopatologiaID id = new HistopatologiaID(itemFormu, formularioId);
        return histopatologiaService.deleteById(id);
    }
}
