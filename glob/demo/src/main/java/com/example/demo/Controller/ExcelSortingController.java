package com.example.demo.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Entity.Record;
import com.example.demo.Service.RecordService;
import com.example.demo.Service.SortingService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class ExcelSortingController {

    @Autowired
    private SortingService sortingService;

    @Autowired
    private RecordService recordService;

    @PostMapping("/excel")
    public ResponseEntity<Map<String, Object>> uploadExcel(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        List<String> errorMessages = new ArrayList<>();

        if (file.isEmpty()) {
            response.put("error", "File is empty. Please upload a valid file.");
            return ResponseEntity.badRequest().body(response);
        }

        if (file.getOriginalFilename() == null) {
            response.put("error", "File name is invalid.");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            List<Map<String, String>> records = extractRecordsFromExcel(file, errorMessages);
            ArrayList<Map<String, String>> sortedRecords = sortingService.sortRecords(new ArrayList<>(records));

            List<Record> insertedRecords = recordService.saveRecords(sortedRecords, errorMessages);

            response.put("insertedRecords", insertedRecords);
            response.put("errors", errorMessages);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (IOException e) {
            response.put("error", "Error processing the file: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    private List<Map<String, String>> extractRecordsFromExcel(MultipartFile file, List<String> errorMessages) throws IOException {
        List<Map<String, String>> records = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
            Workbook workbook = file.getOriginalFilename().endsWith(".xls") ?
                    new HSSFWorkbook(inputStream) :
                    new XSSFWorkbook(inputStream)) {

            // Get the first sheet from the workbook
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);

            if (headerRow == null) {
                throw new IllegalArgumentException("The Excel file is empty or does not contain a header row.");
            }

            // Map column headers to their indices
            Map<String, Integer> columnIndex = new HashMap<>();
            for (Cell cell : headerRow) {
                String header = cell.getStringCellValue().trim().toLowerCase();
                switch (header) {
                    case "time":
                        columnIndex.put("Time", cell.getColumnIndex());
                        break;
                    case "id":
                        columnIndex.put("id", cell.getColumnIndex()); // Replacing "Nombre du personnel" with "id"
                        break;
                    case "in / out status":
                        columnIndex.put("In / Out Status", cell.getColumnIndex());
                        break;
                    case "prénom":
                        columnIndex.put("Prénom", cell.getColumnIndex());
                        break;
                }
            }

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
                                value = String.valueOf((int) cell.getNumericCellValue()); // Avoid decimal conversion
                                break;
                            case BOOLEAN:
                                value = String.valueOf(cell.getBooleanCellValue());
                                break;
                        }
                    }
                    record.put(key, value);
                }

                // Ensure "id" is present instead of "Nombre du personnel"
                if (!record.containsKey("id") || record.get("id").isEmpty()) {
                    errorMessages.add("Missing 'id' at row " + (i + 1));
                    continue;
                }

                if (!record.containsKey("In / Out Status") || record.get("In / Out Status").isEmpty() ||
                        !record.containsKey("Time") || record.get("Time").isEmpty()) {
                    errorMessages.add("Missing 'In / Out Status' or 'Time' at row " + (i + 1));
                    continue;
                }

                records.add(record);
            }
        }

        return records;
    }
}
