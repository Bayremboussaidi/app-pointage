package com.example.demo.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<Integer, List<LocalDateTime>> inRecordsMap = new HashMap<>();
        Map<Integer, List<LocalDateTime>> outRecordsMap = new HashMap<>();
        Map<Integer, String> prénomMap = new HashMap<>();

        // Step 1: Categorize records into separate In and Out lists
        for (Map<String, String> record : records) {
            try {
                String personnelStr = record.get("Nombre du personnel");
                String status = record.get("In / Out Status");
                String timeStr = record.get("Time");
                String prénom = record.get("Prénom");

                if (personnelStr == null || personnelStr.isEmpty()) {
                    errorMessages.add("Error: Missing 'Nombre du personnel' for Prénom: " + prénom);
                    continue;
                }

                int personnel = Integer.parseInt(personnelStr);
                LocalDateTime time = (timeStr != null && !timeStr.isEmpty()) ?
                        LocalDateTime.parse(timeStr, DATE_TIME_FORMATTER) : null;

                if (time == null) {
                    errorMessages.add("Error: Missing 'Time' for Personnel ID: " + personnel + ", Prénom: " + prénom);
                    continue;
                }

                if (prénom != null && !prénom.isEmpty()) {
                    prénomMap.putIfAbsent(personnel, prénom);
                }

                if ("Check-In".equalsIgnoreCase(status) || "OT-In".equalsIgnoreCase(status)) {
                    inRecordsMap.computeIfAbsent(personnel, k -> new ArrayList<>()).add(time);
                } else if ("Check-Out".equalsIgnoreCase(status) || "OT-Out".equalsIgnoreCase(status)) {
                    outRecordsMap.computeIfAbsent(personnel, k -> new ArrayList<>()).add(time);
                }
            } catch (Exception e) {
                errorMessages.add("Error: Invalid data format - " + e.getMessage());
            }
        }

        // Step 2: Match "In" times with the next valid "Out"
        for (Map.Entry<Integer, List<LocalDateTime>> entry : inRecordsMap.entrySet()) {
            int personnel = entry.getKey();
            List<LocalDateTime> inTimes = entry.getValue();
            List<LocalDateTime> outTimes = outRecordsMap.getOrDefault(personnel, new ArrayList<>());

            Collections.sort(inTimes);
            Collections.sort(outTimes);

            int outIndex = 0;
            LocalDateTime lastOutTime = null;

            for (int i = 0; i < inTimes.size(); i++) {
                LocalDateTime inTime = inTimes.get(i);

                // Ensure this "In" comes AFTER the last processed "Out"
                if (lastOutTime != null && !inTime.isAfter(lastOutTime)) {
                    continue; // Skip this "In" because it's before or at the last processed "Out"
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
                    newRecord.setNombreDuPersonnel(personnel);
                    newRecord.setInTime(inTime);
                    newRecord.setOutTime(validOutTime);
                    newRecord.setPrenom(prénomMap.get(personnel));

                    recordsToInsert.add(newRecord);
                } else {
                    errorMessages.add("Warning: No valid 'Out' record found for In-Time: " + inTime +
                            " for Personnel ID: " + personnel + ", Prénom: " + prénomMap.get(personnel));
                }
            }
        }

        if (!recordsToInsert.isEmpty()) {
            recordRepository.saveAll(recordsToInsert);
        }

        return recordsToInsert;
    }

    public List<Record> getRecordsBetweenDates(int personnelId, LocalDateTime date1, LocalDateTime date2) {
        return recordRepository.findByPersonnelAndDateRange(personnelId, date1, date2);
    }

    public List<Record> getAllRecordsBetweenDates(LocalDateTime date1, LocalDateTime date2) {
        return recordRepository.findAllBetweenDates(date1, date2);
    }
}