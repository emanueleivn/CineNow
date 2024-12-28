package it.unisa.application.model.entity;

public class Posto {
    private int idSala;
    private char fila;
    private int numero;

    public Posto() {}

    public Posto(int idSala, char fila, int numero) {
        this.idSala = idSala;
        this.fila = fila;
        this.numero = numero;
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
}
