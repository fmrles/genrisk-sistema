package com.genrisk.sistema.controller;

import com.genrisk.sistema.model.entity.IngresaDatos;
import com.genrisk.sistema.services.IngresaDatosService;
import com.genrisk.sistema.services.PutCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/ingresa-datos")
public class IngresaDatosController {
    
    private final IngresaDatosService ingresaDatosService;

    public IngresaDatosController(IngresaDatosService ingresaDatosService) {
        this.ingresaDatosService = ingresaDatosService;
    }

    @GetMapping
    public ResponseEntity<List<IngresaDatos>> getAllRegistros() {
        return ingresaDatosService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngresaDatos> getRegistroById(@PathVariable Integer id) {
        return ingresaDatosService.getById(id);
    }

    @PostMapping
    public ResponseEntity<IngresaDatos> createRegistro(@RequestBody IngresaDatos registro) {
        // JSON de ejemplo para POST:
        // {
        //   "campo": "edad",
        //   "valorAnterior": "30",
        //   "valorNuevo": "31",
        //   "formulario": { "idFormulario": 101 },
        //   "miembroEquipo": { "idMiembroEquipo": 1 }
        // }
        return ingresaDatosService.createPost(registro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngresaDatos> updateRegistro(
            @PathVariable Integer id,
            @RequestBody IngresaDatos registroDetails) {
        
        PutCommand<IngresaDatos, Integer> command = new PutCommand<>();
        command.setId(id);
        command.setNewData(registroDetails);
        
        return ingresaDatosService.updatePut(command);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegistro(@PathVariable Integer id) {
        return ingresaDatosService.deleteById(id);
    }
}
