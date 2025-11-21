package com.genrisk.sistema.controller;
import com.genrisk.sistema.model.entity.*;
import com.genrisk.sistema.services.*;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/miembro-equipo")
public class MiembroEquipoController {
    private MiembroEquipoService miembroEquipoService;

    public MiembroEquipoController(MiembroEquipoService miembroEquipoService){
        this.miembroEquipoService = miembroEquipoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MiembroEquipo> getMiembroID(@PathVariable Integer id){
        return miembroEquipoService.getById(id);
    }

    @GetMapping()
    public ResponseEntity<List<MiembroEquipo>> getUsuarios(){
        return miembroEquipoService.getAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<MiembroEquipo> putMiembro(@PathVariable Integer id, @RequestBody MiembroEquipo newMiembro){
        PutCommand<MiembroEquipo, Integer> miembroNew = new PutCommand<>();
        miembroNew.setId(id);
        miembroNew.setNewData(newMiembro);
        return miembroEquipoService.updatePut(miembroNew);
    }

    @PostMapping()
    public ResponseEntity<MiembroEquipo> createMiembro(@RequestBody MiembroEquipo miembro){
        return miembroEquipoService.createPost(miembro);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMiembro(@PathVariable Integer id){
        return miembroEquipoService.deleteById(id);
    }
}
    

    
    



