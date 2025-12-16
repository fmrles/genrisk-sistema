package com.genrisk.sistema.controller;

import com.genrisk.sistema.services.PDFExpServices;
import com.genrisk.sistema.services.WordExpServices;
import com.genrisk.sistema.services.ExcelExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
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

    @Autowired
    @Lazy
    private ExcelExportService excelExportService;

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

    @GetMapping("/paciente/{id}/pdf/reclutador")
    public ResponseEntity<byte[]> exportarPacientePDFReclutador(@PathVariable String id) {
        try {
            byte[] pdfBytes = pdfExportService.exportarPacienteAPDFReclutador(id);
            
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
            byte[] pdfBytes = pdfExportService.exportarPacientesAPDF();
            
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

    @GetMapping("/pacientes/pdf/reclutadores")
    public ResponseEntity<byte[]> exportarListaPacientesPDFReclutadores() {
        try {
            byte[] pdfBytes = pdfExportService.exportarPacientesAPDFReclutador();
            
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

    @GetMapping("/datos-clinicos/pdf")
    public ResponseEntity<byte[]> exportarDatosClinicosPDF() {
        try {
            byte[] pdfBytes = pdfExportService.exportarDatosClinicosAPDF();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", 
                "datos_clinicos_" + getTimestamp() + ".pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/datos-clinicos-hpylori/pdf")
    public ResponseEntity<byte[]> exportarDatosClinicosHPyloriPDF() {
        try {
            byte[] pdfBytes = pdfExportService.exportarDatosClinicosHPyloriAPDF();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", 
                "datos_clinicos_hpylori" + getTimestamp() + ".pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/habitos-pacientes/pdf")
    public ResponseEntity<byte[]> exportarHabitosAPDF() {
        try {
            byte[] pdfBytes = pdfExportService.exportarHabitosAPDF();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", 
                "habitos_paciente_" + getTimestamp() + ".pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/fact-dietarios-ambientales/pdf")
    public ResponseEntity<byte[]> exportarFactoresAPDF() {
        try {
            byte[] pdfBytes = pdfExportService.exportarFactDietarioAmbientalAPDF();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", 
                "fact_dietario_ambiental_" + getTimestamp() + ".pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/histopatologias/pdf")
    public ResponseEntity<byte[]> exportarHistoAPDF() {
        try {
            byte[] pdfBytes = pdfExportService.exportarHistopatologiaAPDF();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", 
                "histopatologia" + getTimestamp() + ".pdf");
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
            return construirRespuestaWord(wordBytes, "Reporte_Paciente_" + id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/paciente/{id}/word/reclutadores")
    public ResponseEntity<byte[]> exportarPacienteWordReclutadores(@PathVariable String id) {
        try {
            byte[] wordBytes = wordExportService.exportarPacienteAWordReclutador(id);
            return construirRespuestaWord(wordBytes, "paciente_" + id + "_reclutador");
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
            return construirRespuestaWord(wordBytes, "Listado_Pacientes");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/pacientes/word/reclutadores")
    public ResponseEntity<byte[]> exportarListaPacientesWordReclutadores() {
        try {
            byte[] wordBytes = wordExportService.exportarListaPacientesAWordReclutadores();
            
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
            return construirRespuestaWord(wordBytes, "datos_generales");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/datos-clinicos/word")
    public ResponseEntity<byte[]> exportarDatosClinicosWord() {
        try {
            byte[] wordBytes = wordExportService.exportarDatosClinicosAWord();
            return construirRespuestaWord(wordBytes, "datos_clinicos");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/habitos-pacientes/word")
    public ResponseEntity<byte[]> exportarHabitosPacientesWord() {
        try {
            byte[] wordBytes = wordExportService.exportarHabitosPacienteAWord();
            return construirRespuestaWord(wordBytes, "habitos_pacientes");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/fact-dietarios-ambientales/word")
    public ResponseEntity<byte[]> exportarFactDietariosAmbientalesWord() {
        try {
            byte[] wordBytes = wordExportService.exportarFactDietarioAmbientalAWord();
            return construirRespuestaWord(wordBytes, "fact_dietario_ambiental");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/histopatologias/word")
    public ResponseEntity<byte[]> exportarHistopatologiasWord() {
        try {
            byte[] wordBytes = wordExportService.exportarHistopatologiaAWord();
            return construirRespuestaWord(wordBytes, "histopatologia");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ============ EXPORTACIÓN A EXCEL ================

    /**
     * Exporta los datos dicotomizados (formato pivot) a Excel (XLSX).
     * GET /api/export/dicotomizacion/excel
     */
    @GetMapping("/dicotomizacion/excel")
    public ResponseEntity<byte[]> exportarDicotomizacionExcel() {
        try {
            byte[] excelBytes = excelExportService.exportarDicotomizacionAExcel();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", 
                "dicotomizacion_export_" + getTimestamp() + ".xlsx"); // IMPORTANTE: .xlsx
            headers.setContentLength(excelBytes.length);

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/dicotomizacion/{pacienteId}/excel")
    public ResponseEntity<byte[]> exportarDicotomizacionExcelPaciente(
            @PathVariable String pacienteId) {
        try {
            byte[] excelBytes = excelExportService.exportarDicotomizacionAExcel(pacienteId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", 
                "dicotomizacion_export_" + pacienteId + "_" + getTimestamp() + ".xlsx");
            headers.setContentLength(excelBytes.length);

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/dicotomizacion/conjunto/{conjuntoId}/excel")
    public ResponseEntity<byte[]> exportarDicotomizacionExcelConjunto(
            @PathVariable Integer conjuntoId) {
        try {
            byte[] excelBytes = excelExportService.exportarDicotomizacionAExcelPorConjunto(conjuntoId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", 
                "dicotomizacion_export_conjunto_" + conjuntoId + "_" + getTimestamp() + ".xlsx");
            headers.setContentLength(excelBytes.length);

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // =============== MÉTODOS AUXILIARES ===============

    private String getTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    }

    private ResponseEntity<byte[]> construirRespuestaWord(byte[] contenido, String nombreBase) {
        String filename = nombreBase + "_" + getTimestamp() + ".docx";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentLength(contenido.length);

        return new ResponseEntity<>(contenido, headers, HttpStatus.OK);
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
