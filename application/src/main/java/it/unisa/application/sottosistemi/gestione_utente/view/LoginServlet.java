package it.unisa.application.sottosistemi.gestione_utente.view;

import it.unisa.application.model.entity.Utente;
import it.unisa.application.sottosistemi.gestione_utente.service.AutenticazioneService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private AutenticazioneService authService;

    @Override
    public void init() {
        authService = new AutenticazioneService();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/loginView.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Utente utente = authService.login(email, password);
        if (utente != null) {
            HttpSession session = request.getSession(true);
            String ruolo = utente.getRuolo().toLowerCase();
            switch (ruolo) {
                case "cliente":
                    session.setAttribute("cliente", utente);
                    response.sendRedirect(request.getContextPath() + "/Home");
                    break;
                case "gestore_sede":
                    session.setAttribute("gestoreSede", utente);
                    response.sendRedirect(request.getContextPath() + "/areaGestoreSede.jsp");
                    break;
                case "gestore_catena":
                    session.setAttribute("gestoreCatena", utente);
                    response.sendRedirect(request.getContextPath() + "/areaGestoreCatena.jsp");
                    break;
                default:
                    request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
                    break;
            }
        } else {
            request.setAttribute("errorMessage", "Formato non corretto o errore inserimento dati");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        }
    }
}
