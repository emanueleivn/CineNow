package it.unisa.application.sottosistemi.gestione_catena.service;

import it.unisa.application.model.dao.FilmDAO;
import it.unisa.application.model.entity.Film;
import it.unisa.application.utilities.CampoValidator;
import it.unisa.application.utilities.ValidateStrategyManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatalogoService {
    private final FilmDAO filmDAO = new FilmDAO();
    private ValidateStrategyManager validationManager= new ValidateStrategyManager();

    public void addFilmCatalogo(String titolo, int durata, String descrizione, byte[] locandina, String genere, String classificazione) {
        if (titolo == null || titolo.isEmpty() || durata <= 0 || descrizione == null || descrizione.isEmpty()
                || locandina == null || locandina.length == 0 || genere == null || genere.isEmpty()
                || classificazione == null || classificazione.isEmpty()) {
            throw new IllegalArgumentException("Parametri non validi per l'aggiunta del film.");
        }
        validationManager.addValidator("titolo", new CampoValidator());
        validationManager.addValidator("descrizione", new CampoValidator());
        Map<String, String> inputs = new HashMap<>();
        inputs.put("titolo", titolo);
        inputs.put("descrizione", descrizione);
        if (!validationManager.validate(inputs)) {
            throw new IllegalArgumentException("Parametri non validi per l'aggiunta del film.");
        }
        Film film = new Film(0, titolo, genere, classificazione, durata, locandina, descrizione, false);
        if (!filmDAO.create(film)) {
            throw new RuntimeException("Errore durante l'inserimento del film nel catalogo.");
        }
    }

    public List<Film> getCatalogo() {
        return filmDAO.retrieveAll();
    }
}
