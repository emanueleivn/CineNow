package it.unisa.application.sottosistemi.gestione_sede.view;

import it.unisa.application.model.entity.Proiezione;
import it.unisa.application.sottosistemi.gestione_sede.service.ProgrammazioneSedeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/gestioneProgrammazione")
public class GestioneProgrammazioneServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(GestioneProgrammazioneServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int sedeId = Integer.parseInt(request.getParameter("sedeId"));
            ProgrammazioneSedeService service = new ProgrammazioneSedeService();
            List<Proiezione> programmazioni = service.getProgrammazione(sedeId);
            request.setAttribute("programmazioni", programmazioni);
            request.getRequestDispatcher("/WEB-INF/jsp/gestioneProgrammazione.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Parametro sedeId non valido.", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parametro sedeId non valido.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Errore durante il recupero delle proiezioni.", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante il recupero delle proiezioni.");
        }
    }
}
