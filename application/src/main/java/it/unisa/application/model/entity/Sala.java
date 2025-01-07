package it.unisa.application.model.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Sala {
    private int id;
    private int numeroSala;
    private int capienza;
    private List<Slot> slotList;
    private List<Proiezione> proiezioni;
    private List<Posto> posti;
    private Sede sede;

    public Sala() {
    }

    public Sala(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumeroSala() {
        return numeroSala;
    }

    public void setNumeroSala(int numeroSala) {
        this.numeroSala = numeroSala;
    }

    public int getCapienza() {
        return capienza;
    }

    public void setCapienza(int capienza) {
        this.capienza = capienza;
    }

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public List<Slot> slotList() {
        return slotList;
    }

    public void setSlotList(List<Slot> slotList) {
        this.slotList = slotList;
    }

    public List<Proiezione> getProiezioni() {
        return proiezioni;
    }

    public void setProiezioni(List<Proiezione> proiezioni) {
        this.proiezioni = proiezioni;
    }

    public List<Posto> getPosti() {
        return posti;
    }

    public void setPosti(List<Posto> posti) {
        this.posti = posti;
    }

    public List<Slot> getSlotList() {
        return slotList;
    }

    public Proiezione aggiungiProiezione(Slot slot, LocalDate data, Film film) {
        Proiezione proiezione = new Proiezione();
        proiezione.setDataProiezione(data);
        proiezione.setFilmProiezione(film);
        proiezione.setOrarioProiezione(slot);
        proiezione.setPostiProiezione(creaListaPosti(proiezione));
        return proiezione;
    }

    private List<PostoProiezione> creaListaPosti(Proiezione proiezione) {
        ArrayList<PostoProiezione> posti = new ArrayList<>();
        for (Posto p : this.posti) {
            posti.add(new PostoProiezione(p, proiezione));
        }
        return posti;
    }


    @Override
    public String toString() {
        return "Sala{" +
                "id=" + id +
                ", numeroSala=" + numeroSala +
                ", capienza=" + capienza +
                ", slotList=" + slotList +
                ", proiezioni=" + proiezioni +
                ", posti=" + posti +
                '}';
    }
}
