package com.example.application.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity(name="tripcollector")
public class TripCollector extends AbstractEntity {
    @NotBlank
    private String zeit;
    @NotBlank
    private String abholort;
    @NotBlank
    private String zielort;
    private String anmerkung;
    private String personen;
    private String auto;
    private String fahrer;
    @NotBlank
    private String bezahlung;


    @NotNull
    @ManyToOne
    private Customers customers;

    @NotNull
    @ManyToOne
    private Cars cars;

    @NotNull
    @ManyToOne
    private Employees employees;

    public String getZeit() {
        return zeit;
    }

    public void setZeit(String zeit) {
        this.zeit = zeit;
    }

    public String getAbholort() {
        return abholort;
    }

    public void setAbholort(String abholort) {
        this.abholort = abholort;
    }

    public String getZielort() {
        return zielort;
    }

    public void setZielort(String zielort) {
        this.zielort = zielort;
    }



    public String getAnmerkung() {
        return anmerkung;
    }

    public void setAnmerkung(String anmerkung) {
        this.anmerkung = anmerkung;
    }

    public String getPersonen() {
        return personen;
    }

    public void setPersonen(String personen) {
        this.personen = personen;
    }

    public String getAuto() {
        return auto;
    }

    public void setAuto(String auto) {
        this.auto = auto;
    }

    public String getFahrer() {
        return fahrer;
    }

    public void setFahrer(String fahrer) {
        this.fahrer = fahrer;
    }

    public String getBezahlung() {
        return bezahlung;
    }

    public void setBezahlung(String bezahlung) {
        this.bezahlung = bezahlung;
    }

    public Customers getCustomers() {
        return customers;
    }

    public void setCustomers(Customers customers) {
        this.customers = customers;
    }

    public Cars getCars() {
        return cars;
    }

    public void setCars(Cars cars) {
        this.cars = cars;
    }

    public Employees getEmployees() {
        return employees;
    }

    public void setEmployees(Employees employees) {
        this.employees = employees;
    }
}
