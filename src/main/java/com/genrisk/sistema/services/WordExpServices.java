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
            List<Formulario> formularios = formularioRepository.findByPacienteIdPaciente(idPaciente);

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

    public byte[] exportarDatosClinicosAWord() throws Exception {
        XWPFDocument document = new XWPFDocument();
        
        try {
            agregarTitulo(document, "Datos Clinicos de Pacientes");
            agregarSaltoLinea(document);

            List<DatosClinicos> datosClinicos = datosClinicosRepository.findAll();
            
            // Crear tabla
            XWPFTable table = document.createTable(datosClinicos.size() + 1, 11);
            table.setWidth("100%");

            // Encabezados
            XWPFTableRow headerRow = table.getRow(0);
            configurarCeldaEncabezado(headerRow.getCell(0), "Form ID");
            configurarCeldaEncabezado(headerRow.getCell(1), "AdenoGástrico");
            configurarCeldaEncabezado(headerRow.getCell(2), "FechaAdeGást");
            configurarCeldaEncabezado(headerRow.getCell(3), "AntFamCánGást");
            configurarCeldaEncabezado(headerRow.getCell(4), "Medicamentos");
            configurarCeldaEncabezado(headerRow.getCell(5), "Enfermedades");
            configurarCeldaEncabezado(headerRow.getCell(6), "AntFamCáncer");
            configurarCeldaEncabezado(headerRow.getCell(7), "CirugiaGásPrevia");
            configurarCeldaEncabezado(headerRow.getCell(8), "HPyloriPrueba");
            configurarCeldaEncabezado(headerRow.getCell(9), "Resultado");
            configurarCeldaEncabezado(headerRow.getCell(10), "TiempoTest");

            // Datos
            int rowIndex = 1;
            for (DatosClinicos dc : datosClinicos) {
                XWPFTableRow row = table.getRow(rowIndex);
                configurarCeldaDatos(row.getCell(0), String.valueOf(dc.getIdDatosCli().getFormularioId()));
                configurarCeldaDatos(row.getCell(1), String.valueOf(dc.getAdenoGastrico()));
                configurarCeldaDatos(row.getCell(2), String.valueOf(dc.getFechaAdenoGastrico()));
                configurarCeldaDatos(row.getCell(3), (dc.getAntFamOtroCancer()));
                configurarCeldaDatos(row.getCell(4), (dc.getMedicamentos()));
                configurarCeldaDatos(row.getCell(5), (dc.getOtrasEnfermedades()));
                configurarCeldaDatos(row.getCell(6), String.valueOf(dc.getAntFamOtroCancer()));
                configurarCeldaDatos(row.getCell(7), String.valueOf(dc.getCirugiaGastricaPrevia()));
                configurarCeldaDatos(row.getCell(8), dc.getHpyloriPrueba());
                configurarCeldaDatos(row.getCell(9), dc.getHpyloriResultado());
                configurarCeldaDatos(row.getCell(10), dc.getHpyloriTiempoTest());
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

    public byte[] exportarHabitosPacienteAWord() throws Exception {
        XWPFDocument document = new XWPFDocument();
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            agregarTitulo(document, "Hábitos de Pacientes");
            agregarSaltoLinea(document);

            List<HabitosPaciente> list = habitosPacienteRepository.findAll();

         // Columnas que vamos a mostrar
            String[] headers = new String[] {
                "Formulario ID", "Estado Consumo Tabaco", "Edad Inicio Tabaco",
                "Cant Prom Tabaco", "Tiempo Tabaco (meses)", "Ex Consumidor Tabaco",
                "Estado Consumo Alcohol", "Frecuencia Alcohol", "Cantidad Alcohol",
                "Años Consumo Alcohol", "Ex Consumidor Alcohol", "Ejercicio", "Frecuencia Ejercicio"
            };

            int rows = Math.max(1, (list != null ? list.size() : 0)) + 1; // +1 para encabezado
            XWPFTable table = document.createTable(rows, headers.length);
            table.setWidth("100%");

            // Encabezados
            XWPFTableRow headerRow = table.getRow(0);
            for (int i = 0; i < headers.length; i++) {
                configurarCeldaEncabezado(headerRow.getCell(i), headers[i]);
            }

        // Datos
        if (list != null && !list.isEmpty()) {
            int rowIndex = 1;
            for (HabitosPaciente h : list) {
                XWPFTableRow row = table.getRow(rowIndex++);
                configurarCeldaDatos(row.getCell(0), String.valueOf(h.getIdHabPaciente().getFormularioId()));
                configurarCeldaDatos(row.getCell(1), h.getEstadoConsumoTabaco());
                configurarCeldaDatos(row.getCell(2), h.getEdadInicioTabaco() != null ? String.valueOf(h.getEdadInicioTabaco()) : "N/A");
                configurarCeldaDatos(row.getCell(3), h.getCantPromTabaco() != null ? String.valueOf(h.getCantPromTabaco()) : "N/A");
                configurarCeldaDatos(row.getCell(4), h.getTiempoTabaco() != null ? String.valueOf(h.getTiempoTabaco()) : "N/A");
                configurarCeldaDatos(row.getCell(5), h.getExConsumidorTabaco() != null ? String.valueOf(h.getExConsumidorTabaco()) : "N/A");
                configurarCeldaDatos(row.getCell(6), h.getEstadoConsumoAlcohol());
                configurarCeldaDatos(row.getCell(7), h.getFrecuenciaAlcohol());
                configurarCeldaDatos(row.getCell(8), h.getCantidadAlcohol() != null ? String.valueOf(h.getCantidadAlcohol()) : "N/A");
                configurarCeldaDatos(row.getCell(9), h.getAniosConsumoAlcohol() != null ? String.valueOf(h.getAniosConsumoAlcohol()) : "N/A");
                configurarCeldaDatos(row.getCell(10), h.getExConsumidorAlcohol() != null ? String.valueOf(h.getExConsumidorAlcohol()) : "N/A");
                configurarCeldaDatos(row.getCell(11), h.getEjercicio());
                configurarCeldaDatos(row.getCell(12), h.getFrecuenciaEjercicio());
            }
        } else {
            // Si no hay registros, dejar mensaje en la primera celda de la fila de datos
            XWPFTableRow row = table.getRow(1);
            configurarCeldaDatos(row.getCell(0), "Sin registros");
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
        agregarSaltoLinea(document);

        List<FactDietariosAmbientales> list = factDietariosAmbientalesRepository.findAll();

        String[] headers = new String[] {
            "formulario_id", "trabajo_zona_rural", "agua_consumo_zona",
            "tratamiento_agua", "fumigaciones", "exposicion_pesticidas",
            "combus_lena_diario", "exposicion_quimicos", "dieta_agregasal",
            "dieta_frutas_verduras", "dieta_frituras", "dieta_carnes_cecinas"
        };

        int rows = Math.max(1, list != null ? list.size() : 0) + 1;
        XWPFTable table = document.createTable(rows, headers.length);
        table.setWidth("100%");

        XWPFTableRow headerRow = table.getRow(0);
        for (int i = 0; i < headers.length; i++) {
            configurarCeldaEncabezado(headerRow.getCell(i), headers[i]);
        }

        if (list != null && !list.isEmpty()) {
            int r = 1;
            for (FactDietariosAmbientales f : list) {
                XWPFTableRow row = table.getRow(r++);
                configurarCeldaDatos(row.getCell(0), f.getIdFact().getFormularioId()!= null ? String.valueOf(f.getIdFact().getFormularioId()) : "N/A");
                configurarCeldaDatos(row.getCell(1), (f.getTrabajoZonaRural()));
                configurarCeldaDatos(row.getCell(2), (f.getAguaConsumoRural()));
                configurarCeldaDatos(row.getCell(3), (f.getTratamientoAgua()));
                configurarCeldaDatos(row.getCell(4), (f.getFumigaciones()));
                configurarCeldaDatos(row.getCell(5), (f.getExposicionPesticidas()));
                configurarCeldaDatos(row.getCell(6), (f.getCombusLenaDiario()));
                configurarCeldaDatos(row.getCell(7), (f.getExposicionQuimicos()));
                configurarCeldaDatos(row.getCell(8), (f.getDietaAgregaSal()));
                configurarCeldaDatos(row.getCell(9), (f.getDietaFrutasVerduras()));
                configurarCeldaDatos(row.getCell(10), (f.getDietaFrituras()));
                configurarCeldaDatos(row.getCell(11), (f.getDietaCarnesCecinas()));
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
            agregarSaltoLinea(document);

            List<Histopatologia> list = histopatologiaRepository.findAll();

            String[] headers = new String[] { "formulario_id", "tipo", "estado_clinico", "locali_tumoral" };

            int rows = Math.max(1, list != null ? list.size() : 0) + 1;
            XWPFTable table = document.createTable(rows, headers.length);
            table.setWidth("100%");

            XWPFTableRow headerRow = table.getRow(0);
            for (int i = 0; i < headers.length; i++) {
                configurarCeldaEncabezado(headerRow.getCell(i), headers[i]);
            }

            if (list != null && !list.isEmpty()) {
              int r = 1;
                for (Histopatologia h : list) {
                    XWPFTableRow row = table.getRow(r++);
                    configurarCeldaDatos(row.getCell(0), h.getHistoID().getFormularioId()!= null ? String.valueOf(h.getHistoID().getFormularioId()) : "N/A");
                    configurarCeldaDatos(row.getCell(1), (h.getTipo()));
                    configurarCeldaDatos(row.getCell(2), (h.getEstadoClinico()));
                    configurarCeldaDatos(row.getCell(3), (h.getLocaliTumor()));
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

        //Factores Dietarios Ambientales 
        factDietariosAmbientalesRepository.findByIdFact_FormularioId(formulario.getIdFormulario()) 
            .ifPresent(fda -> agregarFactoresDietariosAmbientales(document, fda));        
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

    private void agregarHabitos(XWPFDocument document, HabitosPaciente hp) {
        agregarTextoNegrita(document, "Hábitos del Paciente:");
        agregarSaltoLinea(document);


        agregarSubtitulo(document,"Tabaco" ); 

        agregarTexto(document, String.format("Estado de Consumo: %s | Edad de Inicio: %s", 
            hp.getEstadoConsumoTabaco(), hp.getEdadInicioTabaco()));
        agregarTexto(document, String.format("Cantidad Promedio (unidades): %s | Tiempo en Consumo (meses): %s", 
            hp.getCantPromTabaco(), hp.getTiempoTabaco())); 
        agregarTexto(document, String.format("Tiempo sin consumir (meses): %s", 
            hp.getExConsumidorTabaco())); 

        agregarSaltoLinea(document);
        agregarSubtitulo(document,"Alcohol" ); 
        agregarSaltoLinea(document);


        agregarTexto(document, String.format("Consumo Alcohol: %s | Frecuencia: %s", 
            hp.getEstadoConsumoAlcohol(), hp.getFrecuenciaAlcohol()));
        agregarTexto(document, String.format("Cantidad: %s | Frecuencia (meses): %s | Tiempo Sin Consumo (meses): %s", 
            hp.getCantidadAlcohol(), hp.getAniosConsumoAlcohol(), hp.getExConsumidorAlcohol()));

        agregarSaltoLinea(document);
        agregarSubtitulo(document,"Ejercicio" ); 
        agregarSaltoLinea(document);


        agregarTexto(document, String.format("Ejercicio: %s | Frecuencia: %s", 
            hp.getEjercicio(), hp.getFrecuenciaEjercicio()));    

        agregarSaltoLinea(document);
    }

    private void agregarFactoresDietariosAmbientales(XWPFDocument document, FactDietariosAmbientales fda){
        agregarTextoNegrita(document, "Factores Dietario-Ambiental del Paciente:");
        agregarSaltoLinea(document);

        agregarTexto(document, String.format("Trabajo Zona Rural: %s | Consumo Agua: %s | Tratamiento Agua: %s", 
            fda.getTrabajoZonaRural(), fda.getAguaConsumoRural(), fda.getTratamientoAgua()));

        agregarTexto(document, String.format("Fumigaciones: %s | ExposicionPesticidas: %s | CombustiónLeña: %s", 
            fda.getFumigaciones(), fda.getExposicionPesticidas(), fda.getCombusLenaDiario()));

        agregarTexto(document, String.format("ExposiciónQuímicos: %s | AgregaSal: %s | ConsumoFrutaVerdura: %s", 
            fda.getExposicionQuimicos(), fda.getDietaAgregaSal(), fda.getDietaFrutasVerduras()));

        agregarTexto(document, String.format("ConsumoFrituras: %s | ConsumoCarnesCecinas: %s | ConsumoRuralAgua: %s", 
            fda.getDietaFrituras(), fda.getDietaCarnesCecinas(), fda.getAguaConsumoRural()));
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

