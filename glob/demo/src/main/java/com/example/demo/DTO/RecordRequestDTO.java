package com.example.demo.DTO;

import java.time.LocalDateTime;

public class RecordRequestDTO  {
    private String id;  // Replacing int personnel with String id
    private LocalDateTime date1;
    private LocalDateTime date2;

    public RecordRequestDTO() {}

    public RecordRequestDTO(String id, LocalDateTime date1, LocalDateTime date2) {
        this.id = id;
        this.date1 = date1;
        this.date2 = date2;
    }

    // Getter and Setter for ID
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter and Setter for date1
    public LocalDateTime getDate1() {
        return date1;
    }

    public void setDate1(LocalDateTime date1) {
        this.date1 = date1;
    }

    // Getter and Setter for date2
    public LocalDateTime getDate2() {
        return date2;
    }

    public void setDate2(LocalDateTime date2) {
        this.date2 = date2;
    }

    @Override
    public String toString() {
        return "RecordRequestDTO{" +
                "id='" + id + '\'' +
                ", date1=" + date1 +
                ", date2=" + date2 +
                '}';
    }
}
