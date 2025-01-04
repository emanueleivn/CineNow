package it.unisa.application.sottosistemi.gestione_sede.view;

import it.unisa.application.model.entity.Proiezione;
import it.unisa.application.sottosistemi.gestione_sede.service.ProgrammazioneSedeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/ProiezioniFilm")
public class ProiezioniFilmServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("errorMessage", "Accesso non consentito");
        req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProgrammazioneSedeService service = new ProgrammazioneSedeService();
        int sedeId = Integer.parseInt(req.getParameter("sedeId"));
        int filmId = Integer.parseInt(req.getParameter("filmId"));
        List<Proiezione> programmazioneFilm =service.getProgrammazioneFilm(filmId, sedeId);
        if (programmazioneFilm.isEmpty()) {
            req.setAttribute("errorMessage", "Proizioni non trovate");
            req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
        }
        req.setAttribute("programmazioneFilm", programmazioneFilm);
        req.getRequestDispatcher("/WEB-INF/jsp/proiezioniFilm.jsp").forward(req, resp);
    }
}
