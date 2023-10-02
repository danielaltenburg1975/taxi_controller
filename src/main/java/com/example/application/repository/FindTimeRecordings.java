package com.example.application.repository;


import com.example.application.data.Time_Recording;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FindTimeRecordings extends JpaRepository<Time_Recording, Long> {


    @Query("select c from Time_Booking c "+
            "where c.gebucht = :param1 "+
            "and c.personalnummer = :param2 " )



    List<Time_Recording> search(@Param("param1") String parameter1,
                                @Param("param2") String parameter2);




}
