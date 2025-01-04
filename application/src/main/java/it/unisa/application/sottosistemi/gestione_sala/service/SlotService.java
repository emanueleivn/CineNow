package it.unisa.application.sottosistemi.gestione_sala.service;

import it.unisa.application.model.dao.FilmDAO;
import it.unisa.application.model.dao.ProiezioneDAO;
import it.unisa.application.model.dao.SlotDAO;
import it.unisa.application.model.entity.Film;
import it.unisa.application.model.entity.Proiezione;
import it.unisa.application.model.entity.Slot;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SlotService {
    private final SlotDAO slotDAO = new SlotDAO();
    private final ProiezioneDAO proiezioneDAO = new ProiezioneDAO();
    private final FilmDAO filmDAO = new FilmDAO();

    public Map<String, Object> getSlotDisponibili(int filmId, int salaId, LocalDate dataInizio, LocalDate dataFine) throws Exception {
        Film film = filmDAO.retrieveById(filmId);
        if (film == null) {
            throw new RuntimeException("Film non esistente.");
        }
        int durata = film.getDurata();
        List<Map<String, Object>> calendar = new ArrayList<>();
        LocalDate current = dataInizio;
        while (!current.isAfter(dataFine)) {
            List<Slot> allSlots = slotDAO.retrieveAllSlots();
            List<Map<String, Object>> slotList = new ArrayList<>();
            for (Slot s : allSlots) {
                Proiezione existing = proiezioneDAO.retrieveProiezioneBySalaSlotAndData(salaId, s.getId(), current);
                if (existing != null) {
                    Film fOccupato = filmDAO.retrieveById(existing.getFilmProiezione().getId());
                    slotList.add(Map.of(
                            "id", s.getId(),
                            "oraInizio", s.getOraInizio().toString().substring(0,5),
                            "occupato", true,
                            "film", fOccupato.getTitolo()
                    ));
                } else {
                    slotList.add(Map.of(
                            "id", s.getId(),
                            "oraInizio", s.getOraInizio().toString().substring(0,5),
                            "occupato", false
                    ));
                }
            }
            calendar.add(Map.of(
                    "data", current.toString(),
                    "slots", slotList
            ));
            current = current.plusDays(1);
        }
        return Map.of("durataFilm", durata, "calendar", calendar);
    }

}