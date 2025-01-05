package it.unisa.application.utilities;

import it.unisa.application.model.entity.Utente;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/GestCatenaFilter")
public class GestCatenaFilter extends HttpFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession(false);
        Utente utente = (Utente) session.getAttribute("gestoreCatena");
        if (utente != null && utente.getRuolo().equals("gestore_catena")) {
            chain.doFilter(request, response);
        } else {
            String error = "Non sei autorizzato.";
            request.setAttribute("errorMessage", error);
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        }
    }
}
