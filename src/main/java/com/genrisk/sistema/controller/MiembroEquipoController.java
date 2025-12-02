package com.genrisk.sistema.controller;

import com.genrisk.sistema.model.entity.*;
import com.genrisk.sistema.model.dto.*;
import com.genrisk.sistema.services.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

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
    public ResponseEntity<MiembroEquipo> putMiembro(@PathVariable Integer id, @Valid @RequestBody MiembroEquipoDTO newMiembro){
        PutCommand<MiembroEquipo, Integer> miembroNewCom = new PutCommand<>();
        miembroNewCom.setId(id);
        
        MiembroEquipo miembroAct = miembroEquipoService.dtoToEntity(newMiembro);
        miembroAct.setIdMiembroEquipo(id);
        
        miembroNewCom.setNewData(miembroAct);
        return miembroEquipoService.updatePut(miembroNewCom);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<MiembroEquipo> miembroOpt = miembroEquipoService.login(request);

        if (miembroOpt.isEmpty()) {
            return ResponseEntity.status(401)
                    .body(Map.of("Error!", "Las credenciales son inválidas"));
        }

        MiembroEquipo miembro = miembroOpt.get();

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Login Exitoso");
        respuesta.put("usuario", Map.of(
            "id", miembro.getIdMiembroEquipo(),
            "nombre", miembro.getNombreMiembro(),
            "correo", miembro.getCorreoMiembro(),
            "rol", miembro.getRolMiembro()
        ));

        return ResponseEntity.ok(respuesta);
}
    @PostMapping()
    public ResponseEntity<?> createMiembro(@Valid @RequestBody MiembroEquipo miembro) {
        try {
            MiembroEquipo creado = miembroEquipoService.crearNuevoMiembro(miembro);
            return ResponseEntity.status(201).body(creado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("Error!", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMiembro(@PathVariable Integer id){
        return miembroEquipoService.deleteById(id);
    }
}