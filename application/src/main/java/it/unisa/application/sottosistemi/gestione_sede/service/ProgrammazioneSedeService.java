package it.unisa.application.sottosistemi.gestione_sede.service;

import it.unisa.application.model.dao.FilmDAO;
import it.unisa.application.model.dao.ProiezioneDAO;
import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.dao.SlotDAO;
import it.unisa.application.model.entity.Film;
import it.unisa.application.model.entity.Proiezione;
import it.unisa.application.model.entity.Sede;
import it.unisa.application.model.entity.Slot;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class ProgrammazioneSedeService {
    private final ProiezioneDAO proiezioneDAO = new ProiezioneDAO();
    private final FilmDAO filmDAO = new FilmDAO();
    private final SlotDAO slotDAO = new SlotDAO();

    public List<Proiezione> getProgrammazioniBySede(int sedeId) {
        List<Proiezione> programmazioni = proiezioneDAO.retrieveAllBySede(sedeId);
        programmazioni.sort((p1, p2) -> {
            if (!p1.getDataProiezione().equals(p2.getDataProiezione())) {
                return p1.getDataProiezione().compareTo(p2.getDataProiezione());
            }
            return p1.getOrarioProiezione().getOraInizio().compareTo(p2.getOrarioProiezione().getOraInizio());
        });

        Map<Integer, Map<String, LocalTime>> nextAvailableSlot = new HashMap<>();
        List<Proiezione> uniqueProiezioni = new ArrayList<>();

        for (Proiezione p : programmazioni) {
            int salaId = p.getSalaProiezione().getId();
            String date = p.getDataProiezione().toString();
            LocalTime startTime = p.getOrarioProiezione().getOraInizio().toLocalTime();
            Film film = filmDAO.retrieveById(p.getFilmProiezione().getId());
            int durata = film.getDurata();
            int slotsNeeded = durata / 30;
            LocalTime projectionEnd = startTime.plusMinutes(slotsNeeded * 30);

            nextAvailableSlot.putIfAbsent(salaId, new HashMap<>());
            Map<String, LocalTime> salaSlots = nextAvailableSlot.get(salaId);
            LocalTime availableTime = salaSlots.getOrDefault(date, LocalTime.MIN);

            if (!startTime.isBefore(availableTime)) {
                uniqueProiezioni.add(p);
                salaSlots.put(date, projectionEnd);
            }
        }

        return uniqueProiezioni;
    }


    public void getProgrammazioneFilm(int sedeId) {}

    public List<Film> getCatalogoSede(Sede sede){
        SedeDAO sedeDAO = new SedeDAO();
        List<Film> catalogo;
        try {
            catalogo = sedeDAO.retrieveFilm(sede.getId());
        } catch (SQLException e) {
            return null;
        }
        return catalogo;
    }
}
