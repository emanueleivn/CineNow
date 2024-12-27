package it.unisa.application.model.entity;

public class Prenotazione {
    private int id;
    private String emailCliente;
    private int idProiezione;


    public Prenotazione(int id, String emailCliente, int idProiezione) {
        this.id = id;
        this.emailCliente = emailCliente;
        this.idProiezione = idProiezione;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public int getIdProiezione() {
        return idProiezione;
    }

    public void setIdProiezione(int idProiezione) {
        this.idProiezione = idProiezione;
    }
}
