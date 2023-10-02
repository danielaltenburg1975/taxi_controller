package com.example.application.repository;

import com.example.application.data.New_Trip;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface New_TripRepository extends CrudRepository<New_Trip,Integer> {
    @Query("select c from new_trip c " +

            "JOIN c.customers e "+
            "JOIN c.employees f "+

            "where lower(c.zeit) like lower(concat('%', :searchTerm, '%'))"  +
            "or lower(c.zielort) like lower(concat('%', :searchTerm, '%'))"+
            "or lower(c.gebucht) like lower(concat('%', :searchTerm, '%'))"+
            "or lower(e.fahrgast) LIKE lower(concat('%', :searchTerm, '%'))"+
            "or lower(f.personalID) LIKE lower(concat('%', :searchTerm, '%'))"

    )

    List<New_Trip> search(@Param("searchTerm") String searchTerm);
}
