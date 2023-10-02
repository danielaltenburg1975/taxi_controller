package com.example.application.repository;

import com.example.application.data.Employees;
import com.example.application.data.Time_Recording;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Time_RecordingRepository extends CrudRepository<Time_Recording,Integer > {
    @Query("select c from Time_Booking c " +
            "JOIN c.employees d "+
            "where lower(c.zeitbuchung) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.gebucht) like lower(concat('%', :searchTerm, '%'))"+
            "or lower(d.personalID) like lower(concat('%', :searchTerm, '%'))")
    List<Time_Recording> search(@Param("searchTerm") String searchTerm);
}
