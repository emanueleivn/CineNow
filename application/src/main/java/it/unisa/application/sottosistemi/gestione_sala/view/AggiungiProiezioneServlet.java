package it.unisa.application.sottosistemi.gestione_sala.view;

import it.unisa.application.model.entity.Film;
import it.unisa.application.model.entity.Sala;
import it.unisa.application.sottosistemi.gestione_sala.service.ProgrammazioneService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/aggiungiProiezione")
public class AggiungiProiezioneServlet extends HttpServlet {
    private final ProgrammazioneService programmazioneService = new ProgrammazioneService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int sedeId = Integer.parseInt(request.getParameter("sedeId"));
            List<Film> films = programmazioneService.getCatalogoFilm();
            List<Sala> sale = programmazioneService.getSaleBySede(sedeId);

            request.setAttribute("films", films);
            request.setAttribute("sale", sale);
            request.setAttribute("sedeId", sedeId);
            request.getRequestDispatcher("/WEB-INF/jsp/aggiungiProiezione.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Errore durante il caricamento della pagina.");
            request.getRequestDispatcher("/WEB-INF/jsp/errore.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int filmId = Integer.parseInt(request.getParameter("film"));
            int salaId = Integer.parseInt(request.getParameter("sala"));
            String[] slotScelti = request.getParameterValues("slot");
            LocalDate dataProiezione = LocalDate.parse(request.getParameter("data"));

            if (slotScelti == null || slotScelti.length == 0) {
                throw new RuntimeException("Nessuno slot selezionato.");
            }

            List<Integer> slotIds = List.of(slotScelti).stream().map(Integer::parseInt).toList();

            boolean success = programmazioneService.aggiungiProiezione(filmId, salaId, slotIds, dataProiezione);

            if (success) {
                response.sendRedirect("gestioneProgrammazione?sedeId=" + request.getParameter("sedeId"));
            } else {
                request.setAttribute("errorMessage", "Errore durante l'aggiunta della proiezione.");
                request.getRequestDispatcher("/WEB-INF/jsp/errore.jsp").forward(request, response);
            }
        } catch (RuntimeException e) {
            // Log dettagliato per eccezioni previste
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore durante l'elaborazione della richiesta: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/errore.jsp").forward(request, response);
        } catch (Exception e) {
            // Log dettagliato per eccezioni sconosciute
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore sconosciuto: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/errore.jsp").forward(request, response);
        }
    }

}
