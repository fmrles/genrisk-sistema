package com.genrisk.sistema.controller;

import com.genrisk.sistema.model.entity.Paciente;
import com.genrisk.sistema.services.PacienteService;
import com.genrisk.sistema.services.PutCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.genrisk.sistema.model.dto.PacienteDetalladoDTO;
import com.genrisk.sistema.services.PacienteDetalladoService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {
    
    private PacienteService pacienteService;

    @Autowired
    private PacienteDetalladoService pacienteDetalladoService;

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

    @GetMapping("/{id}/detallado")
    public ResponseEntity<PacienteDetalladoDTO> getPacienteDetallado(@PathVariable String id) {
        try {
            PacienteDetalladoDTO datosCompletos = pacienteDetalladoService.obtenerDatosCompletosPaciente(id);
            return ResponseEntity.ok(datosCompletos);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
