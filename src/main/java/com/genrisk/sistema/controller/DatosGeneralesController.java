package com.genrisk.sistema.controller;

import jakarta.validation.Valid;
import com.genrisk.sistema.model.entity.DatosGenerales;
import com.genrisk.sistema.model.entity.weakEntityKey.DatosGeneralesID;
import com.genrisk.sistema.services.DatosGeneralesService;
import com.genrisk.sistema.services.PutCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/datos-generales")
public class DatosGeneralesController {

    private final DatosGeneralesService datosGeneralesService;

    public DatosGeneralesController(DatosGeneralesService datosGeneralesService) {
        this.datosGeneralesService = datosGeneralesService;
    }

    @GetMapping
    public ResponseEntity<List<DatosGenerales>> getAllDatosGenerales() {
        return datosGeneralesService.getAll();
    }

    @PostMapping
    public ResponseEntity<DatosGenerales> createDatosGenerales(@Valid @RequestBody DatosGenerales datosGenerales) {
        // Ejemplo de JSON que envía el ID anidado:
        // {
        //   "idDatosGen": { "itemFormu": 1, "formularioId": 101 },
        //   "edad": 30, "sexo": "Femenino", ...
        // }
        return datosGeneralesService.createPost(datosGenerales);
    }

    //Los metodos se adaptan para la clave compuesta
    @GetMapping("/{itemFormu}/{formularioId}")
    public ResponseEntity<DatosGenerales> getDatosGeneralesById(
            @PathVariable Integer itemFormu,
            @PathVariable Integer formularioId) {
        
        DatosGeneralesID id = new DatosGeneralesID(itemFormu, formularioId);
        return datosGeneralesService.getById(id);
    }

    @PutMapping("/{itemFormu}/{formularioId}")
    public ResponseEntity<DatosGenerales> updateDatosGenerales(
            @PathVariable Integer itemFormu,
            @PathVariable Integer formularioId,
            @Valid @RequestBody DatosGenerales datosDetails) {
        
        DatosGeneralesID id = new DatosGeneralesID(itemFormu, formularioId);
        PutCommand<DatosGenerales, DatosGeneralesID> command = new PutCommand<>();
        command.setId(id);
        command.setNewData(datosDetails);
        
        return datosGeneralesService.updatePut(command);
    }

    @DeleteMapping("/{itemFormu}/{formularioId}")
    public ResponseEntity<Void> deleteDatosGenerales(
            @PathVariable Integer itemFormu,
            @PathVariable Integer formularioId) {
        
        DatosGeneralesID id = new DatosGeneralesID(itemFormu, formularioId);
        return datosGeneralesService.deleteById(id);
    }
}
