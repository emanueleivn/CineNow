package it.unisa.application.sottosistemi.gestione_sede.view;

import it.unisa.application.model.entity.Proiezione;
import it.unisa.application.sottosistemi.gestione_sede.service.ProgrammazioneSedeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/gestioneProgrammazione")
public class GestioneProgrammazioneServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int sedeId = Integer.parseInt(request.getParameter("sedeId"));
        ProgrammazioneSedeService service = new ProgrammazioneSedeService();
        List<Proiezione> programmazioni = service.getProgrammazioniBySede(sedeId);
        request.setAttribute("programmazioni", programmazioni);
        request.getRequestDispatcher("/WEB-INF/jsp/gestioneProgrammazione.jsp").forward(request, response);
    }
}
