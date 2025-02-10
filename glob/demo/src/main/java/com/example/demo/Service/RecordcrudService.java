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

    // ✅ Get a record by ID
    public Optional<Record> getRecordById(Long id) {
        return recordRepository.findById(id);
    }

    // ✅ Update an existing record
    public Record updateRecord(Long id, Record updatedRecord) {
        return recordRepository.findById(id)
                .map(record -> {
                    record.setNombreDuPersonnel(updatedRecord.getNombreDuPersonnel());
                    record.setPrenom(updatedRecord.getPrenom());
                    record.setInTime(updatedRecord.getInTime());
                    record.setOutTime(updatedRecord.getOutTime());
                    return recordRepository.save(record);
                })
                .orElseThrow(() -> new RuntimeException("Record not found with ID: " + id));
    }

    
    //  Delete a record by ID
    @Transactional
    public void deleteRecordByPersonnelNumber(Long personnelNumber) {
        if (!recordRepository.existsByNombreDuPersonnel(personnelNumber)) {
            throw new RuntimeException("Record not found with Personnel Number: " + personnelNumber);
        }
        recordRepository.deleteByNombreDuPersonnel(personnelNumber);
    }
    
}
