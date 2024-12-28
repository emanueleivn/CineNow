package it.unisa.application.sottosistemi.gestione_prenotazione.service;


import it.unisa.application.model.dao.PrenotazioneDAO;
import it.unisa.application.model.entity.Cliente;
import it.unisa.application.model.entity.PostoProiezione;
import it.unisa.application.model.entity.Prenotazione;
import it.unisa.application.model.entity.Proiezione;

import java.util.List;

public class PrenotazioneService {
    private final PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();

    public PrenotazioneService() {
    }

    public Prenotazione aggiungiOrdine(Cliente cliente, List<PostoProiezione> posti, Proiezione proiezione) {
        if (cliente == null || posti == null || posti.isEmpty() || proiezione == null) {
            throw new IllegalArgumentException("Cliente, posti e proiezione non possono essere null.");
        }

        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setEmailCliente(cliente.getEmail());
        //prenotazione.setIdProiezione(proiezione.getId());

        if (prenotazioneDAO.create(prenotazione)) {
            for (PostoProiezione posto : posti) {
                //posto.setIdProiezione(proiezione.getId());
                posto.setStato(false);
            }
            return prenotazione;
        } else {
            throw new RuntimeException("Errore durante la creazione della prenotazione.");
        }
    }

    public List<Prenotazione> storicoOrdini(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Il cliente non può essere null.");
        }

        return prenotazioneDAO.retrieveAllByCliente(cliente.getEmail());
    }
}