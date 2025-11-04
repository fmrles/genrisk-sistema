package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.*;
import com.genrisk.sistema.repository.*;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Lazy
public class WordExpServices {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private FormularioRepository formularioRepository;

    @Autowired
    private DatosGeneralesRepository datosGeneralesRepository;

    @Autowired
    private DatosClinicosRepository datosClinicosRepository;

    @Autowired
    private HabitosPacienteRepository habitosPacienteRepository;

    /**
     * Exporta un reporte completo de paciente a Word
     */
    public byte[] exportarPacienteAWord(String idPaciente) throws Exception {
        XWPFDocument document = new XWPFDocument();
        
        try {
            // Obtener datos del paciente
            Paciente paciente = pacienteRepository.findById(idPaciente)
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

            // Título del documento
            agregarTitulo(document, "Reporte de Paciente");
            agregarSaltoLinea(document);

            // Información del paciente
            agregarSeccionPaciente(document, paciente);
            agregarSaltoLinea(document);

            // Obtener formularios del paciente
            List<Formulario> formularios = formularioRepository.findByPaciente_id(idPaciente);

            for (Formulario formulario : formularios) {
                agregarSeccionFormulario(document, formulario);
                agregarSaltoLinea(document);
            }

            // Pie de página
            agregarPiePagina(document);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.write(baos);
            return baos.toByteArray();

        } finally {
            document.close();
        }
    }

    /**
     * Exporta lista de pacientes a Word
     */
    public byte[] exportarListaPacientesAWord() throws Exception {
        XWPFDocument document = new XWPFDocument();
        
        try {
            agregarTitulo(document, "Listado de Pacientes");
            agregarSaltoLinea(document);

            List<Paciente> pacientes = pacienteRepository.findAll();
            
            // Crear tabla
            XWPFTable table = document.createTable(pacientes.size() + 1, 5);
            table.setWidth("100%");

            // Encabezados
            XWPFTableRow headerRow = table.getRow(0);
            configurarCeldaEncabezado(headerRow.getCell(0), "ID");
            configurarCeldaEncabezado(headerRow.getCell(1), "Nombre");
            configurarCeldaEncabezado(headerRow.getCell(2), "Correo");
            configurarCeldaEncabezado(headerRow.getCell(3), "Dirección");
            configurarCeldaEncabezado(headerRow.getCell(4), "Tipo");

            // Datos
            int rowIndex = 1;
            for (Paciente paciente : pacientes) {
                XWPFTableRow row = table.getRow(rowIndex);
                configurarCeldaDatos(row.getCell(0), paciente.getIdPaciente());
                configurarCeldaDatos(row.getCell(1), paciente.getNombrePaciente());
                configurarCeldaDatos(row.getCell(2), paciente.getCorreoPaciente());
                configurarCeldaDatos(row.getCell(3), paciente.getDireccionPaciente());
                configurarCeldaDatos(row.getCell(4), paciente.getTipoPaciente());
                rowIndex++;
            }

            agregarSaltoLinea(document);
            agregarPiePagina(document);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.write(baos);
            return baos.toByteArray();

        } finally {
            document.close();
        }
    }

    /**
     * Exporta datos generales de todos los formularios
     */
    public byte[] exportarDatosGeneralesAWord() throws Exception {
        XWPFDocument document = new XWPFDocument();
        
        try {
            agregarTitulo(document, "Datos Generales de Pacientes");
            agregarSaltoLinea(document);

            List<DatosGenerales> datosGenerales = datosGeneralesRepository.findAll();
            
            // Crear tabla
            XWPFTable table = document.createTable(datosGenerales.size() + 1, 10);
            table.setWidth("100%");

            // Encabezados
            XWPFTableRow headerRow = table.getRow(0);
            configurarCeldaEncabezado(headerRow.getCell(0), "Form ID");
            configurarCeldaEncabezado(headerRow.getCell(1), "Edad");
            configurarCeldaEncabezado(headerRow.getCell(2), "Sexo");
            configurarCeldaEncabezado(headerRow.getCell(3), "Peso");
            configurarCeldaEncabezado(headerRow.getCell(4), "IMC");
            configurarCeldaEncabezado(headerRow.getCell(5), "Estatura");
            configurarCeldaEncabezado(headerRow.getCell(6), "Zona");
            configurarCeldaEncabezado(headerRow.getCell(7), "Años Resi.");
            configurarCeldaEncabezado(headerRow.getCell(8), "Educación");
            configurarCeldaEncabezado(headerRow.getCell(9), "Ocupación");

            // Datos
            int rowIndex = 1;
            for (DatosGenerales dg : datosGenerales) {
                XWPFTableRow row = table.getRow(rowIndex);
                configurarCeldaDatos(row.getCell(0), String.valueOf(dg.getIdDatosGen().getFormularioId()));
                configurarCeldaDatos(row.getCell(1), String.valueOf(dg.getEdad()));
                configurarCeldaDatos(row.getCell(2), dg.getSexo());
                configurarCeldaDatos(row.getCell(3), String.format("%.1f", dg.getPeso()));
                configurarCeldaDatos(row.getCell(4), String.format("%.1f", dg.getImc()));
                configurarCeldaDatos(row.getCell(5), String.format("%.1f", dg.getEstatura()));
                configurarCeldaDatos(row.getCell(6), dg.getZonaResidencial());
                configurarCeldaDatos(row.getCell(7), String.valueOf(dg.getAniosResiActual()));
                configurarCeldaDatos(row.getCell(8), dg.getEducacion());
                configurarCeldaDatos(row.getCell(9), dg.getOcupacion());
                rowIndex++;
            }

            agregarSaltoLinea(document);
            agregarPiePagina(document);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.write(baos);
            return baos.toByteArray();

        } finally {
            document.close();
        }
    }

