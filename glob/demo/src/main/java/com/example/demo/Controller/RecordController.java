package com.example.demo.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Record;
import com.example.demo.Service.RecordcrudService;



@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/records")
public class RecordController {

    private final RecordcrudService recordService;

    public RecordController(RecordcrudService recordService) {
        this.recordService = recordService;
    }

    @PostMapping("/add")
    public ResponseEntity<Record> addRecord(@RequestBody Record record) {
        Record savedRecord = recordService.addRecord(record);
        return ResponseEntity.ok(savedRecord);
    }



    /*
    @PutMapping("/update/{id}")
    public ResponseEntity<Record> updateRecord(@PathVariable Long id, @RequestBody Record updatedRecord) {
        Record updated = recordService.updateRecord(id, updatedRecord);
        return ResponseEntity.ok(updated);
    }
*/
    //  Delete a record by ID
    @DeleteMapping("/delete/{personnelNumber}")
    public ResponseEntity<String> deleteRecord(@PathVariable Long personnelNumber) {
        recordService.deleteRecordByPersonnelNumber(personnelNumber);
        return ResponseEntity.ok("Record deleted successfully.");
    }
    


}
