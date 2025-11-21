package com.genrisk.sistema.controller;

import com.genrisk.sistema.model.entity.DatosClinicos;
import com.genrisk.sistema.model.entity.weakEntityKey.DatosClinicosID;
import com.genrisk.sistema.services.DatosClinicosService;
import com.genrisk.sistema.services.PutCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/datos-clinicos")
public class DatosClinicosController {
  
    private final DatosClinicosService datosClinicosService;

    public DatosClinicosController(DatosClinicosService datosClinicosService) {
        this.datosClinicosService = datosClinicosService;
    }

    @GetMapping
    public ResponseEntity<List<DatosClinicos>> getAllDatosClinicos() {
        return datosClinicosService.getAll();
    }

    @PostMapping
    public ResponseEntity<DatosClinicos> createDatosClinicos(@RequestBody DatosClinicos datosClinicos) {
        return datosClinicosService.createPost(datosClinicos);
    }
    
    // los metodos estan adaptados para la Clave Compuesta 

    @GetMapping("/{itemFormu}/{formularioId}")
    public ResponseEntity<DatosClinicos> getDatosClinicosById(
            @PathVariable Integer itemFormu,
            @PathVariable Integer formularioId) {
        
        DatosClinicosID id = new DatosClinicosID(itemFormu, formularioId);
        return datosClinicosService.getById(id);
    }

    @PutMapping("/{itemFormu}/{formularioId}")
    public ResponseEntity<DatosClinicos> updateDatosClinicos(
            @PathVariable Integer itemFormu,
            @PathVariable Integer formularioId,
            @RequestBody DatosClinicos datosDetails) {
        
        DatosClinicosID id = new DatosClinicosID(itemFormu, formularioId);
        PutCommand<DatosClinicos, DatosClinicosID> command = new PutCommand<>();
        command.setId(id);
        command.setNewData(datosDetails);
        
        return datosClinicosService.updatePut(command);
    }

    @DeleteMapping("/{itemFormu}/{formularioId}")
    public ResponseEntity<Void> deleteDatosClinicos(
            @PathVariable Integer itemFormu,
            @PathVariable Integer formularioId) {
        
        DatosClinicosID id = new DatosClinicosID(itemFormu, formularioId);
        return datosClinicosService.deleteById(id);
    }
}
