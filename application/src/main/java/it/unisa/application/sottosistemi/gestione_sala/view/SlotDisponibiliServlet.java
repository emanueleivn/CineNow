package it.unisa.application.sottosistemi.gestione_sala.view;

import com.google.gson.Gson;
import it.unisa.application.sottosistemi.gestione_sala.service.SlotService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Map;

@WebServlet("/slotDisponibili")
public class SlotDisponibiliServlet extends HttpServlet {
    private final SlotService slotService = new SlotService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int filmId = Integer.parseInt(request.getParameter("filmId"));
            int salaId = Integer.parseInt(request.getParameter("salaId"));
            LocalDate dataInizio = LocalDate.parse(request.getParameter("dataInizio"));
            LocalDate dataFine = LocalDate.parse(request.getParameter("dataFine"));

            Map<String, Object> slots = slotService.slotDisponibili(filmId, salaId, dataInizio, dataFine);

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(new Gson().toJson(slots));
            out.flush();
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Errore nel caricamento degli slot.");
        }
    }
}
