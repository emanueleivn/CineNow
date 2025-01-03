package it.unisa.application.sottosistemi.gestione_sala.service;

import it.unisa.application.model.dao.SlotDAO;
import it.unisa.application.model.entity.Slot;
import java.time.LocalDate;
import java.util.List;

public class SlotService {
    private final SlotDAO slotDAO = new SlotDAO();
    public List<Slot> trovaSlotDisponibiliPerGiorno(int salaId, LocalDate giorno) {
        return slotDAO.retriveFreeSlotPerGiorno(salaId, giorno);
    }
}
