package com.genrisk.sistema.controller;

import com.genrisk.sistema.model.entity.Paciente;
import com.genrisk.sistema.services.PacienteService;
import com.genrisk.sistema.services.PutCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {
    
    private PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping
    public ResponseEntity<List<Paciente>> getAllPacientes() {
        return pacienteService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paciente> getPacienteById(@PathVariable String id) {
        return pacienteService.getById(id);
    }

    @PostMapping
    public ResponseEntity<Paciente> createPaciente(@RequestBody Paciente paciente) {
        return pacienteService.createPost(paciente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paciente> updatePaciente(@PathVariable String id, @RequestBody Paciente pacienteDetails) {
        PutCommand<Paciente, String> pacienteNew = new PutCommand<>();
        pacienteNew.setId(id);
        pacienteNew.setNewData(pacienteDetails);
        return pacienteService.updatePut(pacienteNew);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaciente(@PathVariable String id) {
        return pacienteService.deleteById(id);
    }
}
