package it.unisa.application.sottosistemi.gestione_prenotazione.service;

import it.unisa.application.model.dao.PostoProiezioneDAO;
import it.unisa.application.model.dao.PrenotazioneDAO;
import it.unisa.application.model.entity.Cliente;
import it.unisa.application.model.entity.PostoProiezione;
import it.unisa.application.model.entity.Prenotazione;
import it.unisa.application.model.entity.Proiezione;

import java.util.List;

public class PrenotazioneService {
    private PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();
    private PostoProiezioneDAO postoProiezioneDAO = new PostoProiezioneDAO();

    public PrenotazioneService() {
    }

    public PrenotazioneService(PrenotazioneDAO prenotazioneDAOMock, PostoProiezioneDAO postoProiezioneDAOMock) {
        prenotazioneDAO = prenotazioneDAOMock;
        postoProiezioneDAO = postoProiezioneDAOMock;
    }

    public Prenotazione aggiungiOrdine(Cliente cliente, List<PostoProiezione> posti, Proiezione proiezione) {
        if (cliente == null || posti == null || posti.isEmpty() || proiezione == null) {
            throw new IllegalArgumentException("Cliente, posti e proiezione non possono essere null.");
        }
        for (PostoProiezione postoProiezione : posti) {
            if (!postoProiezione.isStato()) {
                throw new IllegalArgumentException("Posti occupati");
            }
        }
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setCliente(cliente);
        prenotazione.setProiezione(proiezione);
        prenotazione.setPostiPrenotazione(posti);

        if (!prenotazioneDAO.create(prenotazione)) {
            throw new RuntimeException("Errore durante la creazione della prenotazione.");
        }

        for (PostoProiezione postoProiezione : posti) {
            if (!postoProiezioneDAO.occupaPosto(postoProiezione, prenotazione.getId())) {
                throw new RuntimeException("Errore durante l'occupazione del posto.");
            }
        }

        return prenotazione;
    }

    public List<PostoProiezione> ottieniPostiProiezione(Proiezione proiezione){
        return postoProiezioneDAO.retrieveAllByProiezione(proiezione);
    }
}
