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
            FilmDAO filmDAO = new FilmDAO();
            SedeDAO sedeDAO = new SedeDAO();
            SlotDAO slotDAO = new SlotDAO();
            Film film = filmDAO.retrieveById(filmId);
            Sala sala = sedeDAO.retrieveSalaById(salaId);
            if (film == null) {
                throw new RuntimeException("Film non trovato.");
            }
            if (sala == null) {
                throw new RuntimeException("Sala non trovata.");
            }
            int durata = film.getDurata();
            int slotNecessari = (int) Math.ceil(durata / 30.0);
            List<Slot> slotsDisponibili = slotDAO.retrieveAllSlots();
            List<Slot> slotsSelezionati = slotsDisponibili.stream()
                    .filter(slot -> slotIds.contains(slot.getId()))
                    .toList();
            if (slotsSelezionati.size() < slotNecessari) {
                slotNecessari = Math.min(slotsSelezionati.size(), slotNecessari);
            }
            if (slotsSelezionati.isEmpty()) {
                throw new RuntimeException("Nessuno slot valido selezionato.");
            }
            Proiezione proiezione = new Proiezione();
            proiezione.setFilmProiezione(film);
            proiezione.setSalaProiezione(sala);
            proiezione.setDataProiezione(data);
            proiezione.setSlotsProiezione(slotsSelezionati.subList(0, slotNecessari));
            return proiezioneDAO.create(proiezione);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
