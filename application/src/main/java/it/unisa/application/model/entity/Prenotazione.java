package it.unisa.application.model.entity;

import java.util.List;

public class Prenotazione {
    private int id;
    private Proiezione proiezione;
    private List<PostoProiezione> postiProiezione;
    private Cliente cliente;

    public Prenotazione() {}

    public Prenotazione(int id, Cliente cliente, Proiezione proiezione) {
        this.id = id;
        this.cliente = cliente;
        this.proiezione = proiezione;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Proiezione getProiezione() {
        return proiezione;
    }

    public void setProiezione(Proiezione proiezione) {
        this.proiezione = proiezione;
    }

    public List<PostoProiezione> getPostiProiezione() {
        return postiProiezione;
    }

    public void setPostiProiezione(List<PostoProiezione> postiProiezione) {
        this.postiProiezione = postiProiezione;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return "Prenotazione{" +
                "id=" + id +
                ", proiezione=" + proiezione +
                ", postiPrenotazione=" + postiProiezione +
                ", cliente=" + cliente +
                '}';
    }
}
