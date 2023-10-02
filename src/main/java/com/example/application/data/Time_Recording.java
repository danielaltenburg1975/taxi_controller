package com.example.application.data;


import jakarta.persistence.Entity;

import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

@Entity(name = "Time_Booking")
public class Time_Recording extends AbstractEntity {


    @NotBlank
    private String zeitbuchung;

    private String personalnummer;

    @NotBlank
    private String gebucht;
    @ManyToOne
    private Employees employees;

    public String getGebucht() {
        return gebucht;
    }

    public void setGebucht(String gebucht) {
        this.gebucht = gebucht;
    }

    public String getZeitbuchung() {
        return zeitbuchung;
    }

    public void setZeitbuchung(String zeitbuchung) {
        this.zeitbuchung = zeitbuchung;
    }

    public String getPersonalnummer() {
        return personalnummer;
    }

    public void setPersonalnummer(String personalnummer) {
        this.personalnummer = personalnummer;
    }
    public Employees getEmployees() {
        return employees;
    }

    public void setEmployees(Employees employees) {
        this.employees = employees;
    }


}
