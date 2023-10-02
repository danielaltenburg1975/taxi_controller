package com.example.application.repository;

import com.example.application.data.Customers;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomersRepository extends CrudRepository<Customers,Integer > {

    //Counts the number of dependencies of a customer in the tripcollector table.
    @Query("select count(o) from tripcollector o where o.customers = :customer")
    int countCustomersToTripCollectorDependencies(@Param("customer") Customers customers);

    //Counts the number of dependencies of a customer in the new_trip table.
    @Query("select count(o) from new_trip o where o.customers = :customer")
    int countCustomersToNew_TripDependencies(@Param("customer") Customers customers);


    @Query("select c from customers c " +
            "where lower(c.fahrgast) like lower(concat('%', :searchTerm, '%'))"

    )
    List<Customers> search(@Param("searchTerm") String searchTerm);

}