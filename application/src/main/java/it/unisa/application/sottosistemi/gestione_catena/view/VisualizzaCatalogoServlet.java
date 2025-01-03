package it.unisa.application.sottosistemi.gestione_catena.view;

import it.unisa.application.model.entity.Film;
import it.unisa.application.sottosistemi.gestione_catena.service.CatalogoService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/catalogo")
public class VisualizzaCatalogoServlet extends HttpServlet {
    private final CatalogoService catalogoService = new CatalogoService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Film> catalogo = catalogoService.getCatalogo();
        if (catalogo.isEmpty()) {
            request.setAttribute("errorMessage", "Nessun film trovato.");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        } else {
            request.setAttribute("catalogo", catalogo);
            request.getRequestDispatcher("/WEB-INF/jsp/catalogo.jsp").forward(request, response);
        }
    }
}
