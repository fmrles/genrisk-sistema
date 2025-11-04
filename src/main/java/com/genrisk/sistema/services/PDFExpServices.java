package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.*;
import com.genrisk.sistema.repository.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Lazy
public class PDFExpServices{

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

    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
    private static final Font SUBTITLE_FONT = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLUE);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    private static final Font BOLD_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);

    /**
     * Exporta un reporte completo de paciente a PDF
     */
    public byte[] exportarPacienteAPDF(String idPaciente) throws Exception {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            // Obtener datos del paciente
            Paciente paciente = pacienteRepository.findById(idPaciente)
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

            // Título del documento
            agregarTitulo(document, "Reporte de Paciente");
            document.add(Chunk.NEWLINE);

            // Información del paciente
            agregarSeccionPaciente(document, paciente);
            document.add(Chunk.NEWLINE);

            // Obtener formularios del paciente
            List<Formulario> formularios = formularioRepository.findByPacienteIdPaciente(idPaciente);

            for (Formulario formulario : formularios) {
                agregarSeccionFormulario(document, formulario);
                document.add(Chunk.NEWLINE);
            }

            // Pie de página
            agregarPiePagina(document);

        } finally {
            document.close();
        }

        return baos.toByteArray();
    }

    /**
     * Exporta lista de pacientes a PDF
     */
    public byte[] exportarListaPacientesAPDF() throws Exception {
        Document document = new Document(PageSize.A4.rotate()); // Horizontal
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            agregarTitulo(document, "Listado de Pacientes");
            document.add(Chunk.NEWLINE);

            List<Paciente> pacientes = pacienteRepository.findAll();
            
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Encabezados
            agregarEncabezadoTabla(table, "ID", "Nombre", "Correo", "Dirección", "Tipo");

            // Datos
            for (Paciente paciente : pacientes) {
                agregarCeldaTabla(table, paciente.getIdPaciente());
                agregarCeldaTabla(table, paciente.getNombrePaciente());
                agregarCeldaTabla(table, paciente.getCorreoPaciente());
                agregarCeldaTabla(table, paciente.getDireccionPaciente());
                agregarCeldaTabla(table, paciente.getTipoPaciente());
            }

            document.add(table);
            agregarPiePagina(document);

        } finally {
            document.close();
        }

        return baos.toByteArray();
    }

    /**
     * Exporta datos generales de todos los formularios
     */
    public byte[] exportarDatosGeneralesAPDF() throws Exception {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            agregarTitulo(document, "Datos Generales de Pacientes");
            document.add(Chunk.NEWLINE);

            List<DatosGenerales> datosGenerales = datosGeneralesRepository.findAll();
            
            PdfPTable table = new PdfPTable(10);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);

            // Encabezados
            agregarEncabezadoTabla(table, "Form ID", "Edad", "Sexo", "Peso", "IMC", 
                                  "Estatura", "Zona", "Años Resi.", "Educación", "Ocupación");

            // Datos
            for (DatosGenerales dg : datosGenerales) {
                agregarCeldaTabla(table, String.valueOf(dg.getIdDatosGen().getFormularioId()));
                agregarCeldaTabla(table, String.valueOf(dg.getEdad()));
                agregarCeldaTabla(table, dg.getSexo());
                agregarCeldaTabla(table, String.format("%.1f", dg.getPeso()));
                agregarCeldaTabla(table, String.format("%.1f", dg.getImc()));
                agregarCeldaTabla(table, String.format("%.1f", dg.getEstatura()));
                agregarCeldaTabla(table, dg.getZonaResidencial());
                agregarCeldaTabla(table, String.valueOf(dg.getAniosResiActual()));
                agregarCeldaTabla(table, dg.getEducacion());
                agregarCeldaTabla(table, dg.getOcupacion());
            }

            document.add(table);
            agregarPiePagina(document);

        } finally {
            document.close();
        }

        return baos.toByteArray();
    }

    // ============= MÉTODOS AUXILIARES =============

    private void agregarTitulo(Document document, String titulo) throws DocumentException {
        Paragraph titleParagraph = new Paragraph(titulo, TITLE_FONT);
        titleParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(titleParagraph);
    }

    private void agregarSeccionPaciente(Document document, Paciente paciente) throws DocumentException {
        Paragraph subtitle = new Paragraph("Información del Paciente", SUBTITLE_FONT);
        document.add(subtitle);
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("ID: " + paciente.getIdPaciente(), NORMAL_FONT));
        document.add(new Paragraph("Nombre: " + paciente.getNombrePaciente(), NORMAL_FONT));
        document.add(new Paragraph("Correo: " + paciente.getCorreoPaciente(), NORMAL_FONT));
        document.add(new Paragraph("Dirección: " + paciente.getDireccionPaciente(), NORMAL_FONT));
        document.add(new Paragraph("Tipo: " + paciente.getTipoPaciente(), NORMAL_FONT));
    }

    private void agregarSeccionFormulario(Document document, Formulario formulario) throws DocumentException {
        Paragraph subtitle = new Paragraph("Formulario ID: " + formulario.getIdFormulario(), SUBTITLE_FONT);
        document.add(subtitle);
        document.add(Chunk.NEWLINE);

        // Datos Generales
        datosGeneralesRepository.findByIdDatosGen_FormularioId(formulario.getIdFormulario())
            .ifPresent(dg -> {
                try {
                    agregarDatosGenerales(document, dg);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            });

        // Datos Clínicos
        datosClinicosRepository.findByIdDatosCli_FormularioId(formulario.getIdFormulario())
            .ifPresent(dc -> {
                try {
                    agregarDatosClinicos(document, dc);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            });

        // Hábitos
        habitosPacienteRepository.findByIdHabPaciente_FormularioId(formulario.getIdFormulario())
            .ifPresent(hp -> {
                try {
                    agregarHabitos(document, hp);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            });
    }

    private void agregarDatosGenerales(Document document, DatosGenerales dg) throws DocumentException {
        Paragraph p = new Paragraph("Datos Generales:", BOLD_FONT);
        document.add(p);
        
        document.add(new Paragraph(String.format("Edad: %d años | Sexo: %s | IMC: %.1f", 
            dg.getEdad(), dg.getSexo(), dg.getImc()), NORMAL_FONT));
        document.add(new Paragraph(String.format("Peso: %.1f kg | Estatura: %.1f cm", 
            dg.getPeso(), dg.getEstatura()), NORMAL_FONT));
        document.add(new Paragraph(String.format("Zona: %s | Años residencia: %d", 
            dg.getZonaResidencial(), dg.getAniosResiActual()), NORMAL_FONT));
        document.add(new Paragraph(String.format("Educación: %s | Ocupación: %s", 
            dg.getEducacion(), dg.getOcupacion()), NORMAL_FONT));
        document.add(Chunk.NEWLINE);
    }

    private void agregarDatosClinicos(Document document, DatosClinicos dc) throws DocumentException {
        Paragraph p = new Paragraph("Datos Clínicos:", BOLD_FONT);
        document.add(p);
        
        document.add(new Paragraph(String.format("Adenocarcinoma Gástrico: %s", 
            dc.getAdenoGastrico() != null ? dc.getAdenoGastrico() : "N/A"), NORMAL_FONT));
        document.add(new Paragraph(String.format("Antecedentes Fam. Cáncer Gástrico: %s", 
            dc.getAntFamCancerGast()), NORMAL_FONT));
        document.add(new Paragraph(String.format("H. Pylori - Prueba: %s | Resultado: %s", 
            dc.getHpyloriPrueba(), dc.getHpyloriResultado()), NORMAL_FONT));
        document.add(new Paragraph(String.format("Medicamentos: %s", 
            dc.getMedicamentos()), NORMAL_FONT));
        document.add(Chunk.NEWLINE);
    }

    private void agregarHabitos(Document document, HabitosPaciente hp) throws DocumentException {
        Paragraph p = new Paragraph("Hábitos del Paciente:", BOLD_FONT);
        document.add(p);
        
        document.add(new Paragraph(String.format("Consumo Tabaco: %s | Frecuencia: %s", 
            hp.getEstadoConsumoTabaco(), hp.getCantPromTabaco()), NORMAL_FONT));
        document.add(new Paragraph(String.format("Consumo Alcohol: %s | Frecuencia: %s", 
            hp.getEstadoConsumoAlcohol(), hp.getFrecuenciaAlcohol()), NORMAL_FONT));
        document.add(Chunk.NEWLINE);
    }

    private void agregarPiePagina(Document document) throws DocumentException {
        document.add(Chunk.NEWLINE);
        Paragraph footer = new Paragraph(
            "Generado el: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
            " | Sistema GenRisk", 
            new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, BaseColor.GRAY)
        );
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
    }

    private void agregarEncabezadoTabla(PdfPTable table, String... headers) {
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.WHITE);
        
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(BaseColor.DARK_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
        }
    }

    private void agregarCeldaTabla(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : "N/A", NORMAL_FONT));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
    }
}
