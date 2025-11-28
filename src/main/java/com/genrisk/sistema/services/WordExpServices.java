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




    // ============= MÉTODOS AUXILIARES =============

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

    private void agregarSeccionPacienteReclutador(XWPFDocument document, Paciente paciente) { //Nuevo Método
        agregarSubtitulo(document, "Información del Paciente");
        agregarSaltoLinea(document);

        agregarTexto(document, "ID: " + paciente.getIdPaciente());
        agregarTexto(document, "Tipo: " + paciente.getTipoPaciente());
    }

    private void agregarDatosGenerales(XWPFDocument document, DatosGenerales dg) {
        agregarTextoNegrita(document, "Datos Generales:");
        
        agregarTexto(document, String.format("Edad: %d años | Sexo: %s | IMC: %.1f", 
            dg.getEdad(), dg.getSexo(), dg.getImc()));
        agregarTexto(document, String.format("Peso: %.1f kg | Estatura: %.1f cm", 
            dg.getPeso(), dg.getEstatura()));
        agregarTexto(document, String.format("Educación: %s | Ocupación: %s", 
            dg.getEducacion(), dg.getOcupacion()));
        agregarSaltoLinea(document);
    }

    private void agregarDatosClinicos(XWPFDocument document, DatosClinicos dc) {
        agregarTextoNegrita(document, "Datos Clínicos:");
        
        agregarTexto(document, String.format("Adenocarcinoma Gástrico: %s | Fecha Adeno Gástrico: %s", 
            dc.getAdenoGastrico() != null ? dc.getAdenoGastrico() : "N/A", dc.getFechaAdenoGastrico() != null ? dc.getFechaAdenoGastrico() : "N/A"));
         agregarTexto(document, String.format("Cirugía Gástrica Previa: %s", 
            dc.getCirugiaGastricaPrevia()));
        agregarTexto(document, String.format("Antecedentes Fam. Cáncer Gástrico: %s", 
            dc.getAntFamCancerGast()));
        agregarTexto(document, String.format("H. Pylori - Prueba: %s | Resultado: %s | Tiempo(meses): %s", 
            dc.getHpyloriPrueba(), dc.getHpyloriResultado(), dc.getHpyloriTiempoTest()));
        agregarTexto(document, String.format("Medicamentos: %s", 
            dc.getMedicamentos()));
        agregarTexto(document, String.format("Antecedentes Fam. Cáncer: %s", 
            dc.getAntFamOtroCancer()));
        agregarTexto(document, String.format("Otras Enfermedades: %s", 
            dc.getOtrasEnfermedades()));
        agregarSaltoLinea(document);
    }

    private void agregarHistopatologia(XWPFDocument document, Histopatologia hp) {
        agregarTextoNegrita(document, "Histopatología:");
        agregarSaltoLinea(document);

        agregarTexto(document, String.format("Tipo: %s | Estado Clínico: %s | Localización Tumoral: %s", 
            safe(hp.getTipo()), safe(hp.getEstadoClinico()), safe(hp.getLocaliTumoral())));
        agregarSaltoLinea(document);
    }

    private void agregarHabitos(XWPFDocument document, HabitosPaciente hp) {
        agregarTextoNegrita(document, "Hábitos del Paciente:");
        agregarSaltoLinea(document);


        agregarSubtitulo(document,"Tabaco" ); 

        agregarTexto(document, String.format("Estado de Consumo: %s", 
            hp.getEstadoConsumoTabaco()));
        agregarTexto(document, String.format("Cantidad Promedio (unidades): %s | Tiempo en Consumo (meses): %s", 
            hp.getCantPromTabaco(), hp.getTiempoTabaco())); 
        agregarTexto(document, String.format("Tiempo sin consumir (años): %s", 
            hp.getExConsumidorTabaco())); 

        agregarSaltoLinea(document);
        agregarSubtitulo(document,"Alcohol" ); 
        agregarSaltoLinea(document);


        agregarTexto(document, String.format("Consumo Alcohol: %s | Frecuencia: %s", 
            hp.getEstadoConsumoAlcohol(), hp.getFrecuenciaAlcohol()));
        agregarTexto(document, String.format("Cantidad: %s | Frecuencia (meses): %s | Tiempo Sin Consumo (meses): %s", 
            hp.getCantidadAlcohol(), hp.getAniosConsumoAlcohol(), hp.getExConsumidorAlcohol()));

        agregarSaltoLinea(document);
    }

    private void agregarFactoresDietariosAmbientales(XWPFDocument document, FactDietariosAmbientales fda){
        agregarTextoNegrita(document, "Factores Dietario-Ambiental del Paciente:");
        agregarSaltoLinea(document);

        agregarTexto(document, String.format("Consumo Agua: %s | Tratamiento Agua: %s", 
            fda.getAguaConsumoZona(), fda.getTratamientoAgua()));

        agregarTexto(document, String.format("Fumigaciones: %s | ExposicionPesticidas: %s | CombustiónLeña: %s", 
            fda.getFumigaciones(), fda.getExposicionPesticidas(), fda.getCombusLenaDiario()));

        agregarTexto(document, String.format("ExposiciónQuímicos: %s | AgregaSal: %s | ConsumoFrutaVerdura: %s", 
            fda.getExposicionQuimicos(), fda.getDietaAgregaSal(), fda.getDietaFrutasVerduras()));

        agregarTexto(document, String.format("ConsumoFrituras: %s | ConsumoCarnesCecinas: %s", 
            fda.getDietaFrituras(), fda.getDietaCarnesCecinas()));
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

    private String safe(Object obj) {
        return obj == null ? "N/A" : String.valueOf(obj);
    }
    
    private String safeDouble(Double d) {
        return d == null ? "N/A" : String.format("%.1f", d);
    }
}