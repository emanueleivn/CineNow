package it.unisa.application.sottosistemi.gestione_sede.view;

import it.unisa.application.model.dao.FilmDAO;
import it.unisa.application.model.dao.SlotDAO;
import it.unisa.application.model.entity.Film;
import it.unisa.application.model.entity.Proiezione;
import it.unisa.application.model.entity.Slot;
import it.unisa.application.sottosistemi.gestione_sede.service.ProgrammazioneSedeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/gestioneProgrammazione")
public class GestioneProgrammazioneServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int sedeId = Integer.parseInt(request.getParameter("sedeId"));
        ProgrammazioneSedeService service = new ProgrammazioneSedeService();
        List<Proiezione> programmazioni = service.getProgrammazioniBySede(sedeId);
        FilmDAO filmDAO = new FilmDAO();
        SlotDAO slotDAO = new SlotDAO();
        for (Proiezione p : programmazioni) {
            Film f = filmDAO.retrieveById(p.getFilmProiezione().getId());
            Slot s = slotDAO.retriveById(p.getOrarioProiezione().getId());
            p.setFilmProiezione(f);
            p.setOrarioProiezione(s);
        }
        request.setAttribute("programmazioni", programmazioni);
        request.getRequestDispatcher("/WEB-INF/jsp/gestioneProgrammazione.jsp").forward(request, response);
    }
}
