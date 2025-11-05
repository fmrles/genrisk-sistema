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

    public byte[] exportarDicotomizacionAExcel() throws Exception {
        
        // 1. Obtenengo todos los valores dicotomizados
        List<DicotValor> valores = dicotValorRepository.findAll();

        // 2. Proceso y pivoteo los datos
        Map<String, Map<String, Integer>> pivotData = new LinkedHashMap<>();
        Set<String> categorias = new TreeSet<>();

        for (DicotValor valor : valores) {
            String pacienteId = valor.getFormulario().getPaciente().getIdPaciente();
            String categoria = valor.getCategoria();
            Integer valordicico = valor.getValordicico();

            categorias.add(categoria);

            Map<String, Integer> pacienteRow = pivotData.computeIfAbsent(pacienteId, k -> new LinkedHashMap<>());
            pacienteRow.put(categoria, valordicico);

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

            sheet.createRow(rowNum); 
            for (int i = 0; i <= categorias.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            sheet.removeRow(sheet.getRow(rowNum)); 


            workbook.write(baos);
            return baos.toByteArray();
        }
    }
}
