package com.example.demo.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.Record;
import com.example.demo.Repository.RecordRepository;

@Service
public class RecordService {

    @Autowired
    private RecordRepository recordRepository;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<Record> saveRecords(ArrayList<Map<String, String>> records, List<String> errorMessages) {
        List<Record> recordsToInsert = new ArrayList<>();
        Map<String, List<LocalDateTime>> inRecordsMap = new HashMap<>();
        Map<String, List<LocalDateTime>> outRecordsMap = new HashMap<>();
        Map<String, String> prénomMap = new HashMap<>();

        for (Map<String, String> record : records) {
            try {
                String id = record.get("id");
                String status = record.get("In / Out Status");
                String timeStr = record.get("Time");
                String prénom = record.get("Prénom");

                if (id == null || id.isEmpty()) {
                    errorMessages.add("Error: Missing 'id' for Prénom: " + prénom);
                    continue;
                }

                LocalDateTime time = (timeStr != null && !timeStr.isEmpty()) ?
                        LocalDateTime.parse(timeStr, DATE_TIME_FORMATTER) : null;

                if (time == null) {
                    errorMessages.add("Error: Missing 'Time' for ID: " + id + ", Prénom: " + prénom);
                    continue;
                }

                if (prénom != null && !prénom.isEmpty()) {
                    prénomMap.putIfAbsent(id, prénom);
                }

                if ("Check-In".equalsIgnoreCase(status) || "OT-In".equalsIgnoreCase(status)) {
                    inRecordsMap.computeIfAbsent(id, k -> new ArrayList<>()).add(time);
                } else if ("Check-Out".equalsIgnoreCase(status) || "OT-Out".equalsIgnoreCase(status)) {
                    outRecordsMap.computeIfAbsent(id, k -> new ArrayList<>()).add(time);
                }
            } catch (Exception e) {
                errorMessages.add("Error: Invalid data format - " + e.getMessage());
            }
        }

        for (Map.Entry<String, List<LocalDateTime>> entry : inRecordsMap.entrySet()) {
            String id = entry.getKey();
            List<LocalDateTime> inTimes = entry.getValue();
            List<LocalDateTime> outTimes = outRecordsMap.getOrDefault(id, new ArrayList<>());

            Collections.sort(inTimes);
            Collections.sort(outTimes);

            int outIndex = 0;
            LocalDateTime lastOutTime = null;

            for (int i = 0; i < inTimes.size(); i++) {
                LocalDateTime inTime = inTimes.get(i);

                if (lastOutTime != null && !inTime.isAfter(lastOutTime)) {
                    continue;
                }

                LocalDateTime validOutTime = null;
                while (outIndex < outTimes.size()) {
                    LocalDateTime outTime = outTimes.get(outIndex);

                    if (outTime.isAfter(inTime) &&
                            (outTime.toLocalDate().isEqual(inTime.toLocalDate()) || // Same day
                                    (outTime.toLocalDate().isEqual(inTime.toLocalDate().plusDays(1)) &&
                                            outTime.getHour() <= 3))) { // Next day before 03:00 AM
                        validOutTime = outTime;
                        outIndex++; // Move to the next Out Time
                        lastOutTime = validOutTime; // Track the last processed "Out"
                        break;
                    }
                    outIndex++;
                }

                if (validOutTime != null) {
                    Record newRecord = new Record();
                    newRecord.setIdd(id);
                    newRecord.setInTime(inTime);
                    newRecord.setOutTime(validOutTime);
                    newRecord.setPrenom(prénomMap.get(id));

                    recordsToInsert.add(newRecord);
                } else {
                    errorMessages.add("Warning: No valid 'Out' record found for In-Time: " + inTime +
                            " for ID: " + id + ", Prénom: " + prénomMap.get(id));
                }
            }
        }

        if (!recordsToInsert.isEmpty()) {
            recordRepository.saveAll(recordsToInsert);
        }

        return recordsToInsert;
    }

    public List<Record> getRecordsBetweenDates(String id, LocalDateTime date1, LocalDateTime date2) {
        return recordRepository.findByIdAndDateRange(id, date1, date2); // Updated query method to use id
    }

    public List<Record> getAllRecordsBetweenDates(LocalDateTime date1, LocalDateTime date2) {
        return recordRepository.findAllBetweenDates(date1, date2);
    }
}
