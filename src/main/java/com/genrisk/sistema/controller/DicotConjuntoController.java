package com.genrisk.sistema.controller;

import com.genrisk.sistema.model.entity.DicotConjunto;
import com.genrisk.sistema.services.DicotConjuntoService;
import com.genrisk.sistema.services.PutCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/dicot-conjuntos")
public class DicotConjuntoController {
    
    private final DicotConjuntoService dicotConjuntoService;

    public DicotConjuntoController(DicotConjuntoService dicotConjuntoService) {
        this.dicotConjuntoService = dicotConjuntoService;
    }

    @GetMapping
    public ResponseEntity<List<DicotConjunto>> getAllConjuntos() {
        return dicotConjuntoService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DicotConjunto> getConjuntoById(@PathVariable Integer id) {
        return dicotConjuntoService.getById(id);
    }

    @PostMapping
    public ResponseEntity<DicotConjunto> createConjunto(@RequestBody DicotConjunto conjunto) {
        return dicotConjuntoService.createPost(conjunto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DicotConjunto> updateConjunto(
            @PathVariable Integer id,
            @RequestBody DicotConjunto conjuntoDetails) {
        
        PutCommand<DicotConjunto, Integer> command = new PutCommand<>();
        command.setId(id);
        command.setNewData(conjuntoDetails);
        
        return dicotConjuntoService.updatePut(command);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConjunto(@PathVariable Integer id) {
        return dicotConjuntoService.deleteById(id);
    }
}
