package it.unisa.application.model.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Proiezione {
    private int id;
    private Film filmProiezione;
    private Sala salaProiezione;
    private LocalDate dataProiezione;
    private List<PostoProiezione> postiProiezione;
    private Slot orarioProiezione;
    private List<Slot> slotsProiezione;

    public Proiezione() {
        postiProiezione = new ArrayList<PostoProiezione>();
        slotsProiezione = new ArrayList<>();
    }

    public Proiezione(int id, Sala salaProiezione, Film filmProiezione, LocalDate dataProiezione, List<PostoProiezione> postiProiezione, Slot orarioProiezione) {
        this.id = id;
        this.salaProiezione = salaProiezione;
        this.filmProiezione = filmProiezione;
        this.dataProiezione = dataProiezione;
        this.postiProiezione = postiProiezione;
        this.orarioProiezione = orarioProiezione;
    }

    public Proiezione(int i) {
        this.id = i;
        this.postiProiezione = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Film getFilmProiezione() {
        return filmProiezione;
    }

    public void setFilmProiezione(Film filmProiezione) {
        this.filmProiezione = filmProiezione;
    }

    public Sala getSalaProiezione() {
        return salaProiezione;
    }

    public void setSalaProiezione(Sala salaProiezione) {
        this.salaProiezione = salaProiezione;
    }

    public LocalDate getDataProiezione() {
        return dataProiezione;
    }

    public void setDataProiezione(LocalDate dataProiezione) {
        this.dataProiezione = dataProiezione;
    }

    public List<PostoProiezione> getPostiProiezione() {
        return postiProiezione;
    }

    public void setPostiProiezione(List<PostoProiezione> postiProiezione) {
        this.postiProiezione = postiProiezione;
    }

    public Slot getOrarioProiezione() {
        return orarioProiezione;
    }

    public void setOrarioProiezione(Slot orarioProiezione) {
        this.orarioProiezione = orarioProiezione;
    }

    public void occupaPosto(PostoProiezione posto) {
        postiProiezione.stream()
                .filter(postoProiezione -> postoProiezione.equals(posto))
                .findFirst()
                .ifPresent(postoProiezione -> postoProiezione.setStato(false));
    }

    public String getMinOraInizioFormatted() {
        Optional<LocalTime> minTime = slotsProiezione.stream()
                .map(slot -> slot.getOraInizio().toLocalTime())
                .min(LocalTime::compareTo);
        return minTime.map(time -> time.toString().substring(0, 5)).orElse("N/A");
    }

    public List<Slot> getSlotsProiezione() {
        return slotsProiezione;
    }

    public void setSlotsProiezione(List<Slot> slotsProiezione) {
        this.slotsProiezione = slotsProiezione;
    }

    public void aggiungiSlot(Slot slot) {
        this.slotsProiezione.add(slot);
    }

    @Override
    public String toString() {
        return "Proiezione{" +
                "id=" + id +
                ", filmProiezione=" + filmProiezione +
                ", salaProiezione=" + salaProiezione +
                ", dataProiezione=" + dataProiezione +
                ", postiProiezione=" + (postiProiezione != null ? postiProiezione.size() : 0) +
                ", slotsProiezione=" + (slotsProiezione != null ? slotsProiezione.size() : 0) +
                '}';
    }
}
