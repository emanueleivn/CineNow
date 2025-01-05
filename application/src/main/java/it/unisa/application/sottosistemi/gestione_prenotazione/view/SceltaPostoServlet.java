package it.unisa.application.sottosistemi.gestione_prenotazione.view;

import it.unisa.application.model.entity.PostoProiezione;
import it.unisa.application.model.entity.Proiezione;
import it.unisa.application.sottosistemi.gestione_prenotazione.service.PrenotazioneService;
import it.unisa.application.model.dao.ProiezioneDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/SceltaPosto")
public class SceltaPostoServlet extends HttpServlet {
    private final PrenotazioneService prenotazioneService = new PrenotazioneService();
    private final ProiezioneDAO proiezioneDAO = new ProiezioneDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int proiezioneId = Integer.parseInt(req.getParameter("proiezioneId"));
            Proiezione proiezione = proiezioneDAO.retrieveById(proiezioneId);
            if (proiezione == null) {
                req.setAttribute("errorMessage", "Proiezione non trovata.");
                req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
                return;
            }
            List<PostoProiezione> postiProiezione = prenotazioneService.ottieniPostiProiezione(proiezione);
            req.setAttribute("postiProiezione", postiProiezione);
            req.setAttribute("proiezione", proiezione);
            req.getRequestDispatcher("/WEB-INF/jsp/piantinaView.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            req.setAttribute("errorMessage", "Parametro non valido.");
            req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Errore durante il recupero dei posti.");
            req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
        }
    }
}
