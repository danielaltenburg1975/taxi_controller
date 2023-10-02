package com.example.application.data;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity(name = "new_trip")
public class New_Trip extends AbstractEntity{
    private String zeit;
    private String fahrgast;
    private String abholort;
    private String zielort;
    private String anmerkung;
    private String fahrer;
    private String gebucht;
    @NotNull
    @ManyToOne
    private Customers customers;
    @NotNull
    @ManyToOne
    private Employees employees;

    public String getZeit() {
        return zeit;
    }

    public void setZeit(String zeit) {
        this.zeit = zeit;
    }

    public String getFahrgast() {
        return fahrgast;
    }

    public void setFahrgast(String fahrgast) {
        this.fahrgast = fahrgast;
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

    public String getFahrer() {
        return fahrer;
    }

    public void setFahrer(String fahrer) {
        this.fahrer = fahrer;
    }

    public String getGebucht() {
        return gebucht;
    }

    public void setGebucht(String gebucht) {
        this.gebucht = gebucht;
    }
    public Customers getCustomers() {
        return customers;
    }

    public void setCustomers(Customers customers) {
        this.customers = customers;
    }

    public Employees getEmployees() {
        return employees;
    }

    public void setEmployees(Employees employees) {
        this.employees = employees;
    }
}
