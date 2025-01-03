package it.unisa.application.sottosistemi.gestione_sala.view;

import com.google.gson.Gson;
import it.unisa.application.model.dao.FilmDAO;
import it.unisa.application.model.dao.ProiezioneDAO;
import it.unisa.application.model.dao.SlotDAO;
import it.unisa.application.model.entity.Film;
import it.unisa.application.model.entity.Proiezione;
import it.unisa.application.model.entity.Slot;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/slotDisponibili")
public class SlotDisponibiliServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int filmId = Integer.parseInt(request.getParameter("filmId"));
            int salaId = Integer.parseInt(request.getParameter("salaId"));
            LocalDate dataInizio = LocalDate.parse(request.getParameter("dataInizio"));
            LocalDate dataFine = LocalDate.parse(request.getParameter("dataFine"));
            FilmDAO filmDAO = new FilmDAO();
            ProiezioneDAO pdao = new ProiezioneDAO();
            SlotDAO slotDAO = new SlotDAO();
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
                    Proiezione existing = pdao.retrieveProiezioneBySalaSlotAndData(salaId, s.getId(), current);
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
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            Map<String, Object> respObj = Map.of("durataFilm", durata, "calendar", calendar);
            out.print(new Gson().toJson(respObj));
            out.flush();
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Errore caricamento slot.");
        }
    }
}
