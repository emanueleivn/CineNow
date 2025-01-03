package it.unisa.application.sottosistemi.gestione_sala.view;

import it.unisa.application.model.dao.FilmDAO;
import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.entity.Film;
import it.unisa.application.model.entity.Sala;
import it.unisa.application.sottosistemi.gestione_sala.service.ProgrammazioneService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/aggiungiProiezione")
public class AggiungiProiezioneServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int sedeId = Integer.parseInt(request.getParameter("sedeId"));
        FilmDAO filmDAO = new FilmDAO();
        SedeDAO sedeDAO = new SedeDAO();
        List<Film> films = filmDAO.retrieveAll();
        List<Sala> sale = sedeDAO.retrieveSaleBySede(sedeId);
        request.setAttribute("films", films);
        request.setAttribute("sale", sale);
        request.setAttribute("sedeId", sedeId);
        request.getRequestDispatcher("/WEB-INF/jsp/aggiungiProiezione.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int filmId = Integer.parseInt(request.getParameter("film"));
            int salaId = Integer.parseInt(request.getParameter("sala"));
            LocalDate data = LocalDate.parse(request.getParameter("dataProiezione"));
            String[] slotScelti = request.getParameterValues("slot");
            if (slotScelti == null || slotScelti.length == 0) {
                throw new RuntimeException();
            }
            List<Integer> slotIds = new ArrayList<>();
            for (String s : slotScelti) {
                slotIds.add(Integer.parseInt(s));
            }
            ProgrammazioneService service = new ProgrammazioneService();
            boolean success = service.aggiungiProiezione(filmId, salaId, slotIds, data);
            if (success) {
                String sedeId = request.getParameter("sedeId");
                response.sendRedirect("gestioneProgrammazione?sedeId=" + sedeId);
            } else {
                request.setAttribute("errorMessage", "Errore durante l'aggiunta della proiezione.");
                request.getRequestDispatcher("/WEB-INF/jsp/errore.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Errore sconosciuto.");
            request.getRequestDispatcher("/WEB-INF/jsp/errore.jsp").forward(request, response);
        }
    }
}
