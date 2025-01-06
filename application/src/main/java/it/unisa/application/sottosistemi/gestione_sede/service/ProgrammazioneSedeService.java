package it.unisa.application.sottosistemi.gestione_sede.service;

import it.unisa.application.model.dao.ProiezioneDAO;
import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.entity.Film;
import it.unisa.application.model.entity.Proiezione;
import it.unisa.application.model.entity.Sede;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ProgrammazioneSedeService {
    private final ProiezioneDAO proiezioneDAO = new ProiezioneDAO();
    public List<Proiezione> getProgrammazione(int sedeId) {
        List<Proiezione> programmazioni = proiezioneDAO.retrieveAllBySede(sedeId);
        LocalDate today = LocalDate.now();
        List<Proiezione> proiezioniFuture = programmazioni.stream()
                .filter(p -> !p.getDataProiezione().isBefore(today))
                .sorted((p1, p2) -> {
                    if (!p1.getDataProiezione().equals(p2.getDataProiezione())) {
                        return p1.getDataProiezione().compareTo(p2.getDataProiezione());
                    }
                    return p1.getOrarioProiezione().getOraInizio().compareTo(p2.getOrarioProiezione().getOraInizio());
                })
                .toList();

        return proiezioniFuture;
    }



    public List<Proiezione> getProiezioniFilm(int filmId, int sedeId){
        List<Proiezione> proiezioni = proiezioneDAO.retrieveByFilm(new Film(filmId),new Sede(sedeId));
        return proiezioni.stream()
                .filter(p -> !p.getDataProiezione().isBefore(LocalDate.now()) &&
                        !p.getDataProiezione().isAfter(LocalDate.now().plusDays(7)))
                .collect(Collectors.toList());

    }
  
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
