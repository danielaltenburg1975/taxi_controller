package com.example.application.data;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;

@Entity(name = "memories")
public class Memories extends AbstractEntity {

    @NotBlank
    private String datum;

    @NotBlank
    private String erinnerung;

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getErinnerung() {
        return erinnerung;
    }

    public void setErinnerung(String erinnerung) {
        this.erinnerung = erinnerung;
    }
}
