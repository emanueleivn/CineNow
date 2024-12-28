package it.unisa.application.model.entity;

public class Occupa {
    private int idSala;
    private char fila;
    private int numero;
    private int idProiezione;
    private int idPrenotazione;

    public Occupa() {}

    public Occupa(int idSala, char fila, int numero, int idProiezione, int idPrenotazione) {
        this.idSala = idSala;
        this.fila = fila;
        this.numero = numero;
        this.idProiezione = idProiezione;
        this.idPrenotazione = idPrenotazione;
    }

    public int getIdSala() {
        return idSala;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }

    public char getFila() {
        return fila;
    }

    public void setFila(char fila) {
        this.fila = fila;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getIdProiezione() {
        return idProiezione;
    }

    public void setIdProiezione(int idProiezione) {
        this.idProiezione = idProiezione;
    }

    public int getIdPrenotazione() {
        return idPrenotazione;
    }

    public void setIdPrenotazione(int idPrenotazione) {
        this.idPrenotazione = idPrenotazione;
    }
}
