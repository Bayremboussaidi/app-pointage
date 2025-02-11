package com.example.demo.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class SortingService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ArrayList<Map<String, String>> sortRecords(ArrayList<Map<String, String>> records) {
        if (records == null || records.isEmpty()) {
            return new ArrayList<>();
        }

        // Sorting first by "id", then by "Time"
        records.sort((record1, record2) -> {
            String id1 = record1.getOrDefault("id", "").trim();
            String id2 = record2.getOrDefault("id", "").trim();

            // First sort by id (lexicographically)
            int idComparison = id1.compareTo(id2);
            if (idComparison != 0) {
                return idComparison;
            }

            // Then sort by Time
            LocalDateTime time1 = parseDateTime(record1.get("Time"));
            LocalDateTime time2 = parseDateTime(record2.get("Time"));

            return time1.compareTo(time2);
        });

        return new ArrayList<>(records);
    }

    private LocalDateTime parseDateTime(String time) {
        try {
            if (time == null || time.isEmpty()) {
                return LocalDateTime.MAX; // Sort missing times at the end
            }
            return LocalDateTime.parse(time, DATE_TIME_FORMATTER);
        } catch (Exception e) {
            return LocalDateTime.MAX; // Sort invalid dates at the end
        }
    }
}
