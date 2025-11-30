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

    @Autowired 
    private FactDietariosAmbientalesRepository factDietariosAmbientalesRepository; 

    @Autowired 
    private HistopatologiaRepository histopatologiaRepository;

    /**
     * Exporta un reporte completo de paciente a Word
     */
    public byte[] exportarPacienteAWord(String idPaciente) throws Exception {
        XWPFDocument document = new XWPFDocument();
        
        try {
            Paciente paciente = pacienteRepository.findById(idPaciente)
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

            agregarTitulo(document, "Reporte Detallado de Paciente");
            agregarSaltoLinea(document);

            agregarSeccionPaciente(document, paciente);
            agregarSaltoLinea(document);

            List<Formulario> formularios = formularioRepository.findByPacienteIdPaciente(idPaciente);

            // Se itera sobre todos los formularios del paciente
            for (Formulario formulario : formularios) {
                agregarSeccionFormulario(document, formulario);
                agregarSaltoLinea(document);
            }

            agregarPiePagina(document);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.write(baos);
            return baos.toByteArray();

        } finally {
            document.close();
        }
    }

    public byte[] exportarPacienteAWordReclutador(String idPaciente) throws Exception { //Nuevo Método
        XWPFDocument document = new XWPFDocument();
        
        try {
            Paciente paciente = pacienteRepository.findById(idPaciente)
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

            agregarTitulo(document, "Reporte Detallado de Paciente");
            agregarSaltoLinea(document);

            agregarSeccionPacienteReclutador(document, paciente);
            agregarSaltoLinea(document);

            List<Formulario> formularios = formularioRepository.findByPacienteIdPaciente(idPaciente);

            // Se itera sobre todos los formularios del paciente
            for (Formulario formulario : formularios) {
                agregarSeccionFormulario(document, formulario);
                agregarSaltoLinea(document);
            }

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
            agregarTitulo(document, "Listado General de Pacientes");
            List<Paciente> pacientes = pacienteRepository.findAll();
            
            // Crear tabla
            XWPFTable table = document.createTable(pacientes.size() + 1, 5);
            table.setWidth("100%"); // **OPTIMIZACIÓN:** Asegura que la tabla no se desborde

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
                XWPFTableRow row = table.getRow(rowIndex++);
                configurarCeldaDatos(row.getCell(0), paciente.getIdPaciente());
                configurarCeldaDatos(row.getCell(1), paciente.getNombrePaciente());
                configurarCeldaDatos(row.getCell(2), paciente.getCorreoPaciente());
                configurarCeldaDatos(row.getCell(3), paciente.getDireccionPaciente());
                configurarCeldaDatos(row.getCell(4), paciente.getTipoPaciente());
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

    public byte[] exportarListaPacientesAWordReclutadores() throws Exception {
        XWPFDocument document = new XWPFDocument();
        
        try {
            agregarTitulo(document, "Listado General de Pacientes");
            List<Paciente> pacientes = pacienteRepository.findAll();
            
            // Crear tabla
            XWPFTable table = document.createTable(pacientes.size() + 1, 5);
            table.setWidth("100%"); // **OPTIMIZACIÓN:** Asegura que la tabla no se desborde

            // Encabezados
            XWPFTableRow headerRow = table.getRow(0);
            configurarCeldaEncabezado(headerRow.getCell(0), "ID");
            configurarCeldaEncabezado(headerRow.getCell(1), "Tipo");

            // Datos
            int rowIndex = 1;
            for (Paciente paciente : pacientes) {
                XWPFTableRow row = table.getRow(rowIndex++);
                configurarCeldaDatos(row.getCell(0), paciente.getIdPaciente());
                configurarCeldaDatos(row.getCell(1), paciente.getTipoPaciente());
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
            List<DatosGenerales> datosGenerales = datosGeneralesRepository.findAll();
            
            XWPFTable table = document.createTable(datosGenerales.size() + 1, 10);
            table.setWidth("100%");

            String[] headers = new String[] {
                "Form ID", "Edad", "Sexo", "Peso", "IMC", "Estatura", 
                "Zona", "Años Resi.", "Educación", "Ocupación"
            };
            
            XWPFTableRow headerRow = table.getRow(0);
            for (int i = 0; i < headers.length; i++) {
                configurarCeldaEncabezado(headerRow.getCell(i), headers[i]);
            }

            int rowIndex = 1;
            for (DatosGenerales dg : datosGenerales) {
                XWPFTableRow row = table.getRow(rowIndex++);
                configurarCeldaDatos(row.getCell(0), String.valueOf(dg.getIdDatosGen().getFormularioId()));
                configurarCeldaDatos(row.getCell(1), safe(dg.getEdad()));
                configurarCeldaDatos(row.getCell(2), safe(dg.getSexo()));
                configurarCeldaDatos(row.getCell(3), safeDouble(dg.getPeso()));
                configurarCeldaDatos(row.getCell(4), safeDouble(dg.getImc()));
                configurarCeldaDatos(row.getCell(5), safeDouble(dg.getEstatura()));
                configurarCeldaDatos(row.getCell(6), safe(dg.getZonaResidencial()));
                configurarCeldaDatos(row.getCell(7), safe(dg.getEducacion()));
                configurarCeldaDatos(row.getCell(8), safe(dg.getOcupacion()));
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

    public byte[] exportarDatosClinicosAWord() throws Exception {
        XWPFDocument document = new XWPFDocument();
        
        try {
            agregarTitulo(document, "Datos Clínicos de Pacientes");
            List<DatosClinicos> datosClinicos = datosClinicosRepository.findAll();
            
            // Reducir columnas de 11 a 9 para evitar desborde y consolidar datos
            XWPFTable table = document.createTable(datosClinicos.size() + 1, 9);
            table.setWidth("100%");

            String[] headers = new String[] {
                "Form ID", "AdenoGástrico", "Fecha Adeno", "Ant. Fam. Cáncer Gástrico", 
                "H. Pylori (Prueba/Resultado)", "Tiempo Test (meses)", 
                "Medicamentos", "Otras Enf.", "Ant. Fam. Otro Cáncer"
            };

            XWPFTableRow headerRow = table.getRow(0);
            for (int i = 0; i < headers.length; i++) {
                configurarCeldaEncabezado(headerRow.getCell(i), headers[i]);
            }

            int rowIndex = 1;
            for (DatosClinicos dc : datosClinicos) {
                XWPFTableRow row = table.getRow(rowIndex++);
                configurarCeldaDatos(row.getCell(0), String.valueOf(dc.getIdDatosCli().getFormularioId()));
                configurarCeldaDatos(row.getCell(1), safe(dc.getAdenoGastrico()));
                configurarCeldaDatos(row.getCell(2), safe(dc.getFechaAdenoGastrico()));
                configurarCeldaDatos(row.getCell(3), safe(dc.getAntFamCancerGast()));
                // Consolidación de H. Pylori para ahorrar espacio
                configurarCeldaDatos(row.getCell(4), safe(dc.getHpyloriPrueba()) + " / " + safe(dc.getHpyloriResultado()));
                configurarCeldaDatos(row.getCell(5), safe(dc.getHpyloriTiempoTest()));
                configurarCeldaDatos(row.getCell(6), safe(dc.getMedicamentos()));
                configurarCeldaDatos(row.getCell(7), safe(dc.getOtrasEnfermedades()));
                configurarCeldaDatos(row.getCell(8), safe(dc.getAntFamOtroCancer()));
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

    public byte[] exportarHabitosPacienteAWord() throws Exception {
        XWPFDocument document = new XWPFDocument();
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            agregarTitulo(document, "Hábitos de Pacientes");

            List<HabitosPaciente> list = habitosPacienteRepository.findAll();

            String[] headers = new String[] {
                "Form ID", "Estado Tabaco", "Cant. Prom.", 
                "Tiempo Consumo (meses)", "Consumo Alcohol", "Frecuencia Alcohol", 
                "Años Consumo"
            };

            XWPFTable table = document.createTable(list.size() + 1, headers.length);
            table.setWidth("100%"); 

            // Encabezados
            XWPFTableRow headerRow = table.getRow(0);
            for (int i = 0; i < headers.length; i++) {
                configurarCeldaEncabezado(headerRow.getCell(i), headers[i]);
            }

            // Datos
            if (!list.isEmpty()) {
                int rowIndex = 1;
                for (HabitosPaciente h : list) {
                    XWPFTableRow row = table.getRow(rowIndex++);
                    configurarCeldaDatos(row.getCell(0), String.valueOf(h.getIdHabPaciente().getFormularioId()));
                    configurarCeldaDatos(row.getCell(1), safe(h.getEstadoConsumoTabaco()));
                    configurarCeldaDatos(row.getCell(2), safe(h.getCantPromTabaco()));
                    configurarCeldaDatos(row.getCell(3), safe(h.getTiempoTabaco()));
                    configurarCeldaDatos(row.getCell(4), safe(h.getEstadoConsumoAlcohol()));
                    configurarCeldaDatos(row.getCell(5), safe(h.getFrecuenciaAlcohol()));
                    configurarCeldaDatos(row.getCell(6), safe(h.getAniosConsumoAlcohol()));
                }
            } else {
                configurarCeldaDatos(table.getRow(1).getCell(0), "Sin registros");
            }

            agregarSaltoLinea(document);
            agregarPiePagina(document);

            document.write(baos);
            return baos.toByteArray();
        } finally {
            document.close();
        }
    }

    public byte[] exportarFactDietarioAmbientalAWord() throws Exception {
        XWPFDocument document = new XWPFDocument();
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            agregarTitulo(document, "Factores Dietario / Ambientales");

            List<FactDietariosAmbientales> list = factDietariosAmbientalesRepository.findAll();

            // Reducir headers para caber en una página
            String[] headers = new String[] {
                "Form ID", "Agua Consumo", "Tratamiento Agua", 
                "Fumigaciones", "Exposición Químicos", "Dieta Sal", "Dieta Frituras"
            };

            XWPFTable table = document.createTable(list.size() + 1, headers.length);
            table.setWidth("100%");

            XWPFTableRow headerRow = table.getRow(0);
            for (int i = 0; i < headers.length; i++) {
                configurarCeldaEncabezado(headerRow.getCell(i), headers[i]);
            }

            if (!list.isEmpty()) {
                int r = 1;
                for (FactDietariosAmbientales f : list) {
                    XWPFTableRow row = table.getRow(r++);
                    configurarCeldaDatos(row.getCell(0), safe(f.getIdFact().getFormularioId()));
                    configurarCeldaDatos(row.getCell(1), safe(f.getAguaConsumoZona()));
                    configurarCeldaDatos(row.getCell(2), safe(f.getTratamientoAgua()));
                    configurarCeldaDatos(row.getCell(3), safe(f.getFumigaciones()));
                    configurarCeldaDatos(row.getCell(4), safe(f.getExposicionQuimicos()));
                    configurarCeldaDatos(row.getCell(5), safe(f.getDietaAgregaSal()));
                    configurarCeldaDatos(row.getCell(6), safe(f.getDietaFrituras()));
                }
            } else {
                configurarCeldaDatos(table.getRow(1).getCell(0), "Sin registros");
            }

            agregarSaltoLinea(document);
            agregarPiePagina(document);

            document.write(baos);
            return baos.toByteArray();
        } finally {
            document.close();
        }
    }

    public byte[] exportarHistopatologiaAWord() throws Exception {
        XWPFDocument document = new XWPFDocument();
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            agregarTitulo(document, "Histopatología");

            List<Histopatologia> list = histopatologiaRepository.findAll();

            String[] headers = new String[] { "Form ID", "Tipo", "Estado Clínico", "Localización Tumoral" };

            XWPFTable table = document.createTable(list.size() + 1, headers.length);
            table.setWidth("100%"); 

            XWPFTableRow headerRow = table.getRow(0);
            for (int i = 0; i < headers.length; i++) {
                configurarCeldaEncabezado(headerRow.getCell(i), headers[i]);
            }

            if (!list.isEmpty()) {
              int r = 1;
                for (Histopatologia h : list) {
                    XWPFTableRow row = table.getRow(r++);
                    configurarCeldaDatos(row.getCell(0), safe(h.getHistoID().getFormularioId()));
                    configurarCeldaDatos(row.getCell(1), safe(h.getTipo()));
                    configurarCeldaDatos(row.getCell(2), safe(h.getEstadoClinico()));
                    configurarCeldaDatos(row.getCell(3), safe(h.getLocaliTumoral()));
                }
            } else {
                configurarCeldaDatos(table.getRow(1).getCell(0), "Sin registros");
            }

            agregarSaltoLinea(document);
            agregarPiePagina(document);

            document.write(baos);
            return baos.toByteArray();
        } finally {
            document.close();
        }
    }

    // ================== LLAMADAS PARA IMPORTAR DATOS ================0

    private void agregarSeccionFormulario(XWPFDocument document, Formulario formulario) {
        agregarSubtitulo(document, "Formulario ID: " + formulario.getIdFormulario() + " (" + safe(formulario.getTipoFormulario()) + ")");

        // Nota: Las llamadas a los repositorios usan .ifPresent(dto::setter) que ya es seguro
        datosGeneralesRepository.findByIdDatosGen_FormularioId(formulario.getIdFormulario())
            .ifPresent(dg -> agregarDatosGenerales(document, dg));

        datosClinicosRepository.findByIdDatosCli_FormularioId(formulario.getIdFormulario())
            .ifPresent(dc -> agregarDatosClinicos(document, dc));

        habitosPacienteRepository.findByIdHabPaciente_FormularioId(formulario.getIdFormulario())
            .ifPresent(hp -> agregarHabitos(document, hp));

        factDietariosAmbientalesRepository.findByIdFact_FormularioId(formulario.getIdFormulario()) 
            .ifPresent(fda -> agregarFactoresDietariosAmbientales(document, fda));   
            
        histopatologiaRepository.findByHistoID_FormularioId(formulario.getIdFormulario())
             .ifPresent(h -> agregarHistopatologia(document, h));
    }
    // ===================== MÉTODOS PARA CONFIGURAR ESTÉTICA =====================
    private void agregarTitulo(XWPFDocument document, String titulo) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        
        XWPFRun run = paragraph.createRun();
        run.setText(titulo);
        run.setBold(true);
        run.setFontSize(18);
        run.setColor("033664");
    }

    private void agregarSubtitulo(XWPFDocument document, String subtitulo) {
        XWPFParagraph paragraph = document.createParagraph();
        
        XWPFRun run = paragraph.createRun();
        run.setText(subtitulo);
        run.setBold(true);
        run.setFontSize(15);
        run.setColor("077C9C");
    }

    private void agregarTexto(XWPFDocument document, String texto) {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText(texto);
        run.setFontSize(11);
    }

    private void agregarTextoNegrita2(XWPFDocument document, String texto) {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText(texto);
        run.setBold(true);
        run.setFontSize(14);
        run.setColor("701E05");
    }

    private void agregarTextoNegrita3(XWPFDocument document, String texto) {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText(texto);
        run.setBold(true);
        run.setFontSize(12);
        run.setColor("6370F8");
    }

    private void agregarSaltoLinea(XWPFDocument document) {
        document.createParagraph();
    }

    // ============== MÉTODOS PARA EXPORTAR PACIENTES ============
    private void agregarSeccionPaciente(XWPFDocument document, Paciente paciente) {
        agregarSubtitulo(document, "Información del Paciente");
        
        agregarTextoConEtiqueta(document, "ID: ", paciente.getIdPaciente());
        agregarTextoConEtiqueta(document, "Nombre: ", paciente.getNombrePaciente());
        agregarTextoConEtiqueta(document, "Correo: ", paciente.getCorreoPaciente());
        agregarTextoConEtiqueta(document, "Dirección: ", paciente.getDireccionPaciente());
        agregarTextoConEtiqueta(document, "Tipo: ", paciente.getTipoPaciente());
        agregarTextoConEtiqueta(document, "Fecha de Inclusión: ", String.valueOf(paciente.getFechaInclusion()));
    }

    private void agregarSeccionPacienteReclutador(XWPFDocument document, Paciente paciente) { //Nuevo Método
        agregarSubtitulo(document, "Información del Paciente");
        
        agregarTextoConEtiqueta(document, "ID: ", paciente.getIdPaciente());
        agregarTextoConEtiqueta(document, "Tipo: ", paciente.getTipoPaciente());
        agregarTextoConEtiqueta(document, "Fecha de Inclusión: ", String.valueOf(paciente.getFechaInclusion()));
    }

    // =========== MÉTODOS DE FORMULARIO PARA WORD POR CADA PACIENTE =================
    private void agregarDatosGenerales(XWPFDocument document, DatosGenerales dg) {
        agregarTextoNegrita2(document, "Datos Generales:");
        
        agregarTextosMultiples(document,
        "Edad: ", String.format("%d años", dg.getEdad() != null ? dg.getEdad() : 0),
        "Sexo: ", safe(dg.getSexo()),
        "IMC: ", dg.getImc() != null ? String.format("%.1f", dg.getImc()) : "N/A");

        agregarTextosMultiples(document,
            "Peso: ", dg.getPeso() != null ? String.format("%.1f kg", dg.getPeso()) : "N/A",
            "Estatura: ", dg.getEstatura() != null ? String.format("%.1f cm", dg.getEstatura()) : "N/A");

        agregarTextoConEtiqueta(document, "Nacionalidad: ", safe(dg.getNacionalidad()));

        agregarTextosMultiples(document,"Dirección: ", safe(dg.getDireccion()), 
                "Comuna: ", safe(dg.getComuna()),"Ciudad: ", safe(dg.getCiudad()));
        
        agregarTextosMultiples(document,
            "Zona Residencial: ", safe(dg.getZonaResidencial()),
            "Años Residencia: ", safe(dg.getAniosResiActual())
        );

        agregarTextosMultiples(document,
            "Educación: ", safe(dg.getEducacion()),
            "Ocupación: ", safe(dg.getOcupacion())
        );

        agregarTextoConEtiqueta(document, "Previsión de Salud: ", safe(dg.getPrevisionSalud()));
        
        agregarSaltoLinea(document);
    }

    private void agregarDatosClinicos(XWPFDocument document, DatosClinicos dc) {
        agregarTextoNegrita2(document, "Datos Clínicos:");
        
        agregarTextosMultiples(document,
            "Adenocarcinoma Gástrico: ", safe(dc.getAdenoGastrico()),
            "Fecha Adeno Gástrico: ", dc.getFechaAdenoGastrico() != null ? dc.getFechaAdenoGastrico().toString() : "N/A"
        );
        agregarTextoConEtiqueta(document, "Antecedentes Fam. Cáncer Gástrico: ", dc.getAntFamCancerGast());
        agregarTextoConEtiqueta(document, "Antecedentes Fam. Cáncer: ", dc.getAntFamOtroCancer());
        agregarTextoConEtiqueta(document, "Otras Enfermedades: ", dc.getOtrasEnfermedades());
        agregarTextosMultiples(document, "Medicamentos: ", safe(dc.getMedicamentos()), 
        "Ciruguia Gástrica Previa: ", safe(dc.getCirugiaGastricaPrevia()));

        agregarSaltoLinea(document);

        agregarTextosMultiples(document,
            "Resultado HPylori Actual: ", safe(dc.getHpyloriResultado()),
            "Tipo de Prueba: ", dc.getHpyloriPrueba() != null ? dc.getHpyloriPrueba().toString() : "N/A",
            "Tiempo de Test: ", dc.getHpyloriTiempoTest() != null ? String.valueOf(dc.getHpyloriTiempoTest()) : "N/A"
        );

        agregarTextosMultiples(document,
    "Resultado HPylori Pasado: ", safe(dc.getPositivoPasadoHPylori()),
            "Tipo de Prueba: ", dc.getTipoExamenPasadoHPy() != null ? dc.getTipoExamenPasadoHPy().toString() : "N/A",
            "Tiempo de Test (años): ", dc.getAnioPositivoPasado() != null ? String.valueOf(dc.getAnioPositivoPasado()) : "N/A");

        agregarTextosMultiples(document,
    "Tratamiento Erradicación: ", safe(dc.getTrataErradicacion()),
            "Esquema Erradicación: ", dc.getEsquemaTratamientoErra() != null ? dc.getEsquemaTratamientoErra().toString() : "N/A",
            "Año Aproximado: ", dc.getAnioTrataEradica() != null ? String.valueOf(dc.getAnioTrataEradica()) : "N/A");
        
        agregarTextosMultiples(document,"Uso de antibióticos o inhibidores IBP: ", safe(dc.getAntibioticosIBP() 
            != null ?  String.valueOf(dc.getAntibioticosIBP()): "N/A"));

        agregarTextosMultiples(document, "Repetición Examen HPylori: ",
            safe(dc.getRepeticionExamen() != null ?  String.valueOf(dc.getRepeticionExamen()): "N/A"), 
            "Fecha: ", dc.getFechaRepetiExamen() != null ? String.valueOf(dc.getFechaRepetiExamen()): "N/A", 
            "Resultado: ", dc.getResultadosExamen() != null ? String.valueOf(dc.getResultadosExamen()): "N/A"); 

        agregarSaltoLinea(document);
    }

    private void agregarHistopatologia(XWPFDocument document, Histopatologia hp) {
        agregarTextoNegrita2(document, "Histopatología:");

        agregarTextosMultiples(document, 
            "Tipo", safe(hp.getTipo() != null ? String.valueOf(hp.getTipo()) : "N/A"), 
            "Estado Clínico: ", safe(hp.getEstadoClinico() != null ? String.valueOf(hp.getEstadoClinico()) : "N/A"), 
            "Localización Tumoral: ", safe(hp.getLocaliTumoral() != null ? String.valueOf(hp.getLocaliTumoral()) : "N/A"));
        agregarSaltoLinea(document);
    }

    private void agregarHabitos(XWPFDocument document, HabitosPaciente hp) {
        agregarTextoNegrita2(document, "Hábitos del Paciente:");

        agregarTextoNegrita3(document,"Tabaco" ); 

        agregarTextoConEtiqueta(document, "Estado de Consumo: ", 
            hp.getEstadoConsumoTabaco());
        agregarTextoConEtiqueta(document, "Cantidad Promedio (Unidades): ", 
            String.valueOf(hp.getCantPromTabaco())); 
        agregarTextoConEtiqueta(document, "Ex Fumador: ", 
            String.valueOf(hp.getExConsumidorTabaco())); 

        agregarSaltoLinea(document);
        agregarTextoNegrita3(document,"Alcohol" ); 
        
        agregarTextosMultiples(document, 
            "Consumo Alcohol: ", safe(hp.getEstadoConsumoAlcohol() != null ? String.valueOf(hp.getEstadoConsumoAlcohol()) : "N/A"), 
            "Frecuencia: ", safe(hp.getFrecuenciaAlcohol() != null ? String.valueOf(hp.getFrecuenciaAlcohol()) : "N/A"), 
            "Cantidad Promedio: ", safe(hp.getCantidadAlcohol() != null ? String.valueOf(hp.getCantidadAlcohol()) : "N/A"));

        agregarTextosMultiples(document, 
            "Años de Consumo: ", safe(hp.getAniosConsumoAlcohol() != null ? String.valueOf(hp.getAniosConsumoAlcohol()) : "N/A"), 
            "Ex Alcohol: ", safe(hp.getExConsumidorAlcohol() != null ? String.valueOf(hp.getExConsumidorAlcohol()) : "N/A"));

        agregarSaltoLinea(document);
    }

    private void agregarFactoresDietariosAmbientales(XWPFDocument document, FactDietariosAmbientales fda){
        agregarTextoNegrita2(document, "Factores Dietario-Ambiental del Paciente:");

        agregarTextosMultiples(document,
            "Consumo Carnes Procesadas: ", safe(fda.getDietaCarnesCecinas() != null ? String.valueOf(fda.getDietaCarnesCecinas()) : "N/A"), 
            "Consumo Alimentos Salados: ", safe(fda.getDietaAgregaSal() != null ? String.valueOf(fda.getDietaAgregaSal()) : "N/A"));
        
        agregarTextosMultiples(document,
            "Consumo Frituras: ", safe(fda.getDietaFrituras() != null ? String.valueOf(fda.getDietaFrituras()) : "N/A"), 
            "Consumo Alimentos Condimentados: ", safe(fda.getAliCondimentado() != null ? String.valueOf(fda.getAliCondimentado()) : "N/A")); 

        agregarTextosMultiples(document,
            "Consumo Frutas-Verduras: ", safe(fda.getDietaFrutasVerduras() != null ? String.valueOf(fda.getDietaFrutasVerduras()) : "N/A"), 
            "Consumo Infusiones-Bebidas: ", safe(fda.getInfusionesBebidas() != null ? String.valueOf(fda.getInfusionesBebidas()) : "N/A"));

        agregarTextosMultiples(document,
            "Fumigaciones: ", safe(fda.getFumigaciones() != null ? String.valueOf(fda.getFumigaciones()) : "N/A"), 
            "ExposicionPesticidas: ", safe(fda.getExposicionPesticidas() != null ? String.valueOf(fda.getExposicionPesticidas()) : "N/A"), 
            "Humo Leña: ", safe(fda.getCombusLenaDiario() != null ? String.valueOf(fda.getExposicionQuimicos()) : "N/A"));

        agregarTextosMultiples(document,
            "Fuente de Agua: ", safe(fda.getAguaConsumoZona() != null ? String.valueOf(fda.getAguaConsumoZona()) : "N/A"), 
            "Tratamiento de Agua: ", safe(fda.getTratamientoAgua() != null ? String.valueOf(fda.getTratamientoAgua()) : "N/A"));    

        agregarSaltoLinea(document);
    }

    // =========== MÉTODOS GENERALES DE WORD =============
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

    private void agregarTextoConEtiqueta(XWPFDocument document, String etiqueta, String valor) {
    XWPFParagraph paragraph = document.createParagraph();
    
    XWPFRun runEtiqueta = paragraph.createRun();
    runEtiqueta.setText(etiqueta);
    runEtiqueta.setBold(true);
    runEtiqueta.setFontSize(12.5);
    
    XWPFRun runValor = paragraph.createRun();
    runValor.setText(valor != null ? valor : "N/A");
    runValor.setFontSize(11);
    }

    private void agregarTextosMultiples(XWPFDocument document, String... etiquetasYValores) {
    XWPFParagraph paragraph = document.createParagraph();
    
    // Validar que hay un número par de argumentos (etiqueta-valor)
    if (etiquetasYValores.length % 2 != 0) {
        throw new IllegalArgumentException("Debe haber un número par de argumentos (etiqueta-valor)");
    }
    
    for (int i = 0; i < etiquetasYValores.length; i += 2) {
        String etiqueta = etiquetasYValores[i];
        String valor = etiquetasYValores[i + 1];
        
        // Agregar separador "|" si no es el primer campo
        if (i > 0) {
            XWPFRun separador = paragraph.createRun();
            separador.setText(" | ");
            separador.setFontSize(11);
        }
        
        // Etiqueta en negrita
        XWPFRun runEtiqueta = paragraph.createRun();
        runEtiqueta.setText(etiqueta);
        runEtiqueta.setBold(true);
        runEtiqueta.setFontSize(12.5);
        
        // Valor en texto normal
        XWPFRun runValor = paragraph.createRun();
        runValor.setText(valor != null ? valor : "N/A");
        runValor.setFontSize(11);
    }
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