package com.example.demo.Controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.RecordRequestDTO;
import com.example.demo.Entity.Record;
import com.example.demo.Service.RecordService;
/*
@RestController
@RequestMapping("/api/point")
public class PointController {

    private final RecordService recordService;

    public PointController(RecordService recordService) {
        this.recordService = recordService;
    }

@PostMapping("/fetch")
public ResponseEntity<List<Record>> getRecordsBetweenDates(@RequestBody RecordRequestDTO requestDTO) {
        List<Record> allRecords = recordService.getRecordsBetweenDates(
                requestDTO.getPersonnel(),
                requestDTO.getDate1(),
                requestDTO.getDate2()
        );

        // Use HashSet to remove duplicates
        Set<String> uniqueRecords = new HashSet<>();
        List<Record> filteredRecords = allRecords.stream()
            .filter(record -> {
                String uniqueKey = record.getNombreDuPersonnel() + "-" + record.getInTime() + "-" + record.getOutTime();
                return uniqueRecords.add(uniqueKey); // Returns false if duplicate exists
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok(filteredRecords);
    }




    @PostMapping("/fetch/all")
    public ResponseEntity<Map<String, Object>> getAllRecordsBetweenDates(@RequestBody RecordRequestDTO requestDTO) {
        // Fetch records from service
        List<Record> allRecords = recordService.getAllRecordsBetweenDates(
            requestDTO.getDate1(),
            requestDTO.getDate2()
        );
    
        //  Remove exact duplicates using a Set
        Set<String> uniqueRecords = new HashSet<>();
        List<Record> filteredRecords = allRecords.stream()
            .filter(record -> uniqueRecords.add(
                record.getNombreDuPersonnel() + "-" + record.getInTime() + "-" + record.getOutTime()
            ))
            .collect(Collectors.toList());
    
        //  Create JSON response
        Map<String, Object> response = new HashMap<>();
        response.put("totalRecords", filteredRecords.size());
        response.put("records", filteredRecords);
    
        return ResponseEntity.ok(response);
    }

}*/


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/point")
public class PointController {

    private final RecordService recordService;

    public PointController(RecordService recordService) {
        this.recordService = recordService;
    }

    @PostMapping("/fetch")
    public ResponseEntity<List<Record>> getRecordsBetweenDates(@RequestBody RecordRequestDTO requestDTO) {
        List<Record> allRecords = recordService.getRecordsBetweenDates(
                requestDTO.getPersonnel(),
                requestDTO.getDate1(),
                requestDTO.getDate2()
        );

        List<Record> filteredRecords = removeDuplicates(allRecords);

        return ResponseEntity.ok(filteredRecords);
    }

    @PostMapping("/fetch/all")
    public ResponseEntity<Map<String, Object>> getAllRecordsBetweenDates(@RequestBody RecordRequestDTO requestDTO) {
        List<Record> allRecords = recordService.getAllRecordsBetweenDates(
                requestDTO.getDate1(),
                requestDTO.getDate2()
        );

        
        List<Record> filteredRecords = removeDuplicates(allRecords);


        Map<String, Object> response = new HashMap<>();
        response.put("totalRecords", filteredRecords.size());
        response.put("records", filteredRecords);

        return ResponseEntity.ok(response);
    }

    private List<Record> removeDuplicates(List<Record> records) {
        Set<String> uniqueRecords = new HashSet<>();
        return records.stream()
                .filter(record -> uniqueRecords.add(recordKey(record)))
                .collect(Collectors.toList());
    }

    private String recordKey(Record record) {
        return normalizeValue(record.getNombreDuPersonnel()) + "|" +
            normalizeValue(record.getPrenom()) + "|" +
            normalizeValue(record.getInTime()) + "|" +
            normalizeValue(record.getOutTime());
    }

    private String normalizeValue(Object value) {
        return value == null ? "" : value.toString().trim();
    }
}