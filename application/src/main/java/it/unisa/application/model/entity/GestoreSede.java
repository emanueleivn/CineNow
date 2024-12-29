package it.unisa.application.model.entity;

public class GestoreSede extends Utente {
    private Sede sede;

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public GestoreSede() {
    }

    public GestoreSede(String email, String password, Sede sede) {
        super(email, password, "gestore_sede");
        this.sede = sede;
    }
}
