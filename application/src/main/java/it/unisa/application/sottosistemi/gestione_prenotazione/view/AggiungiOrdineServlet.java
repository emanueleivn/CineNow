package it.unisa.application.sottosistemi.gestione_prenotazione.view;

import it.unisa.application.model.entity.Cliente;
import it.unisa.application.model.entity.Posto;
import it.unisa.application.model.entity.PostoProiezione;
import it.unisa.application.model.entity.Proiezione;
import it.unisa.application.model.entity.Sala;
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
        request.getRequestDispatcher("/WEB-INF/jsp/aggiungiOrdine.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Cliente cliente = (Cliente) request.getSession().getAttribute("cliente");
            if (cliente == null) {
                throw new IllegalArgumentException("Cliente non trovato.");
            }

            int idProiezione = Integer.parseInt(request.getParameter("idProiezione"));
            Proiezione proiezione = new Proiezione();
            proiezione.setId(idProiezione);

            String postiInput = request.getParameter("posti");
            if (postiInput == null || postiInput.isEmpty()) {
                throw new IllegalArgumentException("Nessun posto selezionato.");
            }

            String[] postiSelezionati = postiInput.split(";");
            List<PostoProiezione> posti = new ArrayList<>();
            for (String posto : postiSelezionati) {
                String[] dettagliPosto = posto.split(",");
                if (dettagliPosto.length != 3) {
                    throw new IllegalArgumentException("Formato posti non valido.");
                }

                Sala sala = new Sala();
                sala.setId(Integer.parseInt(dettagliPosto[0]));

                Posto p = new Posto();
                p.setSala(sala);
                p.setFila(dettagliPosto[1].charAt(0));
                p.setNumero(Integer.parseInt(dettagliPosto[2]));

                PostoProiezione postoProiezione = new PostoProiezione();
                postoProiezione.setPosto(p);
                postoProiezione.setProiezione(proiezione);
                postoProiezione.setStato(true);
                posti.add(postoProiezione);
            }

            prenotazioneService.aggiungiOrdine(cliente, posti, proiezione);
            response.sendRedirect(request.getContextPath() + "/storicoOrdini");
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        }
    }
}
