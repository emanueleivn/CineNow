package it.unisa.application.sottosistemi.gestione_sala.view;

import it.unisa.application.model.dao.FilmDAO;
import it.unisa.application.model.dao.SlotDAO;
import it.unisa.application.model.entity.Film;
import it.unisa.application.model.entity.Proiezione;
import it.unisa.application.model.entity.Slot;
import it.unisa.application.sottosistemi.gestione_sala.service.ProgrammazioneService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/gestioneProgrammazione")
public class GestioneProgrammazioneServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int sedeId = Integer.parseInt(request.getParameter("sedeId"));

        ProgrammazioneService programmazioneService = new ProgrammazioneService();
        List<Proiezione> programmazioni = programmazioneService.getProgrammazioniBySede(sedeId);

        for (Proiezione p : programmazioni) {
            Film film = new FilmDAO().retrieveById(p.getFilmProiezione().getId());
            Slot slot = new SlotDAO().retriveById(p.getOrarioProiezione().getId());
            p.setFilmProiezione(film);
            p.setOrarioProiezione(slot);
        }

        request.setAttribute("programmazioni", programmazioni);
        request.getRequestDispatcher("/WEB-INF/jsp/gestioneProgrammazione.jsp").forward(request, response);
    }
}
