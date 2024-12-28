package it.unisa.application.sottosistemi.gestione_catena.service;

import it.unisa.application.model.dao.FilmDAO;
import it.unisa.application.model.entity.Film;

import java.util.List;

public class CatalogoService {
    private final FilmDAO filmDAO = new FilmDAO();

    public boolean addFilmCatalogo(Film film) {
        if (film == null || filmDAO.getAllFilms().contains(film)) {
            return false;
        }
        return filmDAO.addFilm(film);
    }

    public List<Film> getCatalogo() {
        return filmDAO.getAllFilms();
    }
}
