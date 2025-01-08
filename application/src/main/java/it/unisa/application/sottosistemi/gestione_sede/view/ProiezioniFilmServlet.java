package it.unisa.application.sottosistemi.gestione_sede.view;

import it.unisa.application.model.entity.Proiezione;
import it.unisa.application.model.entity.Film;
import it.unisa.application.model.entity.Sede;
import it.unisa.application.model.dao.FilmDAO;
import it.unisa.application.model.dao.SedeDAO;
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProgrammazioneSedeService service = new ProgrammazioneSedeService();
        FilmDAO filmDAO = new FilmDAO();
        SedeDAO sedeDAO = new SedeDAO();

        try {
            int sedeId = Integer.parseInt(req.getParameter("sedeId"));
            int filmId = Integer.parseInt(req.getParameter("filmId"));
            List<Proiezione> programmazioneFilm = service.getProiezioniFilm(filmId, sedeId);
            Film film = filmDAO.retrieveById(filmId);
            Sede sede = sedeDAO.retrieveById(sedeId);

            req.setAttribute("programmazioneFilm", programmazioneFilm);
            req.setAttribute("filmNome", film.getTitolo());
            req.setAttribute("sedeNome", sede.getNome());
            req.getRequestDispatcher("/WEB-INF/jsp/proiezioniFilm.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            req.setAttribute("errorMessage", "Parametri non validi.");
            req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Si è verificato un errore durante il recupero delle proiezioni.");
            req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
        }
    }
}
