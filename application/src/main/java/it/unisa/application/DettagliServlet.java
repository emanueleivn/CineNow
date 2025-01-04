package it.unisa.application;

import it.unisa.application.model.dao.FilmDAO;
import it.unisa.application.model.entity.Film;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/DettagliFilm")
public class DettagliServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filmId = req.getParameter("filmId");
        String sedeId = req.getParameter("sedeId");
        FilmDAO filmDAO = new FilmDAO();
        Film film = filmDAO.retrieveById(Integer.parseInt(filmId));
        if (film != null) {
            req.setAttribute("film", film);
            req.setAttribute("sedeId",sedeId);
            req.getRequestDispatcher("/WEB-INF/jsp/dettagliFilm.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
        }
    }
}
