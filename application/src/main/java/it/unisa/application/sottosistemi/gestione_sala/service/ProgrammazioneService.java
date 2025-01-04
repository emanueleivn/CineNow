package it.unisa.application.sottosistemi.gestione_sala.service;

import it.unisa.application.model.dao.FilmDAO;
import it.unisa.application.model.dao.ProiezioneDAO;
import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.dao.SlotDAO;
import it.unisa.application.model.entity.Film;
import it.unisa.application.model.entity.Proiezione;
import it.unisa.application.model.entity.Sala;
import it.unisa.application.model.entity.Slot;
import java.time.LocalDate;
import java.util.List;

public class ProgrammazioneService {
    private final ProiezioneDAO proiezioneDAO = new ProiezioneDAO();
    public boolean aggiungiProiezione(int filmId, int salaId, List<Integer> slotIds, LocalDate data) {
        try {
            FilmDAO fdao = new FilmDAO();
            SedeDAO sedeDAO = new SedeDAO();
            SlotDAO slotDAO = new SlotDAO();
            ProiezioneDAO pdao = new ProiezioneDAO();
            Film film = fdao.retrieveById(filmId);
            Sala sala = sedeDAO.retrieveSalaById(salaId);
            for (int sid : slotIds) {
                Slot sl = slotDAO.retriveById(sid);
                Proiezione p = new Proiezione();
                p.setFilmProiezione(film);
                p.setSalaProiezione(sala);
                p.setOrarioProiezione(sl);
                p.setDataProiezione(data);
                if (!pdao.create(p)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
