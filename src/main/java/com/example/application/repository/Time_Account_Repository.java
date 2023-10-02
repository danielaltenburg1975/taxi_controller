package com.example.application.repository;


import com.example.application.data.Time_Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Time_Account_Repository extends CrudRepository<Time_Account, Integer> {

    /**
     * Custom JPQL query to retrieve Time_Account entities based on provided parameters.
     *
     * This query joins the "time_account" and "employees" tables and filters the results based on the following criteria:
     *  - Matching the "personalID" of the "employees" table with the provided "personalId" parameter.
     *  - Optionally, filtering by the year extracted from the "datum" attribute of the "time_account" table when a "year" parameter is provided and not null or empty.
     *
     * @param personalId The employee's personal ID to filter the results by.
     * @param year       The year to filter the results by (optional). If null or empty, the year filter is ignored.
     * @return A list of Time_Account entities that match the specified criteria.
     */
    @Query("SELECT c FROM time_account c " +
            "JOIN c.employees d " +
            "WHERE d.personalID = :personalId " +
            "AND (COALESCE(:year, '') = '' OR SUBSTRING(c.datum, 4, 2) = :year)")

    List<Time_Account> searchByPersonalIdAndDate(@Param("personalId") String personalId, @Param("year") String year);
}

