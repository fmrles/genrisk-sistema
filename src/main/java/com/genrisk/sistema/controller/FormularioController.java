package com.genrisk.sistema.controller;

import com.genrisk.sistema.model.entity.Formulario;
import com.genrisk.sistema.services.FormularioService;
import com.genrisk.sistema.services.PutCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/formularios")
public class FormularioController {
    
    private FormularioService formularioService;

    public FormularioController(FormularioService formularioService) {
        this.formularioService = formularioService;
    }

   @GetMapping
    public ResponseEntity<List<Formulario>> getAllFormularios() {
        return formularioService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Formulario> getFormularioById(@PathVariable Integer id) {
        return formularioService.getById(id);
    }

    @PostMapping
    public ResponseEntity<Formulario> createFormulario(@RequestBody Formulario formulario) {
        return formularioService.createPost(formulario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Formulario> updateFormulario(@PathVariable Integer id, @RequestBody Formulario formularioDetails) {
        PutCommand<Formulario, Integer> command = new PutCommand<>();
        command.setId(id);
        command.setNewData(formularioDetails);
        return formularioService.updatePut(command);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFormulario(@PathVariable Integer id) {
        return formularioService.deleteById(id);
    }
}
