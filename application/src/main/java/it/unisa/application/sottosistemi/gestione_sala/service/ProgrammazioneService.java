package it.unisa.application.sottosistemi.gestione_sala.service;

import it.unisa.application.model.dao.FilmDAO;
import it.unisa.application.model.dao.ProiezioneDAO;
import it.unisa.application.model.dao.SlotDAO;
import it.unisa.application.model.entity.Film;
import it.unisa.application.model.entity.Proiezione;
import it.unisa.application.model.entity.Sala;
import it.unisa.application.model.entity.Slot;

import java.time.LocalDate;
import java.util.List;

public class ProgrammazioneService {
    private final ProiezioneDAO proiezioneDAO = new ProiezioneDAO();
    private final SlotDAO slotDAO = new SlotDAO();
    private final FilmDAO filmDAO = new FilmDAO();
    

    public boolean aggiungiProiezione(int filmId, int salaId, List<Integer> slotIds, LocalDate data) {
        Film film = filmDAO.retrieveById(filmId);
        if (film == null) {
            return false;
        }
        for (Integer slotId : slotIds) {
            Proiezione proiezione = new Proiezione();
            proiezione.setFilmProiezione(film);
            proiezione.setDataProiezione(data);
            proiezione.setSalaProiezione(new Sala(salaId));
            Slot s = new Slot();
            s.setId(slotId);
            proiezione.setOrarioProiezione(s);
            boolean created = proiezioneDAO.create(proiezione);
            if (!created) {
                return false;
            }
        }
        return true;
    }

    public List<Proiezione> getProgrammazioniBySede(int sedeId) {
        return proiezioneDAO.retrieveAllBySede(sedeId);
    }
}
