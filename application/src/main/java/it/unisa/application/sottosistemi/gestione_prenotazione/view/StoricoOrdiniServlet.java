package it.unisa.application.sottosistemi.gestione_prenotazione.view;

import it.unisa.application.model.entity.Cliente;
import it.unisa.application.model.entity.Prenotazione;
import it.unisa.application.sottosistemi.gestione_prenotazione.service.PrenotazioneService;

import it.unisa.application.sottosistemi.gestione_prenotazione.service.StoricoOrdiniService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/storicoOrdini")
public class StoricoOrdiniServlet extends HttpServlet {
    private final StoricoOrdiniService storicoOrdiniService = new StoricoOrdiniService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Cliente cliente = (Cliente) request.getSession().getAttribute("cliente");

            if (cliente == null) {
                throw new IllegalArgumentException("Cliente non trovato nella sessione.");
            }

            List<Prenotazione> storico = storicoOrdiniService.storicoOrdini(cliente);
            request.setAttribute("storico", storico);
            request.getRequestDispatcher("/WEB-INF/jsp/storicoOrdini.jsp").forward(request, response);

        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Si è verificato un errore durante il recupero dello storico ordini.");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        }
    }
}
