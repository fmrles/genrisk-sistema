package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.DicotValor;
import com.genrisk.sistema.model.entity.Paciente;
import com.genrisk.sistema.repository.DicotValorRepository;
import com.genrisk.sistema.repository.PacienteRepository;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Service
public class ExcelExportService {

    @Autowired
    private DicotValorRepository dicotValorRepository;
    @Autowired
    private PacienteRepository pacienteRepository;
    
    /**
     * Estandariza los nombres de las categorías (columnas) para el reporte final de Excel.
     */
    private String estandarizarNombreCategoria(String categoria) {
        if (categoria == null) return "Desconocido";
        
        return switch (categoria) {
            // --- DATOS GENERALES ---
            case "edad" -> "Edad";
            case "edad_promedio" -> "Edad_promedio"; 
            case "edad_mediana" -> "Edad_mediana";   
            case "edad_50" -> "Edad_50";
            case "sexo" -> "Sexo";
            case "residencia_urbana_5" -> "Residencia_Urbana5"; 
            case "residencia_rural_5" -> "Residencia_Rural5a"; 
            
            // --- TABACO ---
            case "tabaco_moderada_carga" -> "tabaco_moderada_carga"; 
            case "tabaco_grave_carga" -> "tabaco_grave_carga";       
            case "estado_consumo_tabaco" -> "tabaco_nunca_vs_otros";
            
            // --- ALCOHOL ---
            case "alcohol_moderada_carga" -> "alcohol_moderada_carga";
            
            // --- DIETA ---
            case "dieta_carnes_cecinas" -> "CarnesProcesadas_Menos3"; 
            case "dieta_frutas_verduras" -> "FrutasVerduras_3oMas";
            case "dieta_agregasal" -> "AñadeSal_Comida";
            case "dieta_frituras" -> "Frituras_Frecuente";

            // --- OTROS ---
            case "hpylori_resultado" -> "HPylori_AlgunaVezPositivo";
            default -> categoria;
        };
    }
    
    // --- MÉTODO PÚBLICO (TODOS) ---
    /**
     * Exporta los valores dicotomizados de TODOS los pacientes a Excel.
     */
    public byte[] exportarDicotomizacionAExcel() throws Exception {
        List<DicotValor> valores = dicotValorRepository.findAll();
        return crearExcel(valores);
    }

    //MÉTODO PÚBLICO (POR PACIENTE) ---
    /**
     * Exporta los valores dicotomizados de UN paciente específico a Excel.
     */
    public byte[] exportarDicotomizacionAExcel(String pacienteId) throws Exception {
        List<DicotValor> valores = dicotValorRepository.findByFormularioPacienteIdPaciente(pacienteId);
        // Si el paciente no tiene valores, el helper creará un Excel vacío (solo cabeceras).
        return crearExcel(valores);
    }
    /**
     * Exporta los valores dicotomizados de UN conjunto de reglas específico a Excel.
     */
    public byte[] exportarDicotomizacionAExcelPorConjunto(Integer conjuntoId) throws Exception {
        List<DicotValor> valores = dicotValorRepository.findByDicotReglaDicotConjuntoIdDicotConjunto(conjuntoId);
        return crearExcel(valores);
    }
    
    // --- HELPER PRIVADO (LÓGICA DE CREACIÓN DE EXCEL) ---
    /**
     * Método privado que toma una lista de DicotValor y genera el archivo Excel.
     */
    private byte[] crearExcel(List<DicotValor> valoresEncontrados) throws Exception {
        
        List<Paciente> todosLosPacientes = pacienteRepository.findAll(); 

        Map<String, Map<String, Integer>> datosPivot = new LinkedHashMap<>();
        Set<String> columnasCategorias = new TreeSet<>();

        for (DicotValor valor : valoresEncontrados) {
            if (valor.getFormulario() != null && valor.getFormulario().getPaciente() != null) {
                String pid = valor.getFormulario().getPaciente().getIdPaciente();
                String cat = estandarizarNombreCategoria(valor.getCategoria());
                Integer val = valor.getValordicico();

                columnasCategorias.add(cat);
                
                datosPivot.computeIfAbsent(pid, k -> new HashMap<>()).put(cat, val);
            }
        }

        // 3. Crear el libro de Excel
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            
            XSSFSheet sheet = workbook.createSheet("Matriz Dicotomizada");

            // --- CREAR ENCABEZADOS (FILA 0) ---
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID Paciente"); // Primera columna fija
            headerRow.createCell(1).setCellValue("Tipo");        // Segunda columna útil (Caso/Control)

            int colNum = 2;
            for (String cat : columnasCategorias) {
                headerRow.createCell(colNum++).setCellValue(cat);
            }

            // --- LLENAR DATOS (FILA 1 en adelante) ---
            // Iteramos sobre TODOS los pacientes, no solo los que tienen datos en 'datosPivot'
            int rowNum = 1;
            for (Paciente paciente : todosLosPacientes) {
                String pacienteId = paciente.getIdPaciente();
                
                // Recuperamos los datos dicotomizados de este paciente (si existen)
                Map<String, Integer> datosDeEstePaciente = datosPivot.getOrDefault(pacienteId, new HashMap<>());
                
                Row row = sheet.createRow(rowNum++);
                
                // Columna 0: ID
                row.createCell(0).setCellValue(pacienteId);
                
                // Columna 1: Tipo de Paciente (útil para filtrar en Excel/STATA)
                row.createCell(1).setCellValue(paciente.getTipoPaciente());

                // Columnas 2...N: Variables dicotomizadas
                colNum = 2;
                for (String cat : columnasCategorias) {
                    Integer valor = datosDeEstePaciente.get(cat);
                    
                    if (valor != null) {
                        // Si existe el dato (0 o 1), lo escribimos
                        row.createCell(colNum++).setCellValue(valor);
                    } else {
                        // Si NO existe dato, dejamos la celda en blanco (NULL en STATA)
                        // O podrías poner row.createCell(colNum++).setCellValue("."); 
                        row.createCell(colNum++).setBlank();
                    }
                }
            }

            // 4. Autoajustar el ancho de las columnas para que se vea bonito
            for (int i = 0; i < colNum; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(baos);
            return baos.toByteArray();
        }
    }
}
