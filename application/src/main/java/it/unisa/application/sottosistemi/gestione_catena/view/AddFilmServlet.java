package it.unisa.application.sottosistemi.gestione_catena.view;

import it.unisa.application.model.entity.Film;
import it.unisa.application.sottosistemi.gestione_catena.service.CatalogoService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/addFilm")
public class AddFilmServlet extends HttpServlet {
    private final CatalogoService catalogoService = new CatalogoService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String titolo = request.getParameter("titolo");
            String genere = request.getParameter("genere");
            String classificazione = request.getParameter("classificazione");
            int durata = Integer.parseInt(request.getParameter("durata"));
            String locandina = request.getParameter("locandina");
            String descrizione = request.getParameter("descrizione");

            Film film = new Film(0, titolo, genere, classificazione, durata, locandina, descrizione, false);
            if (!catalogoService.addFilmCatalogo(film)) {
                throw new RuntimeException("Errore nell'aggiunta del film. Film già esistente o parametri non validi.");
            }
            response.sendRedirect(request.getContextPath() + "/catalogo");
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        }
    }
}
