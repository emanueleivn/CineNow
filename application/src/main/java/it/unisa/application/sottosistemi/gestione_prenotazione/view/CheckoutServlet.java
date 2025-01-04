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
        // Recupera i dati dalla prima JSP
        String proiezioneId = request.getParameter("proiezioneId");
        String posti = request.getParameter("posti");
        String totale = request.getParameter("totale");

        // Imposta gli attributi per la JSP del checkout
        request.setAttribute("proiezioneId", proiezioneId);
        request.setAttribute("posti", posti);
        request.setAttribute("totale", totale);

        // Inoltra a checkout.jsp
        request.getRequestDispatcher("/WEB-INF/jsp/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Riceve i dati della carta di credito e gli altri dati necessari
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String numeroCarta = request.getParameter("numeroCarta");
        String scadenza = request.getParameter("scadenza");
        String cvv = request.getParameter("cvv");

        // Altri dati recuperati
        String proiezioneId = request.getParameter("proiezioneId");
        String posti = request.getParameter("posti");
        String totale = request.getParameter("totale");

        System.out.println("Dati ricevuti:");
        System.out.println("Nome: " + nome);
        System.out.println("Cognome: " + cognome);
        System.out.println("Numero Carta: " + numeroCarta);
        System.out.println("Scadenza: " + scadenza);
        System.out.println("CVV: " + cvv);
        System.out.println("Proiezione ID: " + proiezioneId);
        System.out.println("Posti selezionati: " + posti);
        System.out.println("Totale: €" + totale);

        request.setAttribute("errorMessage", "Checkout completato con successo!");
        request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
    }
}

