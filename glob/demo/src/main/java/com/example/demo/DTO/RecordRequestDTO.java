package com.example.demo.DTO;

import java.time.LocalDateTime;

public class RecordRequestDTO  {
    private int personnel;
    private LocalDateTime date1;
    private LocalDateTime date2;

    public RecordRequestDTO() {}

    public RecordRequestDTO(int personnel, LocalDateTime date1, LocalDateTime date2) {
        this.personnel = personnel;
        this.date1 = date1;
        this.date2 = date2;
    }

    
    public int getPersonnel() {
        return personnel;
    }

    public void setPersonnel(int personnel) {
        this.personnel = personnel;
    }

    
    public LocalDateTime getDate1() {
        return date1;
    }

    public void setDate1(LocalDateTime date1) {
        this.date1 = date1;
    }


    public LocalDateTime getDate2() {
        return date2;
    }

    public void setDate2(LocalDateTime date2) {
        this.date2 = date2;
    }

    @Override
    public String toString() {
        return "RecordRequestDTO{" +
                "personnel=" + personnel +
                ", date1=" + date1 +
                ", date2=" + date2 +
                '}';
    }
}
