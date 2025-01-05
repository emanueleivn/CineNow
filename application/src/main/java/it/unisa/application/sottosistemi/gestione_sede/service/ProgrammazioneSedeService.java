package it.unisa.application.sottosistemi.gestione_sede.service;

import it.unisa.application.model.dao.FilmDAO;
import it.unisa.application.model.dao.ProiezioneDAO;
import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.entity.Film;
import it.unisa.application.model.entity.Proiezione;
import it.unisa.application.model.entity.Sede;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProgrammazioneSedeService {
    private final ProiezioneDAO proiezioneDAO = new ProiezioneDAO();
    private final FilmDAO filmDAO = new FilmDAO();
    private static final Logger logger = Logger.getLogger(ProgrammazioneSedeService.class.getName());

    public List<Proiezione> getProgrammazioniBySede(int sedeId) {
        List<Proiezione> programmazioni = proiezioneDAO.retrieveAllBySede(sedeId);
        programmazioni.sort((p1, p2) -> {
            if (!p1.getDataProiezione().equals(p2.getDataProiezione())) {
                return p1.getDataProiezione().compareTo(p2.getDataProiezione());
            }
            LocalTime time1 = p1.getSlotsProiezione().stream()
                    .map(slot -> slot.getOraInizio().toLocalTime())
                    .min(LocalTime::compareTo)
                    .orElse(LocalTime.MIN);
            LocalTime time2 = p2.getSlotsProiezione().stream()
                    .map(slot -> slot.getOraInizio().toLocalTime())
                    .min(LocalTime::compareTo)
                    .orElse(LocalTime.MIN);
            return time1.compareTo(time2);
        });

        Map<Integer, Map<String, LocalTime>> nextAvailableSlot = new HashMap<>();
        List<Proiezione> uniqueProiezioni = new ArrayList<>();

        for (Proiezione p : programmazioni) {
            int salaId = p.getSalaProiezione().getId();
            String date = p.getDataProiezione().toString();
            LocalTime startTime = p.getSlotsProiezione().stream()
                    .map(slot -> slot.getOraInizio().toLocalTime())
                    .min(LocalTime::compareTo)
                    .orElse(LocalTime.MIN);
            Film film = filmDAO.retrieveById(p.getFilmProiezione().getId());


            int durata = film.getDurata();
            int slotsNeeded = (int) Math.ceil(durata / 30.0);
            LocalTime projectionEnd = startTime.plusMinutes(slotsNeeded * 30);

            nextAvailableSlot.putIfAbsent(salaId, new HashMap<>());
            Map<String, LocalTime> salaSlots = nextAvailableSlot.get(salaId);
            LocalTime availableTime = salaSlots.getOrDefault(date, LocalTime.MIN);

            if (!startTime.isBefore(availableTime)) {
                p.getSlotsProiezione().sort(Comparator.comparing(slot -> slot.getOraInizio().toLocalTime()));
                uniqueProiezioni.add(p);
                salaSlots.put(date, projectionEnd);
            }
        }

        return uniqueProiezioni;
    }

    public List<Proiezione> getProgrammazioneFilm(int filmId, int sedeId){
        return proiezioneDAO.retrieveByFilm(new Film(filmId), new Sede(sedeId));
    }

    public List<Film> getCatalogoSede(Sede sede){
        SedeDAO sedeDAO = new SedeDAO();
        List<Film> catalogo;
        try {
            catalogo = sedeDAO.retrieveFilm(sede.getId());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore nel recupero del catalogo della sede ID " + sede.getId(), e);
            return null;
        }
        return catalogo;
    }
}
