package com.example.application.repository;

import com.example.application.data.New_Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FindNewTripRepository extends JpaRepository<New_Trip, Long> {

    @Query("SELECT COUNT(nt) FROM new_trip nt WHERE nt.gebucht = :condition")
    Long countByCondition(@Param("condition") String condition);


}
