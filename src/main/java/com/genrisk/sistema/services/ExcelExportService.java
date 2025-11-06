package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.DicotValor;
import com.genrisk.sistema.repository.DicotValorRepository;
import org.apache.poi.ss.usermodel.Cell;
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

    private String estandarizarNombreCategoria(String categoria) {
        return switch (categoria) {
            // --- DATOS GENERALES ---
            case "zona", "zona_residencial" -> "zonaResidencial"; 
            case "anios_resi_actual" -> "aniosResiActual";
            case "edad" -> "edad";
            case "sexo" -> "sexo";
            case "peso" -> "peso";
            case "imc" -> "imc";
            case "estatura" -> "estatura";
            case "educacion" -> "educacion";
            case "ocupacion" -> "ocupacion";

            // --- HÁBITOS PACIENTE ---
            case "estado_cons_tabaco", "estado_consumo_tabaco", "estado" -> "estadoConsumoTabaco"; 
            case "edad_inicio_tabaco" -> "edadInicioTabaco";
            case "cant_prom_tabaco" -> "cantPromTabaco";
            case "tiempo_tabaco" -> "tiempoTabaco";
            case "ex_consumidor_tabaco" -> "exConsumidorTabaco";
            case "estado_cons_alcohol", "estado_consumo_alcohol" -> "estadoConsumoAlcohol";
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
            case "fumigaciones" -> "fumigaciones";
            case "exposicion_pesticidas" -> "exposicionPesticidas";
            case "combus_lena_diario" -> "combusLenaDiario";
            case "exposicion_quimicos" -> "exposicionQuimicos";
            case "dieta_agregasal" -> "dietaAgregaSal";
            case "dieta_frutas_verduras" -> "dietaFrutasVerduras";
            case "dieta_frituras" -> "dietaFrituras";
            case "dieta_carnes_cecinas" -> "dietaCarnesCecinas";

            // Si no está en la lista de inconsistencias, se usa el nombre original.
            default -> categoria; 
        };
    }

    public byte[] exportarDicotomizacionAExcel() throws Exception {
        
        // 1. Obtenengo todos los valores dicotomizados
        List<DicotValor> valores = dicotValorRepository.findAll();

        // 2. Proceso y pivoteo los datos
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

        // 3. Creo el libro de Excel
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            
            XSSFSheet sheet = workbook.createSheet("Dicotomizacion");

            // 4. Creo la Fila de Cabeceras
            Row headerRow = sheet.createRow(0);
            Cell headerCell = headerRow.createCell(0);
            headerCell.setCellValue("paciente_id");

            // Creo las cabeceras de categorías
            int colNum = 1;
            for (String categoria : categorias) {
                headerRow.createCell(colNum++).setCellValue(categoria);
            }

            // 5. Lleno las Filas de Datos
            int rowNum = 1;
            for (Map.Entry<String, Map<String, Integer>> entry : pivotData.entrySet()) {
                String pacienteId = entry.getKey();
                Map<String, Integer> datosFila = entry.getValue();
                
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(pacienteId); // Columna 1: ID Paciente

                colNum = 1;
                for (String categoria : categorias) {
                    Integer valor = datosFila.getOrDefault(categoria, 0); 
                    row.createCell(colNum++).setCellValue(valor);
                }
            }

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
