package com.example.demo.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class extract {

    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        List<Map<String, String>> records = new ArrayList<>();
        
        Workbook workbook;
        try {
            if (file.getOriginalFilename().endsWith(".xls")) {
                workbook = new HSSFWorkbook(file.getInputStream());
            } else if (file.getOriginalFilename().endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(file.getInputStream());
            } else {
                return ResponseEntity.badRequest().body(Collections.singletonList("Invalid file format. Please upload a .xls or .xlsx file."));
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
        
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        
        Row headerRow = sheet.getRow(0);
        Map<String, Integer> columnIndex = new HashMap<>();
        
        System.out.println("Excel Headers: ");
        for (Cell cell : headerRow) {
            String header = cell.getStringCellValue().replaceAll("[^\\p{Print}]", "").trim();
            System.out.println("Header found: [" + header + "]");
            
            switch (header.toLowerCase().replaceAll("[^a-zA-Z0-9]", "")) {
                case "time":
                case "date":
                    columnIndex.put("Time", cell.getColumnIndex());
                    break;
                case "nombredupersonnel":
                    columnIndex.put("Nombre du personnel", cell.getColumnIndex());
                    break;
                case "prenom":
                case "nomprenom":
                    columnIndex.put("Prénom", cell.getColumnIndex());
                    break;
                case "inoutstatus":
                    columnIndex.put("In / Out Status", cell.getColumnIndex());
                    break;
            }
        }
        System.out.println("Detected columns: " + columnIndex);
        
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            
            Map<String, String> record = new HashMap<>();
            for (String key : columnIndex.keySet()) {
                Cell cell = row.getCell(columnIndex.get(key));
                String value = "";
                
                if (cell != null) {
                    switch (cell.getCellType()) {
                        case STRING:
                            value = cell.getStringCellValue().trim();
                            break;
                        case NUMERIC:
                            value = String.valueOf((int) cell.getNumericCellValue());
                            break;
                        case BOOLEAN:
                            value = String.valueOf(cell.getBooleanCellValue());
                            break;
                        default:
                            value = "";
                    }
                }
                record.put(key, value);
            }
            
            System.out.println("Processed row: " + record);
            records.add(record);
        }
        
        records.sort(Comparator.comparing((Map<String, String> record) -> record.get("Time"), Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(record -> {
                    String personnel = record.get("Nombre du personnel");
                    return (personnel != null && !personnel.isEmpty()) ? Integer.parseInt(personnel) : Integer.MAX_VALUE;
                }));
        
        List<String> response = new ArrayList<>();
        for (Map<String, String> record : records) {
            response.add(String.format("( %s,  %s , %s , %s )", 
                    record.getOrDefault("Time", ""),
                    record.getOrDefault("Nombre du personnel", ""),
                    record.getOrDefault("Prénom", ""),
                    record.getOrDefault("In / Out Status", "")));
        }
        
        return ResponseEntity.ok(response);
    }
}
