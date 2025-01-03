package it.unisa.application.sottosistemi.gestione_sede.view;

import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.entity.Film;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/Catalogo")
public class CatalogoSedeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sede = req.getParameter("sede");
        SedeDAO sedeDAO = new SedeDAO();
        List<Film> catalogo = new ArrayList<>();
        switch (sede) {
            case "Mercogliano":
                try {
                    catalogo = sedeDAO.retrieveFilm(1);
                    req.setAttribute("sede", "Mercogliano");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "Laquila":
                try {
                    catalogo = sedeDAO.retrieveFilm(2);
                    req.setAttribute("sede", "L'Aquila");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                req.setAttribute("errorMessage", "Errore caricamento catalogo");
                req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
                return;
        }
        req.setAttribute("catalogo", catalogo);
        req.getRequestDispatcher("/WEB-INF/jsp/catalogoSede.jsp").forward(req, resp);
    }
}
