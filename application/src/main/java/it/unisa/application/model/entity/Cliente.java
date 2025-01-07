package it.unisa.application.model.entity;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends Utente {
    private String nome;
    private String cognome;
    private List<Prenotazione> prenotazioni;

    public Cliente() {
        super();
        this.setRuolo("cliente");
        this.prenotazioni = new ArrayList<Prenotazione>();
    }

    public Cliente(String email, String password, String nome, String cognome) {
        super(email, password, "cliente");
        this.nome = nome;
        this.cognome = cognome;
        this.prenotazioni = new ArrayList<Prenotazione>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public List<Prenotazione> storicoOrdini() {
        return prenotazioni;
    }

    public void setPrenotazioni(List<Prenotazione> prenotazioni) {
        this.prenotazioni = prenotazioni;
    }

    public void aggiungiOrdine(List<PostoProiezione> posti, Proiezione proiezione) {
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setCliente(this);
        prenotazione.setProiezione(proiezione);
        prenotazione.setPostiPrenotazione(posti);
        prenotazioni.add(prenotazione);
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", prenotazioni=" + prenotazioni +
                '}';
    }
}
