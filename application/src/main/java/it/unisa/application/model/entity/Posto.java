package it.unisa.application.model.entity;

public class Posto {
    private Sala sala;
    private char fila;
    private int numero;

    public Posto() {}

    public Posto(Sala sala, char fila, int numero) {
        this.sala = sala;
        this.fila = fila;
        this.numero = numero;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
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

    @Override
    public String toString() {
        return "Posto{" +
                "sala=" + sala +
                ", fila=" + fila +
                ", numero=" + numero +
                '}';
    }
}
