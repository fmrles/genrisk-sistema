package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.*;
import com.genrisk.sistema.repository.*;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Lazy
public class WordExpServices {

    @Autowired private PacienteRepository pacienteRepository;
    @Autowired private FormularioRepository formularioRepository;
    @Autowired private DatosGeneralesRepository datosGeneralesRepository;
    @Autowired private DatosClinicosRepository datosClinicosRepository;
    @Autowired private HabitosPacienteRepository habitosPacienteRepository;
    @Autowired private FactDietariosAmbientalesRepository factDietariosAmbientalesRepository;
    @Autowired private HistopatologiaRepository histopatologiaRepository;

    // =================================================================================
    // 1. REPORTES INDIVIDUALES (FICHA PACIENTE)
    // =================================================================================

    public byte[] exportarPacienteAWordReclutador(String idPaciente) throws Exception {
        return generarFichaOffline(idPaciente, "FICHA DE DATOS DEL PACIENTE (MODO OFFLINE)");
    }

    public byte[] exportarPacienteAWord(String idPaciente) throws Exception {
        return generarFichaOffline(idPaciente, "REPORTE DETALLADO DEL PACIENTE");
    }

    private byte[] generarFichaOffline(String idPaciente, String titulo) throws Exception {
        XWPFDocument document = new XWPFDocument();
        try {
            Paciente paciente = pacienteRepository.findById(idPaciente)
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

            agregarTitulo(document, titulo);
            agregarInstrucciones(document);

            // --- TABLA 1: IDENTIFICACIÓN (6 Filas) ---
            Map<String, String> datosPaciente = new LinkedHashMap<>();
            datosPaciente.put("ID Paciente (NO EDITAR)", paciente.getIdPaciente());
            datosPaciente.put("Nombre Completo", paciente.getNombrePaciente());
            datosPaciente.put("Correo", paciente.getCorreoPaciente());
            datosPaciente.put("Dirección", paciente.getDireccionPaciente());
            datosPaciente.put("Tipo (Caso/Control)", paciente.getTipoPaciente());
            datosPaciente.put("Fecha Inclusión (YYYY-MM-DD)", safe(paciente.getFechaInclusion()));
            crearTablaSeccion(document, "1. IDENTIFICACIÓN DEL PACIENTE", datosPaciente);

            Formulario formulario = formularioRepository.findTopByPacienteOrderByIdFormularioDesc(paciente)
                    .stream().findFirst().orElse(null);

            if (formulario != null) {
                Integer formId = formulario.getIdFormulario();

                // --- TABLA 2: DATOS GENERALES (13 Filas) ---
                DatosGenerales dg = datosGeneralesRepository.findByIdDatosGen_FormularioId(formId).orElse(new DatosGenerales());
                Map<String, String> mapaGen = new LinkedHashMap<>();
                mapaGen.put("Edad", safe(dg.getEdad()));
                mapaGen.put("Sexo (Hombre/Mujer)", safe(dg.getSexo()));
                mapaGen.put("Peso (kg)", safeDouble(dg.getPeso()));
                mapaGen.put("Estatura (cm)", safeDouble(dg.getEstatura()));
                mapaGen.put("Zona (Urbana/Rural)", safe(dg.getZonaResidencial()));
                mapaGen.put("Años Residencia (<5, 5-10, >10)", safe(dg.getAniosResiActual()));
                mapaGen.put("Educación", safe(dg.getEducacion()));
                mapaGen.put("Ocupación", safe(dg.getOcupacion()));
                mapaGen.put("Previsión Salud", safe(dg.getPrevisionSalud()));
                mapaGen.put("Nacionalidad", safe(dg.getNacionalidad()));
                mapaGen.put("Dirección (Residencia)", safe(dg.getDireccion()));
                mapaGen.put("Comuna", safe(dg.getComuna()));
                mapaGen.put("Ciudad", safe(dg.getCiudad()));
                crearTablaSeccion(document, "2. DATOS SOCIODEMOGRÁFICOS", mapaGen);

                // --- TABLA 3: DATOS CLÍNICOS (18 Filas) ---
                DatosClinicos dc = datosClinicosRepository.findByIdDatosCli_FormularioId(formId).orElse(new DatosClinicos());
                Map<String, String> mapaCli = new LinkedHashMap<>();
                mapaCli.put("Adenocarcinoma (Sí/No)", safe(dc.getAdenoGastrico()));
                mapaCli.put("Fecha Diagnóstico (YYYY-MM-DD)", safe(dc.getFechaAdenoGastrico()));
                mapaCli.put("Ant. Fam. Cáncer Gástrico", safe(dc.getAntFamCancerGast()));
                mapaCli.put("Otros Cánceres Familiares", safe(dc.getAntFamOtroCancer()));
                mapaCli.put("Medicamentos", safe(dc.getMedicamentos()));
                mapaCli.put("Otras Enfermedades", safe(dc.getOtrasEnfermedades()));
                mapaCli.put("Cirugía Gástrica Previa", safe(dc.getCirugiaGastricaPrevia())); 
                
                // HPylori Actual
                mapaCli.put("HPylori Resultado Actual", safe(dc.getHpyloriResultado()));
                mapaCli.put("Tipo Prueba Actual", safe(dc.getHpyloriPrueba()));
                mapaCli.put("Tiempo Test Actual (Meses)", safe(dc.getHpyloriTiempoTest())); 
                
                // HPylori Pasado
                mapaCli.put("HPylori Pasado (Sí/No)", safe(dc.getPositivoPasadoHPylori()));
                mapaCli.put("Tipo Examen Pasado", safe(dc.getTipoExamenPasadoHPy())); 
                mapaCli.put("Año Positivo Pasado", safe(dc.getAnioPositivoPasado())); 
                
                // Tratamiento
                mapaCli.put("Tratamiento Erradicación", safe(dc.getTrataErradicacion()));
                mapaCli.put("Esquema Tratamiento", safe(dc.getEsquemaTratamientoErra())); 
                mapaCli.put("Año Tratamiento", safe(dc.getAnioTrataEradica())); 
                mapaCli.put("Uso Antibióticos/IBP", safe(dc.getAntibioticosIBP()));
                crearTablaSeccion(document, "3. ANTECEDENTES CLÍNICOS", mapaCli);

                // --- TABLA 4: HÁBITOS (9 Filas) ---
                HabitosPaciente hp = habitosPacienteRepository.findByIdHabPaciente_FormularioId(formId).orElse(new HabitosPaciente());
                Map<String, String> mapaHab = new LinkedHashMap<>();
                mapaHab.put("Tabaco Estado", safe(hp.getEstadoConsumoTabaco()));
                mapaHab.put("Cigarrillos Promedio Día", safe(hp.getCantPromTabaco()));
                mapaHab.put("Tiempo Fumando", safe(hp.getTiempoTabaco()));
                mapaHab.put("Años Ex-Fumador", safe(hp.getExConsumidorTabaco()));
                mapaHab.put("Alcohol Estado", safe(hp.getEstadoConsumoAlcohol()));
                mapaHab.put("Frecuencia Alcohol", safe(hp.getFrecuenciaAlcohol()));
                mapaHab.put("Cantidad Tragos/Ocasión", safe(hp.getCantidadAlcohol()));
                mapaHab.put("Años Consumo Alcohol", safe(hp.getAniosConsumoAlcohol()));
                mapaHab.put("Años Ex-Bebedor", safe(hp.getExConsumidorAlcohol()));
                crearTablaSeccion(document, "4. HÁBITOS Y ESTILO DE VIDA", mapaHab);

                // --- TABLA 5: FACTORES DIETARIOS (11 Filas) ---
                FactDietariosAmbientales fda = factDietariosAmbientalesRepository.findByIdFact_FormularioId(formId).orElse(new FactDietariosAmbientales());
                Map<String, String> mapaDiet = new LinkedHashMap<>();
                mapaDiet.put("Carnes Procesadas", safe(fda.getDietaCarnesCecinas()));
                mapaDiet.put("Agrega Sal", safe(fda.getDietaAgregaSal()));
                mapaDiet.put("Frituras", safe(fda.getDietaFrituras()));
                mapaDiet.put("Alim. Condimentados", safe(fda.getAliCondimentado()));
                mapaDiet.put("Frutas y Verduras", safe(fda.getDietaFrutasVerduras()));
                mapaDiet.put("Bebidas Calientes", safe(fda.getInfusionesBebidas()));
                mapaDiet.put("Fuente de Agua", safe(fda.getAguaConsumoZona()));
                mapaDiet.put("Tratamiento Agua", safe(fda.getTratamientoAgua()));
                mapaDiet.put("Fumigaciones (Sí/No)", safe(fda.getFumigaciones())); 
                mapaDiet.put("Exposición Pesticidas", safe(fda.getExposicionPesticidas()));
                mapaDiet.put("Humo Leña", safe(fda.getCombusLenaDiario()));
                crearTablaSeccion(document, "5. FACTORES DIETARIOS Y AMBIENTALES", mapaDiet);

                // --- TABLA 6: HISTOPATOLOGÍA (3 Filas) ---
                Histopatologia histo = histopatologiaRepository.findByHistoID_FormularioId(formId).orElse(new Histopatologia());
                Map<String, String> mapaHisto = new LinkedHashMap<>();
                mapaHisto.put("Tipo Histológico", safe(histo.getTipo()));
                mapaHisto.put("Estadio Clínico", safe(histo.getEstadoClinico()));
                mapaHisto.put("Localización Tumoral", safe(histo.getLocaliTumoral()));
                crearTablaSeccion(document, "6. HISTOPATOLOGÍA", mapaHisto);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.write(baos);
            document.close();
            return baos.toByteArray();
        } finally {
            document.close();
        }
    }

    // =================================================================================
    // 2. REPORTES DE LISTADOS (TABLAS GENERALES) 
    // =================================================================================
    public byte[] exportarListaPacientesAWord() throws Exception {
        return generarReporteListado("Listado General de Pacientes", 
            new String[]{"ID", "Nombre", "Correo", "Tipo", "Fecha Ingreso"}, 
            pacienteRepository.findAll(), 
            (table, p) -> {
                XWPFTableRow row = table.createRow();
                row.getCell(0).setText(safe(p.getIdPaciente()));
                row.getCell(1).setText(safe(p.getNombrePaciente()));
                row.getCell(2).setText(safe(p.getCorreoPaciente()));
                row.getCell(3).setText(safe(p.getTipoPaciente()));
                row.getCell(4).setText(safe(p.getFechaInclusion()));
            });
    }

    public byte[] exportarListaPacientesAWordReclutadores() throws Exception {
        return generarReporteListado("Listado Pacientes", new String[]{"ID", "Tipo"}, pacienteRepository.findAll(), 
            (table, p) -> { XWPFTableRow row = table.createRow(); row.getCell(0).setText(safe(p.getIdPaciente())); row.getCell(1).setText(safe(p.getTipoPaciente())); });
    }

    public byte[] exportarDatosGeneralesAWord() throws Exception {
        return generarReporteListado("Reporte Datos Generales", new String[]{"Form ID", "Edad", "Sexo", "Peso", "IMC"}, datosGeneralesRepository.findAll(), 
            (table, dg) -> { XWPFTableRow row = table.createRow(); row.getCell(0).setText(safe(dg.getIdDatosGen().getFormularioId())); row.getCell(1).setText(safe(dg.getEdad())); row.getCell(2).setText(safe(dg.getSexo())); row.getCell(3).setText(safeDouble(dg.getPeso())); row.getCell(4).setText(safeDouble(dg.getImc())); });
    }

    public byte[] exportarDatosClinicosAWord() throws Exception {
        return generarReporteListado("Reporte Datos Clínicos", new String[]{"Form ID", "Adeno", "H.Pylori"}, datosClinicosRepository.findAll(), 
            (table, dc) -> { XWPFTableRow row = table.createRow(); row.getCell(0).setText(safe(dc.getIdDatosCli().getFormularioId())); row.getCell(1).setText(safe(dc.getAdenoGastrico())); row.getCell(2).setText(safe(dc.getHpyloriResultado())); });
    }

    public byte[] exportarHabitosPacienteAWord() throws Exception {
        return generarReporteListado("Reporte Hábitos", new String[]{"Form ID", "Tabaco", "Alcohol"}, habitosPacienteRepository.findAll(), 
            (table, hp) -> { XWPFTableRow row = table.createRow(); row.getCell(0).setText(safe(hp.getIdHabPaciente().getFormularioId())); row.getCell(1).setText(safe(hp.getEstadoConsumoTabaco())); row.getCell(2).setText(safe(hp.getEstadoConsumoAlcohol())); });
    }

    public byte[] exportarFactDietarioAmbientalAWord() throws Exception {
        return generarReporteListado("Reporte Dietario/Ambiental", new String[]{"Form ID", "Agua", "Frituras"}, factDietariosAmbientalesRepository.findAll(), 
            (table, fda) -> { XWPFTableRow row = table.createRow(); row.getCell(0).setText(safe(fda.getIdFact().getFormularioId())); row.getCell(1).setText(safe(fda.getAguaConsumoZona())); row.getCell(2).setText(safe(fda.getDietaFrituras())); });
    }

    public byte[] exportarHistopatologiaAWord() throws Exception {
        return generarReporteListado("Reporte Histopatología", new String[]{"Form ID", "Tipo", "Estadio"}, histopatologiaRepository.findAll(), 
            (table, h) -> { XWPFTableRow row = table.createRow(); row.getCell(0).setText(safe(h.getHistoID().getFormularioId())); row.getCell(1).setText(safe(h.getTipo())); row.getCell(2).setText(safe(h.getEstadoClinico())); });
    }

    // =================================================================================
    // 3. HELPERS
    // =================================================================================
    
    @FunctionalInterface private interface RowFiller<T> { void fill(XWPFTable table, T item); }

    private <T> byte[] generarReporteListado(String titulo, String[] headers, List<T> data, RowFiller<T> filler) throws Exception {
        XWPFDocument doc = new XWPFDocument();
        try {
            agregarTitulo(doc, titulo);
            XWPFTable table = doc.createTable(); table.setWidth("100%");
            XWPFTableRow headerRow = table.getRow(0);
            configurarCeldaEncabezado(headerRow.getCell(0), headers[0]);
            for (int i = 1; i < headers.length; i++) configurarCeldaEncabezado(headerRow.addNewTableCell(), headers[i]);
            for (T item : data) filler.fill(table, item);
            ByteArrayOutputStream baos = new ByteArrayOutputStream(); doc.write(baos); return baos.toByteArray();
        } finally { doc.close(); }
    }

    private void crearTablaSeccion(XWPFDocument doc, String tituloSeccion, Map<String, String> datos) {
        XWPFParagraph p = doc.createParagraph(); p.setSpacingBefore(300);
        XWPFRun r = p.createRun(); r.setText(tituloSeccion); r.setBold(true); r.setFontSize(14); r.setColor("2E74B5");
        XWPFTable table = doc.createTable(); table.setWidth("100%");
        XWPFTableRow header = table.getRow(0);
        configurarCelda(header.getCell(0), "CAMPO (NO MODIFICAR)", true, "E7E6E6");
        configurarCelda(header.addNewTableCell(), "VALOR (EDITAR AQUÍ)", true, "E7E6E6");
        for (Map.Entry<String, String> entry : datos.entrySet()) {
            XWPFTableRow row = table.createRow();
            configurarCelda(row.getCell(0), entry.getKey(), true, null);
            configurarCelda(row.getCell(1), entry.getValue(), false, null);
        }
    }

    private void configurarCelda(XWPFTableCell cell, String texto, boolean bold, String colorHex) {
        if (colorHex != null) cell.setColor(colorHex);
        XWPFParagraph p = cell.getParagraphs().isEmpty() ? cell.addParagraph() : cell.getParagraphs().get(0);
        for (int i = p.getRuns().size() - 1; i >= 0; i--) p.removeRun(i);
        XWPFRun r = p.createRun(); r.setText(texto); r.setBold(bold); r.setFontSize(10);
    }

    private void configurarCeldaEncabezado(XWPFTableCell cell, String texto) { configurarCelda(cell, texto, true, "2C3E50"); cell.getParagraphs().get(0).getRuns().get(0).setColor("FFFFFF"); }
    private void agregarTitulo(XWPFDocument doc, String t) { XWPFParagraph p = doc.createParagraph(); p.setAlignment(ParagraphAlignment.CENTER); XWPFRun r = p.createRun(); r.setText(t); r.setBold(true); r.setFontSize(18); r.setColor("033664"); }
    private void agregarInstrucciones(XWPFDocument doc) { XWPFParagraph p = doc.createParagraph(); XWPFRun r = p.createRun(); r.setText("INSTRUCCIONES: Edite ÚNICAMENTE la columna derecha. No borre filas."); r.setItalic(true); r.setFontSize(9); r.setColor("FF0000"); }
    private String safe(Object obj) { return obj == null ? "" : String.valueOf(obj); }
    private String safeDouble(Double d) { return d == null ? "" : String.format("%.1f", d); }
}