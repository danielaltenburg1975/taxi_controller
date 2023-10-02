package com.example.application.data;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;

@Entity(name = "cars")
public class Cars extends AbstractEntity {

    @NotBlank
    private String automarke;

    @NotBlank
    private String kennzeichen;

    @NotBlank
    private String taxometer;

    @NotBlank
    private String hauptuntersuchung;

    @NotBlank
    private String reifengroessen;

    @NotBlank
    private String tankkarte;

    @NotBlank
    private String sitzplatz;
    public Cars(){};

    public Cars(String kennzeichen) {
        this.kennzeichen = kennzeichen;
    }

    public String getFahrgast() {
        return kennzeichen;
    }

    public String getAutomarke() {
        return automarke;
    }

    public void setAutomarke(String automarke) {
        this.automarke = automarke;
    }

    public String getKennzeichen() {
        return kennzeichen;
    }

    public void setKennzeichen(String kennzeichen) {
        this.kennzeichen = kennzeichen;
    }

    public String getSitzplatz() {
        return sitzplatz;
    }

    public void setSitzplatz(String sitzplatz) {
        this.sitzplatz = sitzplatz;
    }

    public String getTaxometer() {
        return taxometer;
    }

    public void setTaxometer(String taxometer) {
        this.taxometer = taxometer;
    }

    public String getHauptuntersuchung() {
        return hauptuntersuchung;
    }

    public void setHauptuntersuchung(String hauptuntersuchung) {
        this.hauptuntersuchung = hauptuntersuchung;
    }

    public String getReifengroessen() {
        return reifengroessen;
    }

    public void setReifengroessen(String reifengroessen) {
        this.reifengroessen = reifengroessen;
    }

    public String getTankkarte() {
        return tankkarte;
    }

    public void setTankkarte(String tankkarte) {
        this.tankkarte = tankkarte;
    }
}
