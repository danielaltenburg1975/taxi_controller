package com.example.application.data;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;


@Entity(name = "time_account")
public class Time_Account extends AbstractEntity{
    @ManyToOne
    private Employees employees;
    private String datum;
    private String tagesZeit;





    public String getTagesZeit() {
        return tagesZeit;
    }

    public void setTagesZeit(String tagesZeit) {
        this.tagesZeit = tagesZeit;
    }


    public Employees getEmployees() {
        return employees;
    }

    public void setEmployees(Employees employees) {
        this.employees = employees;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }


}
