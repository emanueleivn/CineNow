package it.unisa.application.sottosistemi.gestione_sala.view;

import com.google.gson.Gson;
import it.unisa.application.model.dao.FilmDAO;
import it.unisa.application.model.dao.SlotDAO;
import it.unisa.application.model.entity.Film;
import it.unisa.application.model.entity.Slot;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/slotDisponibili")
public class SlotDisponibiliServlet extends HttpServlet {
    private static class SlotDTO {
        private int id;
        private String oraInizio;

        public SlotDTO(int id, String oraInizio) {
            this.id = id;
            this.oraInizio = oraInizio;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int filmId = Integer.parseInt(request.getParameter("filmId"));
            int salaId = Integer.parseInt(request.getParameter("salaId"));
            LocalDate data = LocalDate.parse(request.getParameter("data"));
            Film film = new FilmDAO().retrieveById(filmId);
            if (film == null) {
                throw new RuntimeException();
            }
            SlotDAO slotDAO = new SlotDAO();
            List<Slot> freeSlots = slotDAO.retriveFreeSlot(data, salaId);
            List<SlotDTO> dto = new ArrayList<>();
            for (Slot s : freeSlots) {
                dto.add(new SlotDTO(s.getId(), s.getOraInizio().toString()));
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(new Gson().toJson(dto));
            out.flush();
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Errore nel caricamento degli slot.");
        }
    }
}
