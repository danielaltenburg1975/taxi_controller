package com.example.application.repository;

import com.example.application.data.Employees;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeesRepository extends CrudRepository<Employees,Integer> {

    //Counts the number of dependencies of a customer in the tripcollector table.
    @Query("select count(o) from tripcollector o where o.employees = :employees")
    int countEmployeesToTripCollectorDependencies(@Param("employees") Employees employees);

    //Counts the number of dependencies of a customer in the new_trip table.
    @Query("select count(o) from new_trip o where o.employees = :employees")
    int countEmployeesToNew_TripDependencies(@Param("employees") Employees employees);

    @Query("select c from Employees c " +
            "where lower(c.name) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.personalID) LIKE lower(concat('%', :searchTerm, '%'))")

    List<Employees> search(@Param("searchTerm") String searchTerm);

}
