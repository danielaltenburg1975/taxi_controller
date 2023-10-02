package com.example.application.repository;

import com.example.application.data.Time_Recording;
import com.example.application.data.TripCollector;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TripCollectorRepository extends CrudRepository<TripCollector,Integer > {
@Query("select c from tripcollector c " +
        "JOIN c.cars d "+
        "JOIN c.customers e "+
        "JOIN c.employees f "+

        "where lower(c.zeit) like lower(concat('%', :searchTerm, '%'))"  +
        "or lower(c.zielort) like lower(concat('%', :searchTerm, '%'))"+
        "or lower(d.kennzeichen) LIKE lower(concat('%', :searchTerm, '%'))"+
        "or lower(e.fahrgast) LIKE lower(concat('%', :searchTerm, '%'))"+
        "or lower(f.personalID) LIKE lower(concat('%', :searchTerm, '%'))"
)
List<TripCollector> search(@Param("searchTerm") String searchTerm);

}