    // ============= MÉTODOS AUXILIARES =============

    private void agregarTitulo(XWPFDocument document, String titulo) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        
        XWPFRun run = paragraph.createRun();
        run.setText(titulo);
        run.setBold(true);
        run.setFontSize(18);
        run.setColor("2E4053");
    }

    private void agregarSubtitulo(XWPFDocument document, String subtitulo) {
        XWPFParagraph paragraph = document.createParagraph();
        
        XWPFRun run = paragraph.createRun();
        run.setText(subtitulo);
        run.setBold(true);
        run.setFontSize(14);
        run.setColor("3498DB");
    }

    private void agregarTexto(XWPFDocument document, String texto) {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText(texto);
        run.setFontSize(11);
    }

    private void agregarTextoNegrita(XWPFDocument document, String texto) {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText(texto);
        run.setBold(true);
        run.setFontSize(11);
    }

    private void agregarSaltoLinea(XWPFDocument document) {
        document.createParagraph();
    }

    private void agregarSeccionPaciente(XWPFDocument document, Paciente paciente) {
        agregarSubtitulo(document, "Información del Paciente");
        agregarSaltoLinea(document);

        agregarTexto(document, "ID: " + paciente.getIdPaciente());
        agregarTexto(document, "Nombre: " + paciente.getNombrePaciente());
        agregarTexto(document, "Correo: " + paciente.getCorreoPaciente());
        agregarTexto(document, "Dirección: " + paciente.getDireccionPaciente());
        agregarTexto(document, "Tipo: " + paciente.getTipoPaciente());
    }

    private void agregarSeccionFormulario(XWPFDocument document, Formulario formulario) {
        agregarSubtitulo(document, "Formulario ID: " + formulario.getIdFormulario());
        agregarSaltoLinea(document);

        // Datos Generales
        datosGeneralesRepository.findByIdDatosGen_FormularioId(formulario.getIdFormulario())
            .ifPresent(dg -> agregarDatosGenerales(document, dg));

        // Datos Clínicos
        datosClinicosRepository.findByIdDatosCli_FormularioId(formulario.getIdFormulario())
            .ifPresent(dc -> agregarDatosClinicos(document, dc));

        // Hábitos
        habitosPacienteRepository.findByIdHabPaciente_FormularioId(formulario.getIdFormulario())
            .ifPresent(hp -> agregarHabitos(document, hp));
    }

    private void agregarDatosGenerales(XWPFDocument document, DatosGenerales dg) {
        agregarTextoNegrita(document, "Datos Generales:");
        
        agregarTexto(document, String.format("Edad: %d años | Sexo: %s | IMC: %.1f", 
            dg.getEdad(), dg.getSexo(), dg.getImc()));
        agregarTexto(document, String.format("Peso: %.1f kg | Estatura: %.1f cm", 
            dg.getPeso(), dg.getEstatura()));
        agregarTexto(document, String.format("Zona: %s | Años residencia: %d", 
            dg.getZonaResidencial(), dg.getAniosResiActual()));
        agregarTexto(document, String.format("Educación: %s | Ocupación: %s", 
            dg.getEducacion(), dg.getOcupacion()));
        agregarSaltoLinea(document);
    }

    private void agregarDatosClinicos(XWPFDocument document, DatosClinicos dc) {
        agregarTextoNegrita(document, "Datos Clínicos:");
        
        agregarTexto(document, String.format("Adenocarcinoma Gástrico: %s", 
            dc.getAdenoGastrico() != null ? dc.getAdenoGastrico() : "N/A"));
        agregarTexto(document, String.format("Antecedentes Fam. Cáncer Gástrico: %s", 
            dc.getAntFamCancerGast()));
        agregarTexto(document, String.format("H. Pylori - Prueba: %s | Resultado: %s", 
            dc.getHpyloriPrueba(), dc.getHpyloriResultado()));
        agregarTexto(document, String.format("Medicamentos: %s", 
            dc.getMedicamentos()));
        agregarSaltoLinea(document);
    }

    private void agregarHabitos(XWPFDocument document, HabitosPaciente hp) {
        agregarTextoNegrita(document, "Hábitos del Paciente:");
        
        agregarTexto(document, String.format("Consumo Tabaco: %s | Frecuencia: %s", 
            hp.getEstadoConsumoTabaco(), hp.getCantPromTabaco()));
        agregarTexto(document, String.format("Consumo Alcohol: %s | Frecuencia: %s", 
            hp.getEstadoConsumoAlcohol(), hp.getFrecuenciaAlcohol()));
        agregarSaltoLinea(document);
    }

    private void agregarPiePagina(XWPFDocument document) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        
        XWPFRun run = paragraph.createRun();
        run.setText("Generado el: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                   " | Sistema GenRisk");
        run.setFontSize(9);
        run.setItalic(true);
        run.setColor("808080");
    }

    private void configurarCeldaEncabezado(XWPFTableCell cell, String texto) {
        cell.setColor("2C3E50");
        XWPFParagraph paragraph = cell.getParagraphs().get(0);
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        
        XWPFRun run = paragraph.createRun();
        run.setText(texto);
        run.setBold(true);
        run.setColor("FFFFFF");
        run.setFontSize(10);
    }

    private void configurarCeldaDatos(XWPFTableCell cell, String texto) {
        XWPFParagraph paragraph = cell.getParagraphs().get(0);
        
        XWPFRun run = paragraph.createRun();
        run.setText(texto != null ? texto : "N/A");
        run.setFontSize(10);
    }
}

