package it.unisa.application.sottosistemi.gestione_prenotazione.view;

import it.unisa.application.model.entity.Cliente;
import it.unisa.application.model.entity.Prenotazione;
import it.unisa.application.sottosistemi.gestione_prenotazione.service.PrenotazioneService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/storicoOrdini")
public class StoricoOrdiniServlet extends HttpServlet {
    private final PrenotazioneService prenotazioneService = new PrenotazioneService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Recupera l'email del cliente dal parametro della richiesta
            Cliente cliente = (Cliente) request.getAttribute("cliente");

            // Validazione del parametro
            if (cliente == null) {
                throw new IllegalArgumentException("Errore cliente.");
            }

            // Recupera lo storico degli ordini per il cliente
            List<Prenotazione> storico = prenotazioneService.storicoOrdini(cliente);

            // Controllo se lo storico è vuoto
            if (storico == null || storico.isEmpty()) {
                throw new IllegalArgumentException("Nessuna prenotazione trovata per il cliente.");
            }

            // Imposta gli attributi per la vista JSP
            request.setAttribute("cliente", cliente);
            request.setAttribute("storico", storico);

            // Reindirizza alla vista JSP
            request.getRequestDispatcher("/WEB-INF/jsp/storicoOrdini.jsp").forward(request, response);

        } catch (IllegalArgumentException e) {
            // Gestione degli errori specifici
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);

        } catch (Exception e) {
            // Gestione generale degli errori
            request.setAttribute("errorMessage", "Si è verificato un errore durante il recupero dello storico ordini.");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        }
    }
}
