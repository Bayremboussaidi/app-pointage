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

    public Record addRecord(Record record) {
        return recordRepository.save(record);
    }

    public List<Record> getAllRecords() {
        return recordRepository.findAll();
    }

    public Optional<Record> getRecordById(String id) {
        return recordRepository.findByIdd(id);
    }

    public Record updateRecord(long id, Record updatedRecord) {
        return recordRepository.findById(id)
                .map(record -> {
                    record.setIdd(updatedRecord.getIdd());
                    record.setPrenom(updatedRecord.getPrenom());
                    record.setInTime(updatedRecord.getInTime());
                    record.setOutTime(updatedRecord.getOutTime());
                    return recordRepository.save(record);
                })
                .orElseThrow(() -> new RuntimeException("Record not found with ID: " + id));
    }

    @Transactional
    public void deleteRecordById(long id) {
        if (!recordRepository.existsById(id)) {
            throw new RuntimeException("Record not found with ID: " + id);
        }
        recordRepository.deleteById(id);
    }
}
