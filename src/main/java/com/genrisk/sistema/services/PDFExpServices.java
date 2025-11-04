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

    @Autowired 
    private FactDietariosAmbientalesRepository factDietariosAmbientalesRepository;

    @Autowired 
    private HistopatologiaRepository histopatologiaRepository;

    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
    private static final Font SUBTITLE_FONT = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLUE);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    private static final Font BOLD_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);

    /**
     * Exporta un reporte completo de paciente a PDF (incluye todas las tablas relacionadas al/los formulario(s) del paciente)
     */
    public byte[] exportarPacienteAPDF(String idPaciente) throws Exception {
        Document document = new Document(PageSize.A4.rotate());
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

    private void agregarTitulo(com.itextpdf.text.Document document, String titulo) throws DocumentException {
        Paragraph titleParagraph = new Paragraph(titulo, TITLE_FONT);
        titleParagraph.setAlignment(Element.ALIGN_CENTER);
        titleParagraph.setSpacingAfter(10f);
        document.add(titleParagraph);
    }

    /**
     * Exporta lista de pacientes a PDF (ya existía, la conservamos como exportarPacientesAPDF)
     */
    public byte[] exportarPacientesAPDF() throws Exception {
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
    
    public byte[] exportarDatosGeneralesAPDF() throws Exception {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            agregarTitulo(document, "Datos Generales de Pacientes");
            document.add(Chunk.NEWLINE);

            PdfPTable table = createPdfTableForDatosGenerales();
            document.add(table);

            agregarPiePagina(document);

        } finally {
            document.close();
        }

        return baos.toByteArray();
    }

    /**
     * Exporta datos clinicos (nuevo método)
     */
    public byte[] exportarDatosClinicosAPDF() throws Exception {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            agregarTitulo(document, "Datos Clínicos de Pacientes");
            document.add(Chunk.NEWLINE);

            PdfPTable table = createPdfTableForDatosClinicos();
            document.add(table);

            agregarPiePagina(document);

        } finally {
            document.close();
        }

        return baos.toByteArray();
    }

    /**
     * Exporta habitos (nuevo método)
     */
    public byte[] exportarHabitosAPDF() throws Exception {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            agregarTitulo(document, "Hábitos de Pacientes");
            document.add(Chunk.NEWLINE);

            PdfPTable table = createPdfTableForHabitosPaciente();
            document.add(table);

            agregarPiePagina(document);

        } finally {
            document.close();
        }

        return baos.toByteArray();
    }

    /**
     * Exporta factores dietario/ambientales (nuevo método)
     */
    public byte[] exportarFactDietarioAmbientalAPDF() throws Exception {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            agregarTitulo(document, "Factores Dietario / Ambientales");
            document.add(Chunk.NEWLINE);

            PdfPTable table = createPdfTableForFactDietarioAmbiental();
            document.add(table);

            agregarPiePagina(document);

        } finally {
            document.close();
        }

        return baos.toByteArray();
    }

    /**
     * Exporta histopatología (nuevo método)
     */
    public byte[] exportarHistopatologiaAPDF() throws Exception {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            agregarTitulo(document, "Histopatología");
            document.add(Chunk.NEWLINE);

            PdfPTable table = createPdfTableForHistopatologia();
            document.add(table);

            agregarPiePagina(document);

        } finally {
            document.close();
        }

        return baos.toByteArray();
    }

    // ============= MÉTODOS AUXILIARES (PDF builders por tabla) =============

    private PdfPTable createPdfTableForPacientes() {
        List<Paciente> pacientes = pacienteRepository.findAll();

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        agregarEncabezadoTabla(table, "id_paciente", "nombre_paciente", "correo_paciente", "direccion_paciente", "tipo_paciente");

        for (Paciente p : pacientes) {
            agregarCeldaTabla(table, safe(p.getIdPaciente()));
            agregarCeldaTabla(table, safe(p.getNombrePaciente()));
            agregarCeldaTabla(table, safe(p.getCorreoPaciente()));
            agregarCeldaTabla(table, safe(p.getDireccionPaciente()));
            agregarCeldaTabla(table, safe(p.getTipoPaciente()));
        }
        return table;
    }

    private PdfPTable createPdfTableForFormularios() {
        List<Formulario> list = formularioRepository.findAll();
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        agregarEncabezadoTabla(table, "id_formulario", "estado_formulario", "tipo_formulario", "fecha_formulario", "paciente_id");
        for (Formulario f : list) {
            agregarCeldaTabla(table, f.getIdFormulario() != null ? String.valueOf(f.getIdFormulario()) : "N/A");
            agregarCeldaTabla(table, safe(f.getEstadoFormulario()));
            agregarCeldaTabla(table, safe(f.getTipoFormulario()));
            agregarCeldaTabla(table, f.getFechaFormulario() != null ? f.getFechaFormulario().toString() : "N/A");
            agregarCeldaTabla(table, safe(f.getPaciente_id()));
        }
        return table;
    }

    private PdfPTable createPdfTableForDatosGenerales() {
        List<DatosGenerales> datosGenerales = datosGeneralesRepository.findAll();
        PdfPTable table = new PdfPTable(10);
        table.setWidthPercentage(100);
        agregarEncabezadoTabla(table,
             "formulario_id", "edad", "sexo", "peso", "imc", "estatura",
            "zona_residencial", "anios_resi_actual", "educacion", "ocupacion");

        for (DatosGenerales dg : datosGenerales) {
            // itemformu
            agregarCeldaTabla(table, dg.getIdDatosGen().getFormularioId()!= null ? String.valueOf(dg.getIdDatosGen().getFormularioId()) : "N/A");
            agregarCeldaTabla(table, dg.getEdad() != null ? String.valueOf(dg.getEdad()) : "N/A");
            agregarCeldaTabla(table, safe(dg.getSexo()));
            agregarCeldaTabla(table, dg.getPeso() != null ? String.format("%.1f", dg.getPeso()) : "N/A");
            agregarCeldaTabla(table, dg.getImc() != null ? String.format("%.1f", dg.getImc()) : "N/A");
            agregarCeldaTabla(table, dg.getEstatura() != null ? String.format("%.1f", dg.getEstatura()) : "N/A");
            agregarCeldaTabla(table, safe(dg.getZonaResidencial()));
            agregarCeldaTabla(table, dg.getAniosResiActual() != null ? String.valueOf(dg.getAniosResiActual()) : "N/A");
            agregarCeldaTabla(table, safe(dg.getEducacion()));
            agregarCeldaTabla(table, safe(dg.getOcupacion()));
        }
        return table;
    }

    private PdfPTable createPdfTableForDatosClinicos() {
        List<DatosClinicos> list = datosClinicosRepository.findAll();
        PdfPTable table = new PdfPTable(12);
        table.setWidthPercentage(100);
        agregarEncabezadoTabla(table,
            "FormularioID", "AdenoGastrico", "FechaAdeno",
            "antFamCanGast", "medicamentos", "otras_enfermedades",
            "antFamCancer", "CiruGastrPrevia", "hpyloriPrueba",
            "Resultado", "TiempoTest");

        for (DatosClinicos dc : list) {
            agregarCeldaTabla(table, dc.getIdDatosCli().getFormularioId() != null ? String.valueOf(dc.getIdDatosCli().getFormularioId()) : "N/A");
            agregarCeldaTabla(table, safe(dc.getAdenoGastrico()));
            agregarCeldaTabla(table, dc.getFechaAdenoGastrico() != null ? dc.getFechaAdenoGastrico().toString() : "N/A");
            agregarCeldaTabla(table, safe(dc.getAntFamCancerGast()));
            agregarCeldaTabla(table, safe(dc.getMedicamentos()));
            agregarCeldaTabla(table, safe(dc.getOtrasEnfermedades()));
            agregarCeldaTabla(table, safe(dc.getAntFamOtroCancer()));
            agregarCeldaTabla(table, safe(dc.getCirugiaGastricaPrevia()));
            agregarCeldaTabla(table, safe(dc.getHpyloriPrueba()));
            agregarCeldaTabla(table, safe(dc.getHpyloriResultado()));
            agregarCeldaTabla(table, safe(dc.getHpyloriTiempoTest()));
        }
        return table;
    }

    private PdfPTable createPdfTableForHabitosPaciente() {
        List<HabitosPaciente> list = habitosPacienteRepository.findAll();
        PdfPTable table = new PdfPTable(14);
        table.setWidthPercentage(100);
        agregarEncabezadoTabla(table,
             "formularioID", "estado_consumo_tabaco", "edad_inicio_tabaco",
            "cant_prom_tabaco", "tiempo_tabaco", "ex_consumidor_tabaco", "estado_consumo_alcohol",
            "frecuencia_alcohol", "cantidad_alcohol", "anios_consumo_alcohol", "ex_consumidor_alcohol",
            "ejercicio", "frecuencia_ejercicio");

        for (HabitosPaciente h : list) {
            agregarCeldaTabla(table, h.getIdHabPaciente().getFormularioId() != null ? String.valueOf(h.getIdHabPaciente().getFormularioId()) : "N/A");
            agregarCeldaTabla(table, safe(h.getEstadoConsumoTabaco()));
            agregarCeldaTabla(table, h.getEdadInicioTabaco() != null ? String.valueOf(h.getEdadInicioTabaco()) : "N/A");
            agregarCeldaTabla(table, h.getCantPromTabaco() != null ? String.valueOf(h.getCantPromTabaco()) : "N/A");
            agregarCeldaTabla(table, h.getTiempoTabaco() != null ? String.valueOf(h.getTiempoTabaco()) : "N/A");
            agregarCeldaTabla(table, h.getExConsumidorTabaco() != null ? String.valueOf(h.getExConsumidorTabaco()) : "N/A");
            agregarCeldaTabla(table, safe(h.getEstadoConsumoAlcohol()));
            agregarCeldaTabla(table, safe(h.getFrecuenciaAlcohol()));
            agregarCeldaTabla(table, h.getCantidadAlcohol() != null ? String.valueOf(h.getCantidadAlcohol()) : "N/A");
            agregarCeldaTabla(table, h.getAniosConsumoAlcohol() != null ? String.valueOf(h.getAniosConsumoAlcohol()) : "N/A");
            agregarCeldaTabla(table, h.getExConsumidorAlcohol() != null ? String.valueOf(h.getExConsumidorAlcohol()) : "N/A");
            agregarCeldaTabla(table, safe(h.getEjercicio()));
            agregarCeldaTabla(table, safe(h.getFrecuenciaEjercicio()));
        }
        return table;
    }

    private PdfPTable createPdfTableForFactDietarioAmbiental() {
        List<FactDietariosAmbientales> list = factDietariosAmbientalesRepository.findAll();
        PdfPTable table = new PdfPTable(13);
        table.setWidthPercentage(100);
        agregarEncabezadoTabla(table,
            "formulario_id", "trabajo_zona_rural", "agua_consumo_zona",
            "tratamiento_agua", "fumigaciones", "exposicion_pesticidas",
            "combus_lena_diario", "exposicion_quimicos", "dieta_agregasal",
            "dieta_frutas_verduras", "dieta_frituras", "dieta_carnes_cecinas");

        for (FactDietariosAmbientales f : list) {
            agregarCeldaTabla(table, f.getIdFact().getFormularioId() != null ? String.valueOf(f.getIdFact().getFormularioId()) : "N/A");
            agregarCeldaTabla(table, safe(f.getTrabajoZonaRural()));
            // según dump el campo es agua_consumo_zona; en tu entidad puede ser getAguaConsumoRural -> probamos ambos en helper
            agregarCeldaTabla(table, safe(f.getAguaConsumoZona()));
            agregarCeldaTabla(table, safe(f.getTratamientoAgua()));
            agregarCeldaTabla(table, safe(f.getFumigaciones()));
            agregarCeldaTabla(table, safe(f.getExposicionPesticidas()));
            agregarCeldaTabla(table, safe(f.getCombusLenaDiario()));
            agregarCeldaTabla(table, safe(f.getExposicionQuimicos()));
            agregarCeldaTabla(table, safe(f.getDietaAgregaSal()));
            agregarCeldaTabla(table, safe(f.getDietaFrutasVerduras()));
            agregarCeldaTabla(table, safe(f.getDietaFrituras()));
            agregarCeldaTabla(table, safe(f.getDietaCarnesCecinas()));
        }
        return table;
    }

    private PdfPTable createPdfTableForHistopatologia() {
        List<Histopatologia> list = histopatologiaRepository.findAll();
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        agregarEncabezadoTabla(table, "formulario_id", "tipo", "estado_clinico", "locali_tumoral");

        for (Histopatologia h : list) {
            agregarCeldaTabla(table, h.getHistoID().getFormularioId() != null ? String.valueOf(h.getHistoID().getFormularioId()) : "N/A");
            agregarCeldaTabla(table, safe(h.getTipo()));
            agregarCeldaTabla(table, safe(h.getEstadoClinico()));
            agregarCeldaTabla(table, safe(h.getLocaliTumor()));
        }
        return table;
    }

    // ============= MÉTODOS AUXILIARES (se usan en secciones por formulario) =============

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

        // Histopatologia (si existe)
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
        document.add(new Paragraph(String.format("Zona: %s | Años residencia: %s",
            safe(dg.getZonaResidencial()),
            dg.getAniosResiActual() != null ? String.valueOf(dg.getAniosResiActual()) : "N/A"), NORMAL_FONT));
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

        document.add(new Paragraph(String.format("Trabajo Zona Rural: %s | Consumo Agua: %s | Tratamiento Agua: %s",
            safe(fda.getTrabajoZonaRural()), safe(fda.getTratamientoAgua() != null ? fda.getTratamientoAgua() : fda.getAguaConsumoZona()),
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
            safe(hp.getTipo()), safe(hp.getEstadoClinico()), safe(hp.getLocaliTumor())), NORMAL_FONT));
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
        document.add(new Paragraph("Tipo: " + safe(paciente.getTipoPaciente()), NORMAL_FONT));
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

    // ---------- helpers ----------
    private String safe(String s) {
        return s == null ? "N/A" : s;
    }
}