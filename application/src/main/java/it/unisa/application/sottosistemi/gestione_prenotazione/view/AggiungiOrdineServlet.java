package it.unisa.application.sottosistemi.gestione_prenotazione.view;

import it.unisa.application.model.dao.ProiezioneDAO;
import it.unisa.application.model.entity.Cliente;
import it.unisa.application.model.entity.Posto;
import it.unisa.application.model.entity.PostoProiezione;
import it.unisa.application.model.entity.Proiezione;
import it.unisa.application.sottosistemi.gestione_prenotazione.service.PrenotazioneService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/AggiungiOrdine")
public class AggiungiOrdineServlet extends HttpServlet {
    private final PrenotazioneService prenotazioneService = new PrenotazioneService();
    private final ProiezioneDAO proiezioneDAO = new ProiezioneDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String proiezioneId = request.getParameter("proiezioneId");
        String postiParam = request.getParameter("posti");
        String nomeCarta = request.getParameter("nomeCarta");
        String numeroCarta = request.getParameter("numeroCarta");
        String scadenzaCarta = request.getParameter("scadenzaCarta");
        String cvv = request.getParameter("cvv");
        if (proiezioneId == null || postiParam == null || nomeCarta == null || numeroCarta == null
                || scadenzaCarta == null || cvv == null) {
            request.setAttribute("errorMessage", "Errore nel checkout. Dati mancanti.");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        HttpSession session = request.getSession();
        Cliente cliente = (Cliente) session.getAttribute("cliente");
        if (cliente == null) {
            request.setAttribute("errorMessage", "Errore generico");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        try {
            Proiezione proiezione = proiezioneDAO.retrieveById(Integer.parseInt(proiezioneId));
            if (proiezione == null) {
                request.setAttribute("errorMessage", "Errore: Proiezione non trovata.");
                request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
                return;
            }
            List<PostoProiezione> postiList = new ArrayList<>();
            for (String posto : postiParam.split(",")) {
                String[] parts = posto.split("-");
                if (parts.length == 2) {
                    char fila = parts[0].charAt(0);
                    int numero = Integer.parseInt(parts[1]);
                    Posto postoEntity = new Posto(proiezione.getSalaProiezione(), fila, numero);
                    PostoProiezione postoProiezione = new PostoProiezione(postoEntity, proiezione);
                    postiList.add(postoProiezione);
                }
            }

            prenotazioneService.aggiungiOrdine(cliente, postiList, proiezione);
            response.sendRedirect(request.getContextPath() + "/storicoOrdini");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore durante la creazione dell'ordine: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        }
    }
}
