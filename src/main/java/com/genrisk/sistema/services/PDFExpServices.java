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

    @Autowired private PacienteRepository pacienteRepository;
    @Autowired private FormularioRepository formularioRepository;
    @Autowired private DatosGeneralesRepository datosGeneralesRepository;
    @Autowired private DatosClinicosRepository datosClinicosRepository;
    @Autowired private HabitosPacienteRepository habitosPacienteRepository;
    @Autowired private FactDietariosAmbientalesRepository factDietariosAmbientalesRepository;
    @Autowired private HistopatologiaRepository histopatologiaRepository;

    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
    private static final Font SUBTITLE_FONT = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLUE);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    private static final Font BOLD_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);

    public byte[] exportarPacienteAPDF(String idPaciente) throws Exception {
        // Documento en horizontal para más espacio
        Document document = new Document(PageSize.A4); 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            Paciente paciente = pacienteRepository.findById(idPaciente)
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

            agregarTitulo(document, "Reporte Detallado de Paciente");
            document.add(Chunk.NEWLINE);

            agregarSeccionPaciente(document, paciente);
            document.add(Chunk.NEWLINE);

            List<Formulario> formularios = formularioRepository.findByPacienteIdPaciente(idPaciente);

            for (Formulario formulario : formularios) {
                agregarSeccionFormulario(document, formulario);
                document.add(Chunk.NEWLINE);
            }

            agregarPiePagina(document);

        } finally {
            document.close();
        }

        return baos.toByteArray();
    } 

    public byte[] exportarPacienteAPDFReclutador(String idPaciente) throws Exception {
        // Documento en horizontal para más espacio
        Document document = new Document(PageSize.A4); 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            Paciente paciente = pacienteRepository.findById(idPaciente)
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

            agregarTitulo(document, "Reporte Detallado de Paciente");
            document.add(Chunk.NEWLINE);

            agregarSeccionPacienteReclutador(document, paciente);
            document.add(Chunk.NEWLINE);

            List<Formulario> formularios = formularioRepository.findByPacienteIdPaciente(idPaciente);

            for (Formulario formulario : formularios) {
                agregarSeccionFormulario(document, formulario);
                document.add(Chunk.NEWLINE);
            }

            agregarPiePagina(document);

        } finally {
            document.close();
        }

        return baos.toByteArray();
    }

    private void agregarTitulo(com.itextpdf.text.Document document, String titulo) throws DocumentException {
        Paragraph titleParagraph = new Paragraph(titulo, TITLE_FONT);
        titleParagraph.setAlignment(Element.ALIGN_CENTER);
        titleParagraph.setSpacingAfter(10f);
        document.add(titleParagraph);
    }

    public byte[] exportarPacientesAPDF() throws Exception {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            agregarTitulo(document, "Listado General de Pacientes");
            document.add(Chunk.NEWLINE);

            List<Paciente> pacientes = pacienteRepository.findAll();

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            agregarEncabezadoTabla(table, "ID", "Nombre", "Correo", "Dirección", "Tipo", "Fecha de Inclusión");

            for (Paciente paciente : pacientes) {
                agregarCeldaTabla(table, safe(paciente.getIdPaciente()));
                agregarCeldaTabla(table, safe(paciente.getNombrePaciente()));
                agregarCeldaTabla(table, safe(paciente.getCorreoPaciente()));
                agregarCeldaTabla(table, safe(paciente.getDireccionPaciente()));
                agregarCeldaTabla(table, safe(paciente.getTipoPaciente()));
                agregarCeldaTabla(table, safe(paciente.getFechaInclusion()));
            }

            document.add(table);
            agregarPiePagina(document);

        } finally {
            document.close();
        }
        return baos.toByteArray();
    }

     public byte[] exportarPacientesAPDFReclutador() throws Exception {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            agregarTitulo(document, "Listado General de Pacientes");
            document.add(Chunk.NEWLINE);

            List<Paciente> pacientes = pacienteRepository.findAll();

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            agregarEncabezadoTabla(table, "ID", "Tipo", "Fecha de Inclusión");

            for (Paciente paciente : pacientes) {
                agregarCeldaTabla(table, safe(paciente.getIdPaciente()));
                agregarCeldaTabla(table, safe(paciente.getTipoPaciente()));
                agregarCeldaTabla(table, safe(paciente.getFechaInclusion()));
            }

            document.add(table);
            agregarPiePagina(document);

        } finally {
            document.close();
        }
        return baos.toByteArray();
    }
    
    public byte[] exportarDatosGeneralesAPDF() throws Exception {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, baos);
            document.open();
            agregarTitulo(document, "Datos Generales de Pacientes");
            PdfPTable table = createPdfTableForDatosGenerales(); // Usamos el método helper
            document.add(table);
            agregarPiePagina(document);
        } finally {
            document.close();
        }
        return baos.toByteArray();
    }

    public byte[] exportarDatosClinicosAPDF() throws Exception {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, baos);
            document.open();
            agregarTitulo(document, "Datos Clínicos de Pacientes");
            PdfPTable table = createPdfTableForDatosClinicos();
            document.add(table);
            agregarPiePagina(document);
        } finally {
            document.close();
        }
        return baos.toByteArray();
    }

    public byte[] exportarHabitosAPDF() throws Exception {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, baos);
            document.open();
            agregarTitulo(document, "Hábitos de Pacientes");
            PdfPTable table = createPdfTableForHabitosPaciente();
            document.add(table);
            agregarPiePagina(document);
        } finally {
            document.close();
        }
        return baos.toByteArray();
    }

    public byte[] exportarFactDietarioAmbientalAPDF() throws Exception {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, baos);
            document.open();
            agregarTitulo(document, "Factores Dietario / Ambientales");
            PdfPTable table = createPdfTableForFactDietarioAmbiental();
            document.add(table);
            agregarPiePagina(document);
        } finally {
            document.close();
        }
        return baos.toByteArray();
    }

    public byte[] exportarHistopatologiaAPDF() throws Exception {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, baos);
            document.open();
            agregarTitulo(document, "Histopatología");
            PdfPTable table = createPdfTableForHistopatologia();
            document.add(table);
            agregarPiePagina(document);
        } finally {
            document.close();
        }
        return baos.toByteArray();
    }

    // ============= MÉTODOS AUXILIARES =============

    private PdfPTable createPdfTableForDatosGenerales() {
        List<DatosGenerales> datosGenerales = datosGeneralesRepository.findAll();
        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100);
        // Ajuste de ancho de columnas para mejor visualización
        try { table.setWidths(new float[] {1f, 1f, 1f, 1f, 1f, 1f, 1.5f, 1f, 1.5f, 2f}); } catch (DocumentException e) { /* ignore */ }

        agregarEncabezadoTabla(table,
             "Form ID", "Edad", "Sexo", "Peso (kg)", "IMC", "Estatura (cm)",
            "Zona Residencial", "Años Resi.", "Educación", "Ocupación");

        for (DatosGenerales dg : datosGenerales) {
            agregarCeldaTabla(table, safe(dg.getIdDatosGen().getFormularioId()));
            agregarCeldaTabla(table, safe(dg.getEdad()));
            agregarCeldaTabla(table, safe(dg.getSexo()));
            agregarCeldaTabla(table, safeDouble(dg.getPeso()));
            agregarCeldaTabla(table, safeDouble(dg.getImc()));
            agregarCeldaTabla(table, safeDouble(dg.getEstatura()));
            agregarCeldaTabla(table, safe(dg.getZonaResidencial()));
            agregarCeldaTabla(table, safe(dg.getEducacion()));
            agregarCeldaTabla(table, safe(dg.getOcupacion()));
        }
        return table;
    }

    private PdfPTable createPdfTableForDatosClinicos() {
        List<DatosClinicos> list = datosClinicosRepository.findAll();
        // Reducido a 10 columnas para caber en A4 horizontal, Cirugia y Otros consolidado
        PdfPTable table = new PdfPTable(9); 
        table.setWidthPercentage(100);
        
        agregarEncabezadoTabla(table,
            "Form ID", 
            "AdenoGástrico", 
            "Fecha Adeno", 
            "Ant. Fam. Cán. Gást.", 
            "Medicamentos", 
            "Otras Enf.", 
            "H. Pylori (P/R/T)", 
            "Cirugía Previa", 
            "Ant. Fam. Otro Cáncer");

        for (DatosClinicos dc : list) {
            agregarCeldaTabla(table, safe(dc.getIdDatosCli().getFormularioId()));
            agregarCeldaTabla(table, safe(dc.getAdenoGastrico()));
            agregarCeldaTabla(table, safe(dc.getFechaAdenoGastrico()));
            agregarCeldaTabla(table, safe(dc.getAntFamCancerGast()));
            agregarCeldaTabla(table, safe(dc.getMedicamentos()));
            agregarCeldaTabla(table, safe(dc.getOtrasEnfermedades()));
            
            // Consolidación de H. Pylori (las tres variables en una celda)
            agregarCeldaTabla(table, safe(dc.getHpyloriPrueba()) + "/" + safe(dc.getHpyloriResultado()) + "/" + safe(dc.getHpyloriTiempoTest()));
            
            agregarCeldaTabla(table, safe(dc.getCirugiaGastricaPrevia()));
            // Esta celda estaba duplicando el AntFamOtroCancer en el código anterior, ahora está correcta:
            agregarCeldaTabla(table, safe(dc.getAntFamOtroCancer()));

        }
        return table;
    }

    private PdfPTable createPdfTableForHabitosPaciente() {
        List<HabitosPaciente> list = habitosPacienteRepository.findAll();
        
        PdfPTable table = new PdfPTable(6); 
        table.setWidthPercentage(100);
        
        agregarEncabezadoTabla(table,
             "Form ID", "Estado Tabaco", "Tiempo Tab. (meses)", 
            "Estado Alcohol", "Frec. Alcohol", "Años Consumo Alcohol");

        for (HabitosPaciente h : list) {
            agregarCeldaTabla(table, safe(h.getIdHabPaciente().getFormularioId()));
            agregarCeldaTabla(table, safe(h.getEstadoConsumoTabaco()));
            agregarCeldaTabla(table, safe(h.getTiempoTabaco()));
            agregarCeldaTabla(table, safe(h.getEstadoConsumoAlcohol()));
            agregarCeldaTabla(table, safe(h.getFrecuenciaAlcohol()));
            agregarCeldaTabla(table, safe(h.getAniosConsumoAlcohol()));
        }
        return table;
    }

    private PdfPTable createPdfTableForFactDietarioAmbiental() {
        List<FactDietariosAmbientales> list = factDietariosAmbientalesRepository.findAll();
        // Reducido a 8 columnas clave para evitar desborde
        PdfPTable table = new PdfPTable(7); 
        table.setWidthPercentage(100);
        
        agregarEncabezadoTabla(table,
            "Form ID", "Agua Consumo", "Fumigaciones", "Exp. Pesticidas",
            "Exp. Químicos", "Dieta Agrega Sal", "Dieta Frituras");

        for (FactDietariosAmbientales f : list) {
            agregarCeldaTabla(table, safe(f.getIdFact().getFormularioId()));
            agregarCeldaTabla(table, safe(f.getAguaConsumoZona()));
            agregarCeldaTabla(table, safe(f.getFumigaciones()));
            agregarCeldaTabla(table, safe(f.getExposicionPesticidas()));
            agregarCeldaTabla(table, safe(f.getExposicionQuimicos()));
            agregarCeldaTabla(table, safe(f.getDietaAgregaSal()));
            agregarCeldaTabla(table, safe(f.getDietaFrituras()));
        }
        return table;
    }

    private PdfPTable createPdfTableForHistopatologia() {
        List<Histopatologia> list = histopatologiaRepository.findAll();
        PdfPTable table = new PdfPTable(4); // 4 columnas
        table.setWidthPercentage(100);

        agregarEncabezadoTabla(table, "Formulario ID", "Tipo", "Estado Clínico", "Localización Tumoral");

        for (Histopatologia h : list) {
            agregarCeldaTabla(table, safe(h.getHistoID().getFormularioId()));
            agregarCeldaTabla(table, safe(h.getTipo()));
            agregarCeldaTabla(table, safe(h.getEstadoClinico()));
            agregarCeldaTabla(table, safe(h.getLocaliTumoral()));
        }
        return table;
    }

    // ============= MÉTODOS EXTRA =============

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

        // Factores Dietario Ambientales
        factDietariosAmbientalesRepository.findByIdFact_FormularioId(formulario.getIdFormulario())
            .ifPresent(fda -> {
                try {
                    agregarFactoresDietariosAmbientales(document, fda);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            });
        
         // Histopatologia
        histopatologiaRepository.findByHistoID_FormularioId(formulario.getIdFormulario())
            .ifPresent(h -> {
            try {
                agregarHistopatologia(document, h);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        });

    }

    private void agregarDatosGenerales(Document document, DatosGenerales dg) throws DocumentException {
        Paragraph p = new Paragraph("Datos Generales:", BOLD_FONT);
        document.add(p);

        document.add(new Paragraph(String.format("Edad: %d años | Sexo: %s | IMC: %s",
            dg.getEdad() != null ? dg.getEdad() : 0, safe(dg.getSexo()),
            dg.getImc() != null ? String.format("%.1f", dg.getImc()) : "N/A"), NORMAL_FONT));
        document.add(new Paragraph(String.format("Peso: %s kg | Estatura: %s cm",
            dg.getPeso() != null ? String.format("%.1f", dg.getPeso()) : "N/A",
            dg.getEstatura() != null ? String.format("%.1f", dg.getEstatura()) : "N/A"), NORMAL_FONT));
        document.add(new Paragraph(String.format("Zona: %s", safe(dg.getZonaResidencial()), NORMAL_FONT)));
        document.add(new Paragraph(String.format("Educación: %s | Ocupación: %s",
            safe(dg.getEducacion()), safe(dg.getOcupacion())), NORMAL_FONT));
        document.add(Chunk.NEWLINE);
    }

    private void agregarDatosClinicos(Document document, DatosClinicos dc) throws DocumentException {
        Paragraph p = new Paragraph("Datos Clínicos:", BOLD_FONT);
        document.add(p);

        document.add(new Paragraph(String.format("Adenocarcinoma Gástrico: %s | Fecha Adeno Gástrico: %s",
            safe(dc.getAdenoGastrico()), dc.getFechaAdenoGastrico() != null ? dc.getFechaAdenoGastrico().toString() : "N/A"), NORMAL_FONT));
        document.add(new Paragraph(String.format("Cirugía Gástrica Previa: %s", safe(dc.getCirugiaGastricaPrevia())), NORMAL_FONT));
        document.add(new Paragraph(String.format("Antecedentes Fam. Cáncer Gástrico: %s", safe(dc.getAntFamCancerGast())), NORMAL_FONT));
        document.add(new Paragraph(String.format("H. Pylori - Prueba: %s | Resultado: %s | Tiempo(meses): %s",
            safe(dc.getHpyloriPrueba()), safe(dc.getHpyloriResultado()), safe(dc.getHpyloriTiempoTest())), NORMAL_FONT));
        document.add(new Paragraph(String.format("Medicamentos: %s", safe(dc.getMedicamentos())), NORMAL_FONT));
        document.add(new Paragraph(String.format("Antecedentes Fam. Cáncer: %s", safe(dc.getAntFamOtroCancer())), NORMAL_FONT));
        document.add(new Paragraph(String.format("Otras Enfermedades: %s", safe(dc.getOtrasEnfermedades())), NORMAL_FONT));
        document.add(Chunk.NEWLINE);
    }

    private void agregarHabitos(Document document, HabitosPaciente hp) throws DocumentException {
        Paragraph p = new Paragraph("Hábitos del Paciente:", BOLD_FONT);
        document.add(p);

        document.add(new Paragraph(String.format("Consumo Tabaco: %s | Frecuencia/Promedio: %s",
            safe(hp.getEstadoConsumoTabaco()), hp.getCantPromTabaco() != null ? String.valueOf(hp.getCantPromTabaco()) : "N/A"), NORMAL_FONT));
        document.add(new Paragraph(String.format("Consumo Alcohol: %s | Frecuencia: %s",
            safe(hp.getEstadoConsumoAlcohol()), safe(hp.getFrecuenciaAlcohol())), NORMAL_FONT));
        document.add(Chunk.NEWLINE);
    }

    private void agregarFactoresDietariosAmbientales(Document document, FactDietariosAmbientales fda) throws DocumentException {
        Paragraph p = new Paragraph("Factores Dietario-Ambiental del Paciente:", BOLD_FONT);
        document.add(p);

        document.add(new Paragraph(String.format("Consumo Agua: %s | Tratamiento Agua: %s",
            safe(fda.getTratamientoAgua() != null ? fda.getTratamientoAgua() : fda.getAguaConsumoZona()),
            safe(fda.getTratamientoAgua())), NORMAL_FONT));

        document.add(new Paragraph(String.format("Fumigaciones: %s | ExposicionPesticidas: %s | CombustiónLeña: %s",
            safe(fda.getFumigaciones()), safe(fda.getExposicionPesticidas()), safe(fda.getCombusLenaDiario())), NORMAL_FONT));

        document.add(new Paragraph(String.format("ExposiciónQuímicos: %s | AgregaSal: %s | ConsumoFrutaVerdura: %s",
            safe(fda.getExposicionQuimicos()), safe(fda.getDietaAgregaSal() != null ? fda.getDietaAgregaSal() : fda.getDietaAgregaSal()),
            safe(fda.getDietaFrutasVerduras())), NORMAL_FONT));

        document.add(new Paragraph(String.format("ConsumoFrituras: %s | ConsumoCarnesCecinas: %s",
            safe(fda.getDietaFrituras()), safe(fda.getDietaCarnesCecinas())), NORMAL_FONT));

        document.add(Chunk.NEWLINE);
    }

    private void agregarHistopatologia(Document document, Histopatologia hp) throws DocumentException {
        Paragraph p = new Paragraph("Histopatología:", BOLD_FONT);
        document.add(p);

        document.add(new Paragraph(String.format("Tipo: %s | Estado Clínico: %s | Localización Tumoral: %s",
            safe(hp.getTipo()), safe(hp.getEstadoClinico()), safe(hp.getLocaliTumoral())), NORMAL_FONT));
        document.add(Chunk.NEWLINE);
    }

    private void agregarSeccionPaciente(Document document, Paciente paciente) throws DocumentException {
        Paragraph subtitle = new Paragraph("Información del Paciente", SUBTITLE_FONT);
        document.add(subtitle);
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("ID: " + safe(paciente.getIdPaciente()), NORMAL_FONT));
        document.add(new Paragraph("Nombre: " + safe(paciente.getNombrePaciente()), NORMAL_FONT));
        document.add(new Paragraph("Correo: " + safe(paciente.getCorreoPaciente()), NORMAL_FONT));
        document.add(new Paragraph("Dirección: " + safe(paciente.getDireccionPaciente()), NORMAL_FONT));
        document.add(new Paragraph("Tipo de Paciente: " + safe(paciente.getTipoPaciente()), NORMAL_FONT));
        document.add(new Paragraph("Fecha de Inclusión: " +safe(paciente.getFechaInclusion()), NORMAL_FONT));
    }

    private void agregarSeccionPacienteReclutador(Document document, Paciente paciente) throws DocumentException {
        Paragraph subtitle = new Paragraph("Información del Paciente", SUBTITLE_FONT);
        document.add(subtitle);
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("ID: " + safe(paciente.getIdPaciente()), NORMAL_FONT));
        document.add(new Paragraph("Tipo de Paciente: " + safe(paciente.getTipoPaciente()), NORMAL_FONT));
        document.add(new Paragraph("Fecha de Inclusión: " +safe(paciente.getFechaInclusion()), NORMAL_FONT));
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

    private String safe(Object obj) {
        return obj == null ? "N/A" : String.valueOf(obj);
    }
    
    private String safeDouble(Double d) {
        return d == null ? "N/A" : String.format("%.1f", d);
    }
}