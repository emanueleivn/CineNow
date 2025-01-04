package it.unisa.application.model.entity;

public class PostoProiezione {
    private Posto posto;
    private Proiezione proiezione;
    private boolean stato;

    public PostoProiezione() {
        this.stato = true;
    }

    public PostoProiezione(Posto posto, Proiezione proiezione) {
        this.stato = true;
        this.posto = posto;
        this.proiezione = proiezione;
    }

    public PostoProiezione(Sala sala, char fila, int numero, Proiezione proiezione, boolean stato) {
        posto = new Posto(sala, fila, numero);
        this.proiezione = proiezione;
        this.stato = true;
    }

    public Posto getPosto() {
        return posto;
    }

    public void setPosto(Posto posto) {
        this.posto = posto;
    }

    public Proiezione getProiezione() {
        return proiezione;
    }

    public void setProiezione(Proiezione proiezione) {
        this.proiezione = proiezione;
    }

    public boolean isStato() {
        return stato;
    }

    public void setStato(boolean stato) {
        this.stato = stato;
    }

}
