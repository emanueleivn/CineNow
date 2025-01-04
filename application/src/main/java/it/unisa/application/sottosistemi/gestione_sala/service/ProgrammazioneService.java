package it.unisa.application.sottosistemi.gestione_sala.service;

import it.unisa.application.model.dao.FilmDAO;
import it.unisa.application.model.dao.ProiezioneDAO;
import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.dao.SlotDAO;
import it.unisa.application.model.entity.Film;
import it.unisa.application.model.entity.Proiezione;
import it.unisa.application.model.entity.Sala;

import java.time.LocalDate;
import java.util.List;

public class ProgrammazioneService {
    private final FilmDAO filmDAO = new FilmDAO();
    private final SedeDAO sedeDAO = new SedeDAO();
    private final ProiezioneDAO proiezioneDAO = new ProiezioneDAO();
    private final SlotDAO proiezioneSlotDAO = new SlotDAO();

    public List<Film> getCatalogoFilm() {
        return filmDAO.retrieveAll();
    }

    public List<Sala> getSaleBySede(int sedeId) {
        return sedeDAO.retrieveSaleBySede(sedeId);
    }

    public boolean aggiungiProiezione(int filmId, int salaId, List<Integer> slotIds, LocalDate data) {
        try {
            // Creazione della proiezione
            Proiezione proiezione = new Proiezione();
            proiezione.setFilmProiezione(new Film(filmId));
            proiezione.setSalaProiezione(new Sala(salaId));
            proiezione.setDataProiezione(data);

            System.out.println("Creazione proiezione: filmId=" + filmId + ", salaId=" + salaId + ", data=" + data);

            if (!proiezioneDAO.create(proiezione)) {
                System.err.println("Errore durante la creazione della proiezione.");
                return false;
            }

            // Associazione degli slot alla proiezione
            for (int slotId : slotIds) {
                System.out.println("Associazione slotId=" + slotId + " alla proiezioneId=" + proiezione.getId());
                if (!proiezioneSlotDAO.associaSlot(proiezione.getId(), slotId)) {
                    System.err.println("Errore durante l'associazione dello slot: slotId=" + slotId);
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
