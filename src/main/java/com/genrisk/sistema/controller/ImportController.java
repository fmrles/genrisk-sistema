package com.genrisk.sistema.controller;

import com.genrisk.sistema.services.WordImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/import")
public class ImportController {
    private final WordImportService wordImportService;

    public ImportController(WordImportService wordImportService) {
        this.wordImportService = wordImportService;
    }

    @PostMapping("/word")
    public ResponseEntity<Map<String, String>> importarWord(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        
        if (file.isEmpty()) {
            response.put("error", "El archivo está vacío. Por favor seleccione un documento válido.");
            return ResponseEntity.badRequest().body(response);
        }
        
        try {
            wordImportService.importarWordPaciente(file);
            response.put("mensaje", "¡Éxito! Los datos del paciente han sido actualizados correctamente desde el archivo.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "Error al procesar el archivo: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
