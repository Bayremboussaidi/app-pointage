package com.example.demo.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Entity.Record;

public interface RecordRepository extends JpaRepository<Record, Long> {

    boolean existsByNombreDuPersonnel(Long nombreDuPersonnel);
    void deleteByNombreDuPersonnel(Long nombreDuPersonnel);
    
    @Query("SELECT DISTINCT r FROM Record r WHERE r.nombreDuPersonnel = :personnelId AND r.inTime BETWEEN :startDate AND :endDate ORDER BY r.inTime ASC")
    List<Record> findByPersonnelAndDateRange(@Param("personnelId") int personnelId,
                                            @Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);




    @Query("SELECT DISTINCT r FROM Record r WHERE r.inTime BETWEEN :startDate AND :endDate ORDER BY r.inTime ASC")
    List<Record> findAllBetweenDates(@Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);
                                            
                                            

}
