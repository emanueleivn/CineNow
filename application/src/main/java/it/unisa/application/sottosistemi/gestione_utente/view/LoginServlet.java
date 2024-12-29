package it.unisa.application.sottosistemi.gestione_utente.view;

import it.unisa.application.model.entity.Cliente;
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/loginView.jsp").forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Cliente cliente = authService.login(email, password);
        if (cliente != null) {
            HttpSession session = request.getSession();
            session.setAttribute("clienteLoggato", cliente);
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } else {
            request.setAttribute("errorMessage", "Formato non corretto o errore inserimento dati");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        }
    }
}
