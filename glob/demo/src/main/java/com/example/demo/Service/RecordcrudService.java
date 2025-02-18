package com.example.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.Entity.Record;
import com.example.demo.Repository.RecordRepository;

import jakarta.transaction.Transactional;

@Service
public class RecordcrudService {

    private final RecordRepository recordRepository;

    public RecordcrudService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    // ✅ Add a new record
    public Record addRecord(Record record) {
        return recordRepository.save(record);
    }

    // ✅ Get all records
    public List<Record> getAllRecords() {
        return recordRepository.findAll();
    }

    // ✅ Get a record by ID (Updated to use String id)
    public Optional<Record> getRecordById(String id) {
        return recordRepository.findByIdd(id);
    }

    // ✅ Update an existing record (Uses String id instead of Long)
    public Record updateRecord(long id, Record updatedRecord) {
        return recordRepository.findById(id)
                .map(record -> {
                    record.setIdd(updatedRecord.getIdd()); // Changed from setNombreDuPersonnel
                    record.setPrenom(updatedRecord.getPrenom());
                    record.setInTime(updatedRecord.getInTime());
                    record.setOutTime(updatedRecord.getOutTime());
                    return recordRepository.save(record);
                })
                .orElseThrow(() -> new RuntimeException("Record not found with ID: " + id));
    }

    // ✅ Delete a record by ID (Updated from Personnel Number to ID)
    @Transactional
    public void deleteRecordById(long id) {
        if (!recordRepository.existsById(id)) {
            throw new RuntimeException("Record not found with ID: " + id);
        }
        recordRepository.deleteById(id);
    }
}
