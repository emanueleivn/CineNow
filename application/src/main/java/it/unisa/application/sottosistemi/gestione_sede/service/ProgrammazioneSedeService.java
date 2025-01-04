package it.unisa.application.sottosistemi.gestione_sede.service;

import it.unisa.application.model.dao.ProiezioneDAO;
import it.unisa.application.model.entity.Proiezione;

import java.util.List;

public class ProgrammazioneSedeService {
    ProiezioneDAO proiezioneDAO = new ProiezioneDAO();
    public List<Proiezione> getProgrammazioniBySede(int sedeId) {
        return proiezioneDAO.retrieveAllBySede(sedeId);
    }
    public void getProgrammazioneFilm(int sedeId){}
    public void getCatalogoSede(int sedeId){}
}
