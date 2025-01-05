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
    private final ProgrammazioneService programmazioneService = new ProgrammazioneService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String sedeIdParam = request.getParameter("sedeId");
            if (sedeIdParam == null || sedeIdParam.trim().isEmpty()) {
                throw new IllegalArgumentException("Parametro sedeId mancante o nullo.");
            }

            int sedeId;
            try {
                sedeId = Integer.parseInt(sedeIdParam);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Parametro sedeId non valido: deve essere un numero.");
            }

            FilmDAO filmDAO = new FilmDAO();
            SedeDAO sedeDAO = new SedeDAO();

            List<Film> films = filmDAO.retrieveAll();
            List<Sala> sale = sedeDAO.retrieveSaleBySede(sedeId);

            if (films == null || films.isEmpty()) {
                throw new RuntimeException("Nessun film disponibile.");
            }
            if (sale == null || sale.isEmpty()) {
                throw new RuntimeException("Nessuna sala disponibile per la sede selezionata.");
            }

            request.setAttribute("sedeId", sedeId);
            request.setAttribute("films", films);
            request.setAttribute("sale", sale);
            request.getRequestDispatcher("/WEB-INF/jsp/aggiungiProiezione.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Errore durante il caricamento della pagina: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/errore.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String sedeIdParam = request.getParameter("sedeId");
            if (sedeIdParam == null || sedeIdParam.trim().isEmpty()) {
                throw new IllegalArgumentException("Parametro sedeId mancante o nullo.");
            }

            int sedeId = Integer.parseInt(sedeIdParam);
            int filmId = Integer.parseInt(request.getParameter("film"));
            int salaId = Integer.parseInt(request.getParameter("sala"));
            String[] slotScelti = request.getParameterValues("slot");

            if (slotScelti == null || slotScelti.length == 0) {
                throw new IllegalArgumentException("Nessuno slot selezionato.");
            }

            List<Integer> slotIds = new ArrayList<>();
            LocalDate dataProiezione = null;

            for (String slot : slotScelti) {
                String[] parts = slot.split(":");
                int slotId = Integer.parseInt(parts[0]);
                dataProiezione = LocalDate.parse(parts[1]);
                slotIds.add(slotId);
            }

            boolean success = programmazioneService.aggiungiProiezione(filmId, salaId, slotIds, dataProiezione);
            if (success) {
                response.sendRedirect("gestioneProgrammazione?sedeId=" + sedeId);
            } else {
                request.setAttribute("errorMessage", "Errore durante l'aggiunta della proiezione.");
                request.getRequestDispatcher("/WEB-INF/jsp/errore.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Errore durante il salvataggio: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/errore.jsp").forward(request, response);
        }
    }
}
