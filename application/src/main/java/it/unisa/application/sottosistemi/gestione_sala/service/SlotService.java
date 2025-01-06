package it.unisa.application.sottosistemi.gestione_sala.service;

import it.unisa.application.model.dao.FilmDAO;
import it.unisa.application.model.dao.ProiezioneDAO;
import it.unisa.application.model.dao.SlotDAO;
import it.unisa.application.model.entity.Film;
import it.unisa.application.model.entity.Proiezione;
import it.unisa.application.model.entity.Slot;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlotService {
    private final SlotDAO slotDAO = new SlotDAO();
    private final ProiezioneDAO proiezioneDAO = new ProiezioneDAO();
    private final FilmDAO filmDAO = new FilmDAO();

    public Map<String, Object> slotDisponibili(int filmId, int salaId, LocalDate dataInizio, LocalDate dataFine) throws Exception {
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
            for (Slot slot : allSlots) {
                Proiezione existing = proiezioneDAO.retrieveProiezioneBySalaSlotAndData(salaId, slot.getId(), current);
                Map<String, Object> slotData = new HashMap<>();
                slotData.put("id", slot.getId());
                slotData.put("oraInizio", slot.getOraInizio().toString().substring(0, 5));
                if (existing != null) {
                    Film filmOccupato = filmDAO.retrieveById(existing.getFilmProiezione().getId());
                    slotData.put("occupato", true);
                    slotData.put("film", filmOccupato.getTitolo());
                } else {
                    slotData.put("occupato", false);
                }
                slotList.add(slotData);
            }

            Slot lastSlot = allSlots.get(allSlots.size() - 1);
            int slotEndTime = lastSlot.getOraInizio().toLocalTime().toSecondOfDay() + (30 * 60); // Aggiungere 30 minuti

            if (slotEndTime < (lastSlot.getOraInizio().toLocalTime().toSecondOfDay() + durata * 60)) {
                Map<String, Object> lastSlotData = new HashMap<>();
                lastSlotData.put("id", lastSlot.getId());
                lastSlotData.put("oraInizio", lastSlot.getOraInizio().toString().substring(0, 5));
                lastSlotData.put("occupato", false);
                slotList.add(lastSlotData);
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