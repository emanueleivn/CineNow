package it.unisa.application.model.entity;

public class PostoProiezione {
    private int idSala;
    private char fila;
    private int numero;
    private int idProiezione;
    private boolean stato;

    public PostoProiezione() {}

    public PostoProiezione(int idSala, char fila, int numero, int idProiezione, boolean stato) {
        this.idSala = idSala;
        this.fila = fila;
        this.numero = numero;
        this.idProiezione = idProiezione;
        this.stato = stato;
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

    public boolean isStato() {
        return stato;
    }

    public void setStato(boolean stato) {
        this.stato = stato;
    }
}
