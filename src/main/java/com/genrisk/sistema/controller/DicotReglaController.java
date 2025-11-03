package com.genrisk.sistema.controller;

import com.genrisk.sistema.model.entity.DicotRegla;
import com.genrisk.sistema.services.DicotReglaService;
import com.genrisk.sistema.services.PutCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/dicot-reglas")
public class DicotReglaController {
    
    private final DicotReglaService dicotReglaService;

    public DicotReglaController(DicotReglaService dicotReglaService) {
        this.dicotReglaService = dicotReglaService;
    }

    @GetMapping
    public ResponseEntity<List<DicotRegla>> getAllReglas() {
        return dicotReglaService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DicotRegla> getReglaById(@PathVariable Integer id) {
        return dicotReglaService.getById(id);
    }

    @PostMapping
    public ResponseEntity<DicotRegla> createRegla(@RequestBody DicotRegla regla) {
        // JSON ee ejemplo para POST:
        // {
        //   "entidadObj": "datos_generales",
        //   "atributoObj": "edad",
        //   "operador": ">=",
        //   "valorInf": 50,
        //   "dicotConjunto": { "idDicotConjunto": 1 }
        // }
        return dicotReglaService.createPost(regla);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DicotRegla> updateRegla(
            @PathVariable Integer id,
            @RequestBody DicotRegla reglaDetails) {
        
        PutCommand<DicotRegla, Integer> command = new PutCommand<>();
        command.setId(id);
        command.setNewData(reglaDetails);
        
        return dicotReglaService.updatePut(command);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegla(@PathVariable Integer id) {
        return dicotReglaService.deleteById(id);
    }
}
