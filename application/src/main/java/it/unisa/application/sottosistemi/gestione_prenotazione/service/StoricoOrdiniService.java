package it.unisa.application.sottosistemi.gestione_prenotazione.service;

import it.unisa.application.model.dao.PrenotazioneDAO;
import it.unisa.application.model.entity.Cliente;
import it.unisa.application.model.entity.Prenotazione;

import java.util.List;

public class StoricoOrdiniService {
    private PrenotazioneDAO prenotazioneDAO;

    public StoricoOrdiniService() {
        this.prenotazioneDAO = new PrenotazioneDAO();
    }

    public StoricoOrdiniService(PrenotazioneDAO prenotazioneDAOMock) {
        prenotazioneDAO = prenotazioneDAOMock;
    }

    public List<Prenotazione> storicoOrdini(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Il cliente non può essere null.");
        }
        return prenotazioneDAO.retrieveAllByCliente(cliente);
    }
}
