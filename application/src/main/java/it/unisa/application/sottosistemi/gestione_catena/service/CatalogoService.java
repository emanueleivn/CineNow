package it.unisa.application.sottosistemi.gestione_catena.service;

import it.unisa.application.model.dao.FilmDAO;
import it.unisa.application.model.entity.Film;

import java.util.List;

public class CatalogoService {
    private final FilmDAO filmDAO = new FilmDAO();

    public void addFilmCatalogo(String titolo, int durata, String descrizione, String locandina, String genere, String classificazione) {
        if (titolo == null || titolo.isEmpty() || durata <= 0 || descrizione == null || descrizione.isEmpty()
                || locandina == null || locandina.isEmpty() || genere == null || genere.isEmpty()
                || classificazione == null || classificazione.isEmpty() || filmDAO.retrieveAll().stream().anyMatch(f -> f.getTitolo().equals(titolo))) {
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
