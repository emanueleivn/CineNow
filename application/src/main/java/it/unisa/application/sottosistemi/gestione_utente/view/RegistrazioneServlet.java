package it.unisa.application.sottosistemi.gestione_utente.view;

import it.unisa.application.model.entity.Cliente;
import it.unisa.application.sottosistemi.gestione_utente.service.RegistrazioneService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
@WebServlet("/registrazione")
public class RegistrazioneServlet extends HttpServlet {
    RegistrazioneService regServ;

    @Override
    public void init() throws ServletException {
        regServ = new RegistrazioneService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/registrazioneView.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String nome = req.getParameter("nome");
        String cognome = req.getParameter("cognome");
        Cliente cliente = regServ.registrazione(email, password, nome, cognome);
        if(cliente!=null){
            HttpSession session = req.getSession();
            session.setAttribute("cliente", cliente);
            resp.sendRedirect(req.getContextPath() + "/Home");
        }else{
            req.setAttribute("errorMessage", "Formato non corretto o errore inserimento dati");
            req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
        }
    }
}
