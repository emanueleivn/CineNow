package it.unisa.application.model.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Proiezione {
    private int id;
    private Film filmProiezione;
    private Sala salaProiezione;
    private LocalDate dataProiezione;
    private List<PostoProiezione> postiProiezione;
    private Slot orarioProiezione;

    public Proiezione() {
        postiProiezione = new ArrayList<PostoProiezione>();
    }

    public Proiezione(int id, Sala salaProiezione, Film filmProiezione, LocalDate dataProiezione, List<PostoProiezione> postiProiezione, Slot orarioProiezione) {
        this.id = id;
        this.salaProiezione = salaProiezione;
        this.filmProiezione = filmProiezione;
        this.dataProiezione = dataProiezione;
        this.postiProiezione = postiProiezione;
        this.orarioProiezione = orarioProiezione;
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

    @Override
    public String toString() {
        return "Proiezione{" +
                "id=" + id +
                ", filmProiezione=" + filmProiezione +  // Assumendo che Film non abbia riferimenti ciclici
                ", salaProiezione=" + salaProiezione +
                ", dataProiezione=" + dataProiezione +
                ", postiProiezione=" + (postiProiezione != null ? postiProiezione.size() : 0) +  // Stampiamo solo il numero di posti
                ", orarioProiezione=" + orarioProiezione +
                '}';
    }

}
