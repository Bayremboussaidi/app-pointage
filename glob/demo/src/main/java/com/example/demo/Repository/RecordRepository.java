package com.example.demo.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Entity.Record;

public interface RecordRepository extends JpaRepository<Record, Long> {

    // ✅ Find by 'idd' (Since it's not a primary key)
    Optional<Record> findByIdd(String idd);
    
    // ✅ Check if an 'idd' exists
    boolean existsByIdd(String idd);
    
    // ✅ Delete by 'idd'
    void deleteByIdd(String idd);

    @Query("SELECT DISTINCT r FROM Record r WHERE r.idd = :id AND r.inTime BETWEEN :startDate AND :endDate ORDER BY r.inTime ASC")
    List<Record> findByIdAndDateRange(@Param("id") String id,
                                    @Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate);

    @Query("SELECT DISTINCT r FROM Record r WHERE r.inTime BETWEEN :startDate AND :endDate ORDER BY r.inTime ASC")
    List<Record> findAllBetweenDates(@Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate);
}
