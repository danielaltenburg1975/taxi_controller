package com.example.application.data;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity(name = "customers")
public class Customers extends AbstractEntity{
    @NotBlank
    private String fahrgast;
    @NotBlank
    private String adresse;
    @NotBlank
    private String telefon;
    @Email
    private String email;
    private String kostentraeger;

    public Customers(){};


    public Customers(String fahrgast) {
        this.fahrgast = fahrgast;
    }

    public String getFahrgast() {
        return fahrgast;
    }

    public void setFahrgast(String fahrgast) {
        this.fahrgast = fahrgast;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKostentraeger() {
        return kostentraeger;
    }

    public void setKostentraeger(String kostentraeger) {
        this.kostentraeger = kostentraeger;
    }
}
