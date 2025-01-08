package it.unisa.application.sottosistemi.gestione_sede.view;

import it.unisa.application.model.entity.Film;
import it.unisa.application.model.entity.Sede;
import it.unisa.application.sottosistemi.gestione_sede.service.ProgrammazioneSedeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/Catalogo")
public class CatalogoSedeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sede = req.getParameter("sede");
        if (sede == null || sede.isBlank()) {
            req.setAttribute("errorMessage", "Errore caricamento catalogo: sede non specificata");
            req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
            return;
        }
        Sede sedeObject = new Sede();
        List<Film> catalogo;
        sedeObject.setNome(sede);
        ProgrammazioneSedeService service = new ProgrammazioneSedeService();
        switch (sede) {
            case "Mercogliano":
                sedeObject.setId(1);
                catalogo = service.getCatalogoSede(sedeObject);
                req.setAttribute("sede", "Mercogliano");
                req.setAttribute("sedeId", sedeObject.getId());
                break;
            case "Laquila":
                sedeObject.setId(2);
                catalogo = service.getCatalogoSede(sedeObject);
                req.setAttribute("sede", "L'Aquila");
                req.setAttribute("sedeId", sedeObject.getId());
                break;
            default:
                req.setAttribute("errorMessage", "Errore caricamento catalogo");
                req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
                return;
        }
        if (catalogo != null) {
            req.setAttribute("catalogo", catalogo);
            req.getRequestDispatcher("/WEB-INF/jsp/catalogoSede.jsp").forward(req, resp);
        } else {
            req.setAttribute("errorMessage", "Errore caricamento catalogo");
            req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
        }
    }
}
