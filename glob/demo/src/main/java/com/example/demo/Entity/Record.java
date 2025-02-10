package com.example.demo.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@Entity

@Table(name = "pointage")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_du_personnel")
    private int nombreDuPersonnel;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "in_time")
    private LocalDateTime inTime;

    @Column(name = "out_time")
    private LocalDateTime outTime;





    public int getNombreDuPersonnel() {
        return nombreDuPersonnel;
    }
    
    public void setNombreDuPersonnel(int nombreDuPersonnel) {
        this.nombreDuPersonnel = nombreDuPersonnel;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public LocalDateTime getInTime() {
        return inTime;
    }
    
    public void setInTime(LocalDateTime inTime2) {
        this.inTime = inTime2;
    }
    
    public LocalDateTime getOutTime() {
        return outTime;
    }
    
    public void setOutTime(LocalDateTime outTime) {
        this.outTime = outTime;
    }
}
