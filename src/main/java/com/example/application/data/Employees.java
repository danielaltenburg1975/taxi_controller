package com.example.application.data;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity(name = "Employees")
public class Employees extends AbstractEntity   {

    @NotBlank
    private String personalID;
    @NotBlank
    private String name;
    @NotBlank
    private String adresse;
    @NotBlank
    private String telefon;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String geburtstag;
    private String ein_austritt;
    private String urlaubsanspruch;
    private String pensum;
    private String krankenversicherung;
    private String sozialversicherung;

    public Employees(){};

    public Employees(String personalID ) {
        this.personalID = personalID;
    }


    public String getPersonalID() {
        return personalID;
    }

    public void setPersonalID(String personalID) {
        this.personalID = personalID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEin_austritt() {
        return ein_austritt;
    }

    public void setEin_austritt(String ein_austritt) {
        this.ein_austritt = ein_austritt;
    }

    public String getUrlaubsanspruch() {
        return urlaubsanspruch;
    }

    public void setUrlaubsanspruch(String urlaubsanspruch) {
        this.urlaubsanspruch = urlaubsanspruch;
    }

    public String getPensum() {
        return pensum;
    }

    public void setPensum(String pensum) {
        this.pensum = pensum;
    }

    public String getKrankenversicherung() {
        return krankenversicherung;
    }

    public void setKrankenversicherung(String krankenversicherung) {
        this.krankenversicherung = krankenversicherung;
    }

    public String getSozialversicherung() {
        return sozialversicherung;
    }

    public void setSozialversicherung(String sozialversicherung) {
        this.sozialversicherung = sozialversicherung;
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

    public String getGeburtstag() {
        return geburtstag;
    }

    public void setGeburtstag(String geburtstag) {
        this.geburtstag = geburtstag;
    }


}
