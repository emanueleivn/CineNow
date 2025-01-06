package it.unisa.application.sottosistemi.gestione_utente.view;

import it.unisa.application.sottosistemi.gestione_utente.service.AutenticazioneService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private AutenticazioneService authService;
    @Override
    public void init() throws ServletException {
        authService = new AutenticazioneService();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        authService.logout(req.getSession());
        req.getRequestDispatcher("/Home").forward(req, resp);
    }
}
