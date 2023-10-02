package com.example.application.repository;

import com.example.application.data.Cars;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarsRepository  extends CrudRepository<Cars,Integer > {
        //Counts the number of dependencies of a car in the tripcollector table.
        @Query("select count(o) from tripcollector o where o.cars = :cars")
        int countCarDependencies(@Param("cars") Cars cars);

        @Query("select c from cars c " +
                "where lower(c.kennzeichen) like lower(concat('%', :searchTerm, '%')) "+
                "or lower(c.automarke) like lower(concat('%', :searchTerm, '%'))")
        List<Cars> search(@Param("searchTerm") String searchTerm);
}
