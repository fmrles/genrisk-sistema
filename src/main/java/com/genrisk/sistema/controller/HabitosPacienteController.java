package com.genrisk.sistema.controller;

import com.genrisk.sistema.model.entity.HabitosPaciente;
import com.genrisk.sistema.model.entity.weakEntityKey.HabitosPacienteID;
import com.genrisk.sistema.services.HabitosPacienteService;
import com.genrisk.sistema.services.PutCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/habitos-paciente")
public class HabitosPacienteController {

    private final HabitosPacienteService habitosPacienteService;

    public HabitosPacienteController(HabitosPacienteService habitosPacienteService) {
        this.habitosPacienteService = habitosPacienteService;
    }

    @GetMapping
    public ResponseEntity<List<HabitosPaciente>> getAllHabitosPaciente() {
        return habitosPacienteService.getAll();
    }

    @PostMapping
    public ResponseEntity<HabitosPaciente> createHabitosPaciente( @Valid @RequestBody HabitosPaciente habitosPaciente) {
        return habitosPacienteService.createPost(habitosPaciente);
    }
    
    // los metodos estan adaptados para la Clave Compuesta 

    @GetMapping("/{itemFormu}/{formularioId}")
    public ResponseEntity<HabitosPaciente> getHabitosPacienteById(
            @PathVariable Integer itemFormu,
            @PathVariable Integer formularioId) {
        
        HabitosPacienteID id = new HabitosPacienteID(itemFormu, formularioId);
        return habitosPacienteService.getById(id);
    }

    @PutMapping("/{itemFormu}/{formularioId}")
    public ResponseEntity<HabitosPaciente> updateHabitosPaciente(
            @PathVariable Integer itemFormu,
            @PathVariable Integer formularioId,
            @Valid @RequestBody HabitosPaciente habitosDetails) {
        
        HabitosPacienteID id = new HabitosPacienteID(itemFormu, formularioId);
        PutCommand<HabitosPaciente, HabitosPacienteID> command = new PutCommand<>();
        command.setId(id);
        command.setNewData(habitosDetails);
        
        return habitosPacienteService.updatePut(command);
    }

    @DeleteMapping("/{itemFormu}/{formularioId}")
    public ResponseEntity<Void> deleteHabitosPaciente(
            @PathVariable Integer itemFormu,
            @PathVariable Integer formularioId) {
        
        HabitosPacienteID id = new HabitosPacienteID(itemFormu, formularioId);
        return habitosPacienteService.deleteById(id);
    }
}
