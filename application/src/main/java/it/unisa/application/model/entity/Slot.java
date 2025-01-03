package it.unisa.application.model.entity;

import java.sql.Time;

public class Slot {
    private int id;
    private Time oraInizio;

    public Slot() {
    }

    public Slot(int id) {
        this.id = id;
    }

    public Time getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(Time oraInizio) {
        this.oraInizio = oraInizio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Slot{" +
                "id=" + id +
                ", oraInizio=" + oraInizio +
                '}';
    }
}
