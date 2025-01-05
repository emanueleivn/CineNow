package it.unisa.application.sottosistemi.gestione_prenotazione.view;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/Checkout")
public class CheckoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String proiezioneId = request.getParameter("proiezioneId");
        String posti = request.getParameter("posti");
        String totale = request.getParameter("totale");
        request.setAttribute("proiezioneId", proiezioneId);
        request.setAttribute("posti", posti);
        request.setAttribute("totale", totale);
        request.getRequestDispatcher("/WEB-INF/jsp/checkout.jsp").forward(request, response);
    }
}

