package com.genrisk.sistema.controller;

import com.genrisk.sistema.model.entity.Muestra;
import com.genrisk.sistema.model.entity.weakEntityKey.muestraID;
import com.genrisk.sistema.services.MuestraService;
import com.genrisk.sistema.services.PutCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/muestras")
public class MuestraController {

    private final MuestraService muestraService;

    public MuestraController(MuestraService muestraService) {
        this.muestraService = muestraService;
    }

    @GetMapping
    public ResponseEntity<List<Muestra>> getAllMuestras() {
        return muestraService.getAll();
    }

    @PostMapping
    public ResponseEntity<Muestra> createMuestra(@RequestBody Muestra muestra) {
        // JSON esperado:
        // {
        //   "id": { "pacienteId": "pac1", "nroMuestra": 1 },
        //   "tipoMuestra": "ADN", ...
        // }
        return muestraService.createPost(muestra);
    }
    
    // los metodos estan adaptados para la Clave Compuesta 

    @GetMapping("/{pacienteId}/{nroMuestra}")
    public ResponseEntity<Muestra> getMuestraById(
            @PathVariable String pacienteId,
            @PathVariable Integer nroMuestra) {
        
        muestraID id = new muestraID(pacienteId, nroMuestra);
        return muestraService.getById(id);
    }

    @PutMapping("/{pacienteId}/{nroMuestra}")
    public ResponseEntity<Muestra> updateMuestra(
            @PathVariable String pacienteId,
            @PathVariable Integer nroMuestra,
            @RequestBody Muestra muestraDetails) {
        
        muestraID id = new muestraID(pacienteId, nroMuestra);
        PutCommand<Muestra, muestraID> command = new PutCommand<>();
        command.setId(id);
        command.setNewData(muestraDetails);
        
        return muestraService.updatePut(command);
    }

    @DeleteMapping("/{pacienteId}/{nroMuestra}")
    public ResponseEntity<Void> deleteMuestra(
            @PathVariable String pacienteId,
            @PathVariable Integer nroMuestra) {
        
        muestraID id = new muestraID(pacienteId, nroMuestra);
        return muestraService.deleteById(id);
    }
}