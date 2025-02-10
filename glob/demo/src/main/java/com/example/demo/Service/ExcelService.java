package com.example.demo.Service;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class ExcelService {

    public List<Map<String, String>> parseExcelFile(MultipartFile file) throws IOException {
        List<Map<String, String>> records = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
            Workbook workbook = file.getOriginalFilename().endsWith(".xls") ?
                    new HSSFWorkbook(inputStream) : new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);

            if (headerRow == null) {
                throw new IllegalArgumentException("The Excel file is empty or missing headers.");
            }

            // Map column headers to their indices
            Map<String, Integer> columnIndex = new HashMap<>();
            for (Cell cell : headerRow) {
                String header = cell.getStringCellValue().trim().toLowerCase();
                switch (header) {
                    case "time":
                        columnIndex.put("Time", cell.getColumnIndex());
                        break;
                    case "nombre du personnel":
                        columnIndex.put("Nombre du personnel", cell.getColumnIndex());
                        break;
                    case "in / out status":
                        columnIndex.put("In / Out Status", cell.getColumnIndex());
                        break;
                    case "prénom":
                        columnIndex.put("Prénom", cell.getColumnIndex());
                        break;
                }
            }

            // Extract data rows
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
                        }
                    }
                    record.put(key, value);
                }
                records.add(record);
            }
        }
        return records;
    }
}
