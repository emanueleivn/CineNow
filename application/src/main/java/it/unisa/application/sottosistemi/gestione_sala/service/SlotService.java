package it.unisa.application.sottosistemi.gestione_sala.service;

import it.unisa.application.model.dao.SlotDAO;
import it.unisa.application.model.entity.Sala;
import it.unisa.application.model.entity.Slot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SlotService {
    private final SlotDAO slotDAO = new SlotDAO();

    public List<List<Slot>> trovaBlocchiSlot(Sala sala, LocalDate data, int durataFilm) {
        List<Slot> freeSlots = slotDAO.retriveFreeSlot(data, sala.getId());
        freeSlots.sort((s1, s2) -> s1.getOraInizio().compareTo(s2.getOraInizio()));
        List<List<Slot>> blocchi = new ArrayList<>();
        for (int i = 0; i < freeSlots.size(); i++) {
            List<Slot> blocco = new ArrayList<>();
            Slot first = freeSlots.get(i);
            LocalDateTime start = LocalDateTime.of(data, first.getOraInizio().toLocalTime());
            LocalDateTime requiredEnd = start.plusMinutes(durataFilm);
            blocco.add(first);
            LocalDateTime endSlot = start.plusMinutes(60);
            int j = i + 1;
            while (j < freeSlots.size() && endSlot.isBefore(requiredEnd)) {
                Slot next = freeSlots.get(j);
                LocalDateTime nextStart = LocalDateTime.of(data, next.getOraInizio().toLocalTime());
                if (!nextStart.isBefore(endSlot)) {
                    blocco.add(next);
                    endSlot = nextStart.plusMinutes(60);
                } else {
                    break;
                }
                j++;
            }
            if (!endSlot.isBefore(requiredEnd)) {
                blocchi.add(blocco);
            }
        }
        return blocchi;
    }
}
