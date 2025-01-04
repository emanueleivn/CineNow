package it.unisa.application.sottosistemi.gestione_sede.service;

import it.unisa.application.model.dao.ProiezioneDAO;
import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.entity.Film;
import it.unisa.application.model.entity.Proiezione;
import it.unisa.application.model.entity.Sede;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProgrammazioneSedeService {
    ProiezioneDAO proiezioneDAO = new ProiezioneDAO();

    public List<Proiezione> getProgrammazioniBySede(int sedeId) {
        return proiezioneDAO.retrieveAllBySede(sedeId);
    }
    public List<Proiezione> getProgrammazioneFilm(int filmId, int sedeId){
        return proiezioneDAO.retriveByFilm(new Film(filmId),new Sede(sedeId));
    }
    public List<Film> getCatalogoSede(Sede sede){
        SedeDAO sedeDAO = new SedeDAO();
        List<Film> catalogo;
        try {
            catalogo =  sedeDAO.retrieveFilm(sede.getId());
        } catch (SQLException e) {
            return null;
        }
        return catalogo;
    }
}
