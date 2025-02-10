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

        // Sorting first by "Nombre du personnel", then by "Time"
        records.sort((record1, record2) -> {
            int personnel1 = parsePersonnel(record1.get("Nombre du personnel"));
            int personnel2 = parsePersonnel(record2.get("Nombre du personnel"));

            // First sort by personnel ID
            int personnelComparison = Integer.compare(personnel1, personnel2);
            if (personnelComparison != 0) {
                return personnelComparison;
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

    private int parsePersonnel(String personnel) {
        try {
            return (personnel != null && !personnel.isEmpty()) ? Integer.parseInt(personnel) : Integer.MAX_VALUE;
        } catch (NumberFormatException e) {
            return Integer.MAX_VALUE; // Sort invalid personnel IDs at the end
        }
    }
}
