package it.unisa.application.sottosistemi.gestione_prenotazione.view;

import it.unisa.application.model.entity.Cliente;
import it.unisa.application.model.entity.PostoProiezione;
import it.unisa.application.model.entity.Proiezione;
import it.unisa.application.model.entity.Prenotazione;
import it.unisa.application.sottosistemi.gestione_prenotazione.service.PrenotazioneService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/aggiungiOrdine")
public class AggiungiOrdineServlet extends HttpServlet {
    private final PrenotazioneService prenotazioneService = new PrenotazioneService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Reindirizza alla pagina con il form per aggiungere un ordine
        request.getRequestDispatcher("/WEB-INF/jsp/aggiungiOrdine.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Recupera i parametri della richiesta
            String emailCliente = request.getParameter("emailCliente");
            int idProiezione = Integer.parseInt(request.getParameter("idProiezione"));

            String postiInput = request.getParameter("posti");
            if (postiInput == null || postiInput.isEmpty()) {
                throw new IllegalArgumentException("Nessun posto selezionato.");
            }

            // Elabora i posti dalla stringa
            String[] postiSelezionati = postiInput.split(";");
            List<PostoProiezione> posti = new ArrayList<>();
            for (String posto : postiSelezionati) {
                String[] dettagliPosto = posto.split(",");
                if (dettagliPosto.length != 3) {
                    throw new IllegalArgumentException("Formato posti non valido.");
                }
                int idSala = Integer.parseInt(dettagliPosto[0]);
                char fila = dettagliPosto[1].charAt(0);
                int numero = Integer.parseInt(dettagliPosto[2]);
                posti.add(new PostoProiezione(idSala, fila, numero, idProiezione, true));
            }
            //Prenotazione prenotazione = prenotazioneService.aggiungiOrdine(cliente, posti, proiezione);

            // Reindirizza alla pagina dello storico ordini
            response.sendRedirect(request.getContextPath() + "/storicoOrdini?emailCliente=" + emailCliente);
        } catch (Exception e) {
            // Gestione degli errori
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        }
    }
}
