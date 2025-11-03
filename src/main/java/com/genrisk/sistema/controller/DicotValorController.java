package com.genrisk.sistema.controller;

import com.genrisk.sistema.model.entity.DicotValor;
import com.genrisk.sistema.services.DicotValorService;
import com.genrisk.sistema.services.PutCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/dicot-valores")
public class DicotValorController {
    
    private final DicotValorService dicotValorService;

    public DicotValorController(DicotValorService dicotValorService) {
        this.dicotValorService = dicotValorService;
    }

    @GetMapping
    public ResponseEntity<List<DicotValor>> getAllValores() {
        return dicotValorService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DicotValor> getValorById(@PathVariable Integer id) {
        return dicotValorService.getById(id);
    }

    @PostMapping
    public ResponseEntity<DicotValor> createValor(@RequestBody DicotValor valor) {
        // JSON de ejemplo para POST:
        // {
        //   "categoria": "edad",
        //   "valordicico": 1,
        //   "formulario": { "idFormulario": 101 },
        //   "dicotRegla": { "idDicotRegla": 1 }
        // }
        return dicotValorService.createPost(valor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DicotValor> updateValor(
            @PathVariable Integer id,
            @RequestBody DicotValor valorDetails) {
        
        PutCommand<DicotValor, Integer> command = new PutCommand<>();
        command.setId(id);
        command.setNewData(valorDetails);
        
        return dicotValorService.updatePut(command);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteValor(@PathVariable Integer id) {
        return dicotValorService.deleteById(id);
    }
}
