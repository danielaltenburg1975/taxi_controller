package com.example.application.repository;

import com.example.application.data.Memories;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemoriesRepository extends CrudRepository<Memories,Integer > {


        @Query("select c from memories c " +
                "WHERE c.datum = :datum ")
        List<Memories> search(@Param("datum") String searchTerm);
}
