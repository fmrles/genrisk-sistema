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

    public byte[] exportarDatosClinicosHPyloriAPDF() throws Exception {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, baos);
            document.open();
            agregarTitulo(document, "Datos Clínicos HPylori de Pacientes");
            PdfPTable table = createPdfTableForDatosClinicosHPylori();
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
            agregarTitulo(document, "Factores Dietario / Ambientales de Pacientes");
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
            agregarTitulo(document, "Histopatologías de Pacientes");
            PdfPTable table = createPdfTableForHistopatologia();
            document.add(table);
            agregarPiePagina(document);
        } finally {
            document.close();
        }
        return baos.toByteArray();
    }

    // ============= MÉTODOS CREADORES =============

    private PdfPTable createPdfTableForDatosGenerales() {
        List<DatosGenerales> datosGenerales = datosGeneralesRepository.findAll();
        PdfPTable table = new PdfPTable(16);
        table.setWidthPercentage(100);
        // Ajuste de ancho de columnas para mejor visualización
        try { table.setWidths(new float[] {0.7f, 0.7f, 0.6f, 0.6f, 0.6f, 0.5f, 0.5f, 1f, 1f, 1f, 1f, 1f, 0.7f, 1f, 1f, 1f}); } catch (DocumentException e) { /* ignore */ }



        agregarEncabezadoTabla(table,
             "FormID", "PacID", "Edad", "Sexo", "Peso", "IMC", "Estatura", "Nacionalidad", "Dirección", "Comuna", "Ciudad",
            "Zona Resi.", "Años Resi.", "Educación", "Ocupación", "Previsión Salud");
        
        for (DatosGenerales dg : datosGenerales) {

            List<Paciente> pac = pacienteRepository.findAll();
            List<Formulario> form = formularioRepository.findAll();

            agregarCeldaTabla(table, safe(dg.getIdDatosGen().getFormularioId()));
            
            String pacienteId = obtenerPacienteIdPorFormulario(dg.getIdDatosGen().getFormularioId());
            agregarCeldaTabla(table, pacienteId);

            //Datos Sociodemográficos (formulario)
            agregarCeldaTabla(table, safe(dg.getEdad()));
            agregarCeldaTabla(table, safe(dg.getSexo()));

            agregarCeldaTabla(table, safeDouble(dg.getPeso())); //Var. Antropométricas (formulario)
            agregarCeldaTabla(table, safeDouble(dg.getImc()));  //Var. Antropométricas (formulario)
            agregarCeldaTabla(table, safeDouble(dg.getEstatura())); //Var. Antropométricas (formulario)


            agregarCeldaTabla(table, safe(dg.getNacionalidad()));
            agregarCeldaTabla(table, safe(dg.getDireccion()));
            agregarCeldaTabla(table, safe(dg.getComuna()));
            agregarCeldaTabla(table, safe(dg.getCiudad()));

            agregarCeldaTabla(table, safe(dg.getZonaResidencial()));
            agregarCeldaTabla(table, safe(dg.getAniosResiActual()));

            agregarCeldaTabla(table, safe(dg.getEducacion()));
            agregarCeldaTabla(table, safe(dg.getOcupacion()));
            agregarCeldaTabla(table, safe(dg.getPrevisionSalud()));
        }
        return table;
    }

    private PdfPTable createPdfTableForDatosClinicos() {
        List<DatosClinicos> list = datosClinicosRepository.findAll();
        
        PdfPTable table = new PdfPTable(9); 
        table.setWidthPercentage(100);
        try { table.setWidths(new float[] {0.5f, 0.5f, 0.6f, 0.6f, 1f, 1f, 1f, 1f, 1f}); } catch (DocumentException e) { /* ignore */ }
        
        agregarEncabezadoTabla(table,
            "FormID", "PacID", "AdenoGást", "FechaAdeno", "AntFamCánGást", "AntFamOtroCán", "OtrasEnf", "Medicamentos", "Cirugía Previa");

        for (DatosClinicos dc : list) {
            agregarCeldaTabla(table, safe(dc.getIdDatosCli().getFormularioId()));

            String pacienteId = obtenerPacienteIdPorFormulario(dc.getIdDatosCli().getFormularioId());
            agregarCeldaTabla(table, pacienteId);

            agregarCeldaTabla(table, safe(dc.getAdenoGastrico()));
            agregarCeldaTabla(table, safe(dc.getFechaAdenoGastrico()));
            agregarCeldaTabla(table, safe(dc.getAntFamCancerGast()));
            agregarCeldaTabla(table, safe(dc.getAntFamOtroCancer()));
            agregarCeldaTabla(table, safe(dc.getOtrasEnfermedades()));
            agregarCeldaTabla(table, safe(dc.getMedicamentos()));
            agregarCeldaTabla(table, safe(dc.getCirugiaGastricaPrevia()));
        }
        return table;
    }

    private PdfPTable createPdfTableForDatosClinicosHPylori(){
        List<DatosClinicos> list2 = datosClinicosRepository.findAll();

        PdfPTable table = new PdfPTable(15);
        table.setWidthPercentage(100);

        try{
            table.setWidths(new float [] {0.5f, 0.5f, 0.6f, 0.6f, 0.8f, 0.6f, 0.8f, 0.8f, 0.8f, 0.6f, 0.7f, 0.8f, 0.8f, 0,8, 0,7});
        } catch (DocumentException excep){
            /* ignore */
        }

        agregarEncabezadoTabla(table, "FormID", "PacID", "ResulExam", "TipoExam", "TiempoExam", "ResulPas", "AñoResul", "TipoExamPas", 
            "TrataErra", "AñoTrata", "Esquema", "Antib/IBP", "RepeExamPos", "FechaExamPos", "ResulExamPos");

        for (DatosClinicos dc : list2) {
            agregarCeldaTabla(table, safe(dc.getIdDatosCli().getFormularioId()));

            String pacienteId = obtenerPacienteIdPorFormulario(dc.getIdDatosCli().getFormularioId());
            agregarCeldaTabla(table, pacienteId);

            agregarCeldaTabla(table, safe(dc.getHpyloriResultado()));
            agregarCeldaTabla(table, safe(dc.getHpyloriPrueba()));
            agregarCeldaTabla(table, safe(dc.getHpyloriTiempoTest()));

            agregarCeldaTabla(table, safe(dc.getPositivoPasadoHPylori()));
            agregarCeldaTabla(table, safe(dc.getAnioPositivoPasado()));
            agregarCeldaTabla(table, safe(dc.getTipoExamenPasadoHPy()));

            agregarCeldaTabla(table, safe(dc.getTrataErradicacion()));
            agregarCeldaTabla(table, safe(dc.getAnioTrataEradica()));
            agregarCeldaTabla(table, safe(dc.getEsquemaTratamientoErra()));

            agregarCeldaTabla(table, safe(dc.getAntibioticosIBP()));

            agregarCeldaTabla(table, safe(dc.getRepeticionExamen()));
            agregarCeldaTabla(table, safe(dc.getResultadosExamen()));
            agregarCeldaTabla(table, safe(dc.getFechaRepetiExamen()));
        }
        return table;
    }

    private PdfPTable createPdfTableForHabitosPaciente() {
        List<HabitosPaciente> list = habitosPacienteRepository.findAll();
        
        PdfPTable table = new PdfPTable(10); 
        table.setWidthPercentage(100);
        try { table.setWidths(new float[] {0.5f, 0.5f, 0.8f, 1f, 0.8f, 0.9f, 0.8f, 0.7f, 0.5f, 0.7f}); } catch (DocumentException e) { /* ignore */ }
        
        agregarEncabezadoTabla(table,
             "FormID", "PacID", "ConsumoTabaco", "CantPromDiario", "ExFumador", "ConsumoAlcohol", "Frecuencia", "CantProm", "AñosCons", "ExAlcohol");

        for (HabitosPaciente h : list) {
            agregarCeldaTabla(table, safe(h.getIdHabPaciente().getFormularioId()));
            String pacienteId = obtenerPacienteIdPorFormulario(h.getIdHabPaciente().getFormularioId());
            agregarCeldaTabla(table, pacienteId);
            agregarCeldaTabla(table, safe(h.getEstadoConsumoTabaco()));
            agregarCeldaTabla(table, safe(h.getCantPromTabaco()));
            agregarCeldaTabla(table, safe(h.getExConsumidorTabaco()));
            agregarCeldaTabla(table, safe(h.getEstadoConsumoAlcohol()));
            agregarCeldaTabla(table, safe(h.getFrecuenciaAlcohol()));
            agregarCeldaTabla(table, safe(h.getCantidadAlcohol()));
            agregarCeldaTabla(table, safe(h.getAniosConsumoAlcohol()));
            agregarCeldaTabla(table, safe(h.getExConsumidorAlcohol()));
        }
        return table;
    }

    private PdfPTable createPdfTableForFactDietarioAmbiental() {
        List<FactDietariosAmbientales> list = factDietariosAmbientalesRepository.findAll();
        
        PdfPTable table = new PdfPTable(13); 
        table.setWidthPercentage(100);
        try { table.setWidths(new float[] {0.5f, 0.5f, 0.6f, 0.6f, 0.8f, 0.6f, 0.8f, 0.8f, 0.8f, 0.6f, 0.7f, 0.8f, 0.8f}); } catch (DocumentException e) { /* ignore */ }
        
        agregarEncabezadoTabla(table,
            "Form ID", "PacID", "CarnesCecinas", "AlimSalados", "FrutaVerdura", "Frituras", "AlimCondimentado", "BebidasInfusiones", 
        "Pesticidas", "Quimicos", "HumoLeña", "FuenteAgua", "TratamAgua");

        for (FactDietariosAmbientales f : list) {
            agregarCeldaTabla(table, safe(f.getIdFact().getFormularioId()));

            String pacienteId = obtenerPacienteIdPorFormulario(f.getIdFact().getFormularioId());
            agregarCeldaTabla(table, pacienteId);

            agregarCeldaTabla(table, safe(f.getDietaCarnesCecinas()));
            agregarCeldaTabla(table, safe(f.getDietaAgregaSal()));
            agregarCeldaTabla(table, safe(f.getDietaFrutasVerduras()));
            agregarCeldaTabla(table, safe(f.getDietaFrituras()));
            agregarCeldaTabla(table, safe(f.getAliCondimentado()));
            agregarCeldaTabla(table, safe(f.getInfusionesBebidas()));
            agregarCeldaTabla(table, safe(f.getExposicionPesticidas()));
            agregarCeldaTabla(table, safe(f.getExposicionQuimicos()));
            agregarCeldaTabla(table, safe(f.getCombusLenaDiario()));
            agregarCeldaTabla(table, safe(f.getAguaConsumoZona()));
            agregarCeldaTabla(table, safe(f.getTratamientoAgua()));
        }
        return table;
    }

    private PdfPTable createPdfTableForHistopatologia() {
        List<Histopatologia> list = histopatologiaRepository.findAll();
        PdfPTable table = new PdfPTable(5); // 4 columnas
        table.setWidthPercentage(100);

        agregarEncabezadoTabla(table, "Formulario ID", "Paciente ID", "Tipo", "Estado Clínico", "Localización Tumoral");

        for (Histopatologia h : list) {
            agregarCeldaTabla(table, safe(h.getHistoID().getFormularioId()));
            String pacienteId = obtenerPacienteIdPorFormulario(h.getHistoID().getFormularioId());
            agregarCeldaTabla(table, pacienteId);
            agregarCeldaTabla(table, safe(h.getTipo()));
            agregarCeldaTabla(table, safe(h.getEstadoClinico()));
            agregarCeldaTabla(table, safe(h.getLocaliTumoral()));
        }
        return table;
    }

    // ============= MÉTODOS DE LLAMADA =============

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

    // =========== MÉTODOS FORMULARIO POR PACIENTE =============
    private void agregarDatosGenerales(Document document, DatosGenerales dg) throws DocumentException {
        Paragraph p = new Paragraph("Datos Generales:", BOLD_FONT);
        document.add(p);

        document.add(new Paragraph(String.format("Edad: %d años | Sexo: %s | IMC: %s",
            dg.getEdad() != null ? dg.getEdad() : 0, 
            safe(dg.getSexo()),
            dg.getImc() != null ? String.format("%.1f", dg.getImc()) : "N/A"), NORMAL_FONT));


        document.add(new Paragraph(String.format("Peso: %s kg | Estatura: %s cm",
            dg.getPeso() != null ? String.format("%.1f", dg.getPeso()) : "N/A",
            dg.getEstatura() != null ? String.format("%.1f", dg.getEstatura()) : "N/A"), NORMAL_FONT));

        document.add(new Paragraph(String.format("Nacionalidad: %s", safe(dg.getNacionalidad())), NORMAL_FONT));

        document.add(new Paragraph(String.format("Dirección: %s | Comuna: %s | Ciudad: %s",
            safe(dg.getDireccion() != null ? String.valueOf(dg.getNacionalidad()) : "N/A"), 
            safe(dg.getComuna() != null ? String.valueOf(dg.getComuna()) : "N/A"), 
            safe(dg.getCiudad() != null ? String.valueOf(dg.getCiudad()) : "N/A")), 
            NORMAL_FONT));

        document.add(new Paragraph(String.format("Zona Residencial: %s | Años Residencia: %s",
            safe(dg.getZonaResidencial() != null ? String.valueOf(dg.getZonaResidencial()) : "N/A"), 
            safe(dg.getAniosResiActual() != null ? String.valueOf(dg.getAniosResiActual()) : "N/A")), 
            NORMAL_FONT));


        document.add(new Paragraph(String.format("Educación: %s | Ocupación: %s",
            safe(dg.getEducacion() != null ? String.valueOf(dg.getEducacion()) : "N/A"), 
            safe(dg.getOcupacion() != null ? String.valueOf(dg.getOcupacion()) : "N/A")), 
            NORMAL_FONT));

        document.add(new Paragraph(String.format("Previsión de Salud: %s", 
            safe(dg.getPrevisionSalud() != null ? String.valueOf(dg.getPrevisionSalud()) : "N/A")), 
            NORMAL_FONT));
        
        document.add(Chunk.NEWLINE);
    }

    private void agregarDatosClinicos(Document document, DatosClinicos dc) throws DocumentException {
        Paragraph p = new Paragraph("Datos Clínicos:", BOLD_FONT);
        document.add(p);

        document.add(new Paragraph(String.format("Adenocarcinoma Gástrico: %s | Fecha Adeno Gástrico: %s",
            safe(dc.getAdenoGastrico()), dc.getFechaAdenoGastrico() != null ? dc.getFechaAdenoGastrico().toString() : "N/A"), NORMAL_FONT));
        
        document.add(new Paragraph(String.format("Antecedentes Fam. Cáncer Gástrico: %s", safe(dc.getAntFamCancerGast())), NORMAL_FONT));
        document.add(new Paragraph(String.format("Antecedentes Fam. Cáncer: %s", safe(dc.getAntFamOtroCancer())), NORMAL_FONT));
        document.add(new Paragraph(String.format("Otras Enfermedades: %s", safe(dc.getOtrasEnfermedades())), NORMAL_FONT));
        document.add(new Paragraph(String.format("Medicamentos: %s", safe(dc.getMedicamentos())), NORMAL_FONT));
        document.add(new Paragraph(String.format("Cirugía Gástrica Previa: %s", safe(dc.getCirugiaGastricaPrevia())), NORMAL_FONT));

        document.add(new Paragraph(String.format("Resultado HPylori Actual: %s | Tipo de Prueba: %s | Tiempo de Test: %s",
            safe(dc.getHpyloriResultado() != null ? dc.getHpyloriResultado().toString() : "N/A"), 
            dc.getHpyloriPrueba() != null ? dc.getHpyloriPrueba().toString() : "N/A", 
            dc.getHpyloriTiempoTest() != null ? String.valueOf(dc.getHpyloriTiempoTest()): "N/A"), 
            NORMAL_FONT));

        document.add(new Paragraph(String.format("Resultado HPylori Pasado: %s | Esquema: %s | Año aproximado: %s",
            safe(dc.getPositivoPasadoHPylori() != null ?  String.valueOf(dc.getPositivoPasadoHPylori()): "N/A"), 
            dc.getTipoExamenPasadoHPy() != null ? dc.getTipoExamenPasadoHPy().toString() : "N/A", 
            dc.getAnioPositivoPasado() != null ? String.valueOf(dc.getAnioPositivoPasado()): "N/A"), 
            NORMAL_FONT));

        document.add(new Paragraph(String.format("Tratamiento Erradicación: %s | Esquema Erradicación: %s | Año aproximado: %s",
            safe(dc.getTrataErradicacion() != null ?  String.valueOf(dc.getTrataErradicacion()): "N/A"), 
            dc.getEsquemaTratamientoErra() != null ? dc.getEsquemaTratamientoErra().toString() : "N/A", 
            dc.getAnioTrataEradica() != null ? String.valueOf(dc.getAnioTrataEradica()): "N/A"), 
            NORMAL_FONT));
        
        document.add(new Paragraph(String.format("Uso de antibióticos o inhibidores IBP: %s", safe(dc.getAntibioticosIBP() 
            != null ?  String.valueOf(dc.getAntibioticosIBP()): "N/A")), NORMAL_FONT));

        document.add(new Paragraph(String.format("Repetición Examen HPylori: %s | Fecha: %s | Resultado: %s",
            safe(dc.getRepeticionExamen() != null ?  String.valueOf(dc.getRepeticionExamen()): "N/A"), 
            dc.getFechaRepetiExamen() != null ? String.valueOf(dc.getFechaRepetiExamen()): "N/A", 
            dc.getResultadosExamen() != null ? String.valueOf(dc.getResultadosExamen()): "N/A"), 
            NORMAL_FONT));       

        document.add(Chunk.NEWLINE);
    }

    private void agregarHabitos(Document document, HabitosPaciente hp) throws DocumentException {
        Paragraph p = new Paragraph("Hábitos del Paciente:", BOLD_FONT);
        document.add(p);

        document.add(new Paragraph(String.format("Consumo Tabaco: %s | Cantidad Promedio (Unidades): %s | Ex Fumador: %s",
            safe(hp.getEstadoConsumoTabaco() != null ? String.valueOf(hp.getEstadoConsumoTabaco()) : "N/A"), 
            hp.getCantPromTabaco() != null ? String.valueOf(hp.getCantPromTabaco()) : "N/A",  
            hp.getExConsumidorTabaco() != null ? String.valueOf(hp.getExConsumidorTabaco()) : "N/A"), 
            NORMAL_FONT));

        document.add(new Paragraph(String.format("Consumo Alcohol: %s | Frecuencia: %s | Cantidad Promedio: %s",
            safe(hp.getEstadoConsumoAlcohol() != null ? String.valueOf(hp.getEstadoConsumoAlcohol()) : "N/A"), 
            safe(hp.getFrecuenciaAlcohol() != null ? String.valueOf(hp.getFrecuenciaAlcohol()) : "N/A"), 
            safe(hp.getCantidadAlcohol() != null ? String.valueOf(hp.getCantidadAlcohol()) : "N/A")), 
            NORMAL_FONT));

        document.add(new Paragraph(String.format("Años de Consumo: %s | Ex Alcohol: %s",
            safe(hp.getAniosConsumoAlcohol() != null ? String.valueOf(hp.getAniosConsumoAlcohol()) : "N/A"), 
            safe(hp.getExConsumidorAlcohol() != null ? String.valueOf(hp.getExConsumidorAlcohol()) : "N/A")), 
            NORMAL_FONT));

        document.add(Chunk.NEWLINE);
    }

    private void agregarFactoresDietariosAmbientales(Document document, FactDietariosAmbientales fda) throws DocumentException {
        Paragraph p = new Paragraph("Factores Dietario-Ambiental del Paciente:", BOLD_FONT);
        document.add(p);

        document.add(new Paragraph(String.format("Consumo Carnes Procesadas: %s | Consumo Alimentos Salados: %s",
            safe(fda.getDietaCarnesCecinas() != null ? String.valueOf(fda.getDietaCarnesCecinas()) : "N/A"), 
            safe(fda.getDietaAgregaSal() != null ? String.valueOf(fda.getDietaAgregaSal()) : "N/A")), 
            NORMAL_FONT));

        document.add(new Paragraph(String.format("Consumo Frituras: %s | Consumo Alimentos Condimentados: %s",
            safe(fda.getDietaFrituras() != null ? String.valueOf(fda.getDietaFrituras()) : "N/A"), 
            safe(fda.getAliCondimentado() != null ? String.valueOf(fda.getAliCondimentado()) : "N/A")), 
            NORMAL_FONT));

        document.add(new Paragraph(String.format("Consumo Frutas-Verduras: %s | Consumo Infusiones-Bebidas: %s",
            safe(fda.getDietaFrutasVerduras() != null ? String.valueOf(fda.getDietaFrutasVerduras()) : "N/A"), 
            safe(fda.getInfusionesBebidas() != null ? String.valueOf(fda.getInfusionesBebidas()) : "N/A")),
            NORMAL_FONT));

        document.add(new Paragraph(String.format("Fumigaciones: %s | ExposicionPesticidas: %s | Humo Leña: %s",
            safe(fda.getFumigaciones() != null ? String.valueOf(fda.getFumigaciones()) : "N/A"), 
            safe(fda.getExposicionPesticidas() != null ? String.valueOf(fda.getExposicionPesticidas()) : "N/A"), 
            safe(fda.getCombusLenaDiario() != null ? String.valueOf(fda.getExposicionQuimicos()) : "N/A")), 
            NORMAL_FONT));

        document.add(new Paragraph(String.format("Fuente de Agua: %s | Tratamiento de Agua: %s",
            safe(fda.getAguaConsumoZona() != null ? String.valueOf(fda.getAguaConsumoZona()) : "N/A"), 
            safe(fda.getTratamientoAgua() != null ? String.valueOf(fda.getTratamientoAgua()) : "N/A")), 
            NORMAL_FONT));    

        document.add(Chunk.NEWLINE);
    }

    private void agregarHistopatologia(Document document, Histopatologia hp) throws DocumentException {
        Paragraph p = new Paragraph("Histopatología:", BOLD_FONT);
        document.add(p);

        document.add(new Paragraph(String.format("Tipo: %s | Estado Clínico: %s | Localización Tumoral: %s",
            safe(hp.getTipo() != null ? String.valueOf(hp.getTipo()) : "N/A"), 
            safe(hp.getEstadoClinico() != null ? String.valueOf(hp.getEstadoClinico()) : "N/A"), 
            safe(hp.getLocaliTumoral() != null ? String.valueOf(hp.getLocaliTumoral()) : "N/A")), 
            NORMAL_FONT));
        document.add(Chunk.NEWLINE);
    }

    private void agregarSeccionPaciente(Document document, Paciente paciente) throws DocumentException {
        Paragraph subtitle = new Paragraph("Información del Paciente", SUBTITLE_FONT);
        document.add(subtitle);
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("ID: " + safe(paciente.getIdPaciente() != null ? String.valueOf(paciente.getIdPaciente()) : "N/A"), NORMAL_FONT));
        document.add(new Paragraph("Nombre: " + safe(paciente.getNombrePaciente() != null ? String.valueOf(paciente.getNombrePaciente()) : "N/A"), NORMAL_FONT));
        document.add(new Paragraph("Correo: " + safe(paciente.getCorreoPaciente() != null ? String.valueOf(paciente.getCorreoPaciente()) : "N/A"), NORMAL_FONT));
        document.add(new Paragraph("Dirección: " + safe(paciente.getDireccionPaciente() != null ? String.valueOf(paciente.getDireccionPaciente()) : "N/A"), NORMAL_FONT));
        document.add(new Paragraph("Tipo de Paciente: " + safe(paciente.getTipoPaciente() != null ? String.valueOf(paciente.getTipoPaciente()) : "N/A"), NORMAL_FONT));
        document.add(new Paragraph("Fecha de Inclusión: " +safe(paciente.getFechaInclusion() != null ? String.valueOf(paciente.getFechaInclusion()) : "N/A"), NORMAL_FONT));
    }

    private void agregarSeccionPacienteReclutador(Document document, Paciente paciente) throws DocumentException {
        Paragraph subtitle = new Paragraph("Información del Paciente", SUBTITLE_FONT);
        document.add(subtitle);
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("ID: " + safe(paciente.getIdPaciente() != null ? String.valueOf(paciente.getIdPaciente()) : "N/A"), NORMAL_FONT));
        document.add(new Paragraph("Tipo de Paciente: " + safe(paciente.getTipoPaciente() != null ? String.valueOf(paciente.getTipoPaciente()) : "N/A"), NORMAL_FONT));
        document.add(new Paragraph("Fecha de Inclusión: " +safe(paciente.getFechaInclusion() != null ? String.valueOf(paciente.getFechaInclusion()) : "N/A"), NORMAL_FONT));
    }


    // ============= MÉTODOS EXTRA O AUXILIARES ============
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

    private String obtenerPacienteIdPorFormulario(Integer formularioId) {
        try {
            Formulario formulario = formularioRepository.findById(formularioId).orElse(null);
            if (formulario != null && formulario.getPaciente() != null) {
                return safe(formulario.getPaciente().getIdPaciente());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "N/A";
    }
}