package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.DicotValor;
import com.genrisk.sistema.repository.DicotValorRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Service
public class ExcelExportService {

    @Autowired
    private DicotValorRepository dicotValorRepository;
    
    /**
     * Estandariza los nombres de las categorías (columnas) para el reporte final de Excel.
     */
    private String estandarizarNombreCategoria(String categoria) {
        return switch (categoria) {
            // --- DATOS GENERALES ---
            case "zona", "zona_residencial" -> "zonaResidencial";
            case "anios_resi_actual" -> "aniosResiActual";
            
            // --- HÁBITOS PACIENTE ---
            case "estado_cons_alcohol", "estado_consumo_alcohol" -> "estadoConsumoAlcohol";
            case "estado_cons_tabaco", "estado_consumo_tabaco", "estado" -> "estadoConsumoTabaco";
            case "edad_inicio_tabaco" -> "edadInicioTabaco";
            case "cant_prom_tabaco" -> "cantPromTabaco";
            case "tiempo_tabaco" -> "tiempoTabaco";
            case "ex_consumidor_tabaco" -> "exConsumidorTabaco";
            case "frecuencia_alcohol", "frecuencia" -> "frecuenciaAlcohol";
            case "cantidad_alcohol" -> "cantidadAlcohol";
            case "anios_consumo_alcohol" -> "aniosConsumoAlcohol";
            case "ex_consumidor_alcohol" -> "exConsumidorAlcohol";
            case "frecuencia_ejercicio" -> "frecuenciaEjercicio";

            // --- DATOS CLÍNICOS ---
            case "adeno_gastrico" -> "adenoGastrico";
            case "ant_fam_cancer_gast" -> "antFamCancerGast";
            case "hpylori_tiempo_test" -> "hpyloriTiempoTest";

            // --- FACTORES DIETARIOS AMBIENTALES ---
            case "trabajo_zona_rural" -> "trabajoZonaRural";
            case "agua_consumo_zona" -> "aguaConsumoZona";
            case "tratamiento_agua" -> "tratamientoAgua";
            case "exposicion_pesticidas" -> "exposicionPesticidas";
            case "combus_lena_diario" -> "combusLenaDiario";
            case "exposicion_quimicos" -> "exposicionQuimicos";
            case "dieta_agregasal" -> "dietaAgregaSal";
            case "dieta_frutas_verduras" -> "dietaFrutasVerduras";
            case "dieta_frituras" -> "dietaFrituras";
            case "dieta_carnes_cecinas" -> "dietaCarnesCecinas";
            
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
    private byte[] crearExcel(List<DicotValor> valores) throws Exception {
        
        // 1. Procesar y pivotar los datos
        Map<String, Map<String, Integer>> pivotData = new LinkedHashMap<>();
        Set<String> categorias = new TreeSet<>();

        for (DicotValor valor : valores) {
            String pacienteId = valor.getFormulario().getPaciente().getIdPaciente();
            String categoriaEstandarizada = estandarizarNombreCategoria(valor.getCategoria());
            Integer valordicico = valor.getValordicico();

            categorias.add(categoriaEstandarizada);
            Map<String, Integer> pacienteRow = pivotData.computeIfAbsent(pacienteId, k -> new LinkedHashMap<>());
            pacienteRow.put(categoriaEstandarizada, valordicico);
        }

        // 2. Crear el libro de Excel
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            
            XSSFSheet sheet = workbook.createSheet("Dicotomizacion");

            // 3. Crear la Fila de Cabeceras
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("paciente_id");

            int colNum = 1;
            for (String categoria : categorias) {
                headerRow.createCell(colNum++).setCellValue(categoria);
            }

            // 4. Llenar las Filas de Datos
            int rowNum = 1;
            for (Map.Entry<String, Map<String, Integer>> entry : pivotData.entrySet()) {
                String pacienteId = entry.getKey();
                Map<String, Integer> datosFila = entry.getValue();
                
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(pacienteId); 

                colNum = 1;
                for (String categoria : categorias) {
                    Integer valor = datosFila.getOrDefault(categoria, 0); 
                    row.createCell(colNum++).setCellValue(valor);
                }
            }

            // 5. Autoajustar el tamaño de las columnas
            Row adjustmentRow = sheet.createRow(rowNum);
            for (int i = 0; i <= categorias.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            sheet.removeRow(adjustmentRow);


            workbook.write(baos);
            return baos.toByteArray();
        }
    }
}
