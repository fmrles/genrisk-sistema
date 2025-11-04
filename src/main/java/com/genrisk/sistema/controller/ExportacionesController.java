package com.genrisk.sistema.controller;

import com.genrisk.sistema.services.PDFExpServices;
import com.genrisk.sistema.services.WordExpServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/export")
@CrossOrigin(origins = "*")
public class ExportacionesController {

    @Autowired
    @Lazy
    private PDFExpServices pdfExportService;

    @Autowired
    @Lazy
    private WordExpServices wordExportService;

    // =============== EXPORTACIÓN A PDF ===============

    /**
     * Exporta un paciente específico a PDF
     * GET /api/export/paciente/{id}/pdf
     */
    @GetMapping("/paciente/{id}/pdf")
    public ResponseEntity<byte[]> exportarPacientePDF(@PathVariable String id) {
        try {
            byte[] pdfBytes = pdfExportService.exportarPacienteAPDF(id);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", 
                "paciente_" + id + "_" + getTimestamp() + ".pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Exporta lista de todos los pacientes a PDF
     * GET /api/export/pacientes/pdf
     */
    @GetMapping("/pacientes/pdf")
    public ResponseEntity<byte[]> exportarListaPacientesPDF() {
        try {
            byte[] pdfBytes = pdfExportService.exportarListaPacientesAPDF();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", 
                "lista_pacientes_" + getTimestamp() + ".pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Exporta datos generales a PDF
     * GET /api/export/datos-generales/pdf
     */
    @GetMapping("/datos-generales/pdf")
    public ResponseEntity<byte[]> exportarDatosGeneralesPDF() {
        try {
            byte[] pdfBytes = pdfExportService.exportarDatosGeneralesAPDF();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", 
                "datos_generales_" + getTimestamp() + ".pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // =============== EXPORTACIÓN A WORD ===============

    /**
     * Exporta un paciente específico a Word
     * GET /api/export/paciente/{id}/word
     */
    @GetMapping("/paciente/{id}/word")
    public ResponseEntity<byte[]> exportarPacienteWord(@PathVariable String id) {
        try {
            byte[] wordBytes = wordExportService.exportarPacienteAWord(id);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", 
                "paciente_" + id + "_" + getTimestamp() + ".docx");
            headers.setContentLength(wordBytes.length);

            return new ResponseEntity<>(wordBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Exporta lista de todos los pacientes a Word
     * GET /api/export/pacientes/word
     */
    @GetMapping("/pacientes/word")
    public ResponseEntity<byte[]> exportarListaPacientesWord() {
        try {
            byte[] wordBytes = wordExportService.exportarListaPacientesAWord();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", 
                "lista_pacientes_" + getTimestamp() + ".docx");
            headers.setContentLength(wordBytes.length);

            return new ResponseEntity<>(wordBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Exporta datos generales a Word
     * GET /api/export/datos-generales/word
     */
    @GetMapping("/datos-generales/word")
    public ResponseEntity<byte[]> exportarDatosGeneralesWord() {
        try {
            byte[] wordBytes = wordExportService.exportarDatosGeneralesAWord();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", 
                "datos_generales_" + getTimestamp() + ".docx");
            headers.setContentLength(wordBytes.length);

            return new ResponseEntity<>(wordBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/datos-clinicos/word")
    public ResponseEntity<byte[]> exportarDatosClinicosWord() {
        try {
            byte[] wordBytes2 = wordExportService.exportarDatosClinicosAWord();
            
            HttpHeaders headers2 = new HttpHeaders();
            headers2.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers2.setContentDispositionFormData("attachment", 
                "datos_clinicos_" + getTimestamp() + ".docx");
            headers2.setContentLength(wordBytes2.length);

            return new ResponseEntity<>(wordBytes2, headers2, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/habitos-pacientes/word")
    public ResponseEntity<byte[]> exportarHabitosPacientesWord() {
        try {
            byte[] wordBytes = wordExportService.exportarHabitosPacienteAWord();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", 
                "habitos_pacientes_" + getTimestamp() + ".docx");
            headers.setContentLength(wordBytes.length);

            return new ResponseEntity<>(wordBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/fact-dietarios-ambientales/word")
    public ResponseEntity<byte[]> exportarFactDietariosAmbientalesWord() {
        try {
            byte[] wordBytes = wordExportService.exportarFactDietarioAmbientalAWord();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", 
                "fact_dietario_ambiental" + getTimestamp() + ".docx");
            headers.setContentLength(wordBytes.length);

            return new ResponseEntity<>(wordBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/histopatologias/word")
    public ResponseEntity<byte[]> exportarHistopatologiasWord() {
        try {
            byte[] wordBytes = wordExportService.exportarHistopatologiaAWord();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", 
                "histopatologia" + getTimestamp() + ".docx");
            headers.setContentLength(wordBytes.length);

            return new ResponseEntity<>(wordBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // =============== MÉTODOS AUXILIARES ===============

    private String getTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    }

    /**
     * Endpoint de prueba
     * GET /api/export/test
     */
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Export API está funcionando correctamente");
    }
}
