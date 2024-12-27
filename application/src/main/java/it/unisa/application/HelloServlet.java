package it.unisa.application;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import javax.sql.DataSource;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private DataSource dataSource;
    private String message;

    @Override
    public void init() throws ServletException {
        // Imposta un messaggio di benvenuto
        message = "Elenco Utenti";

        // Recupera il DataSource dal ServletContext
        ServletContext context = getServletContext();
        dataSource = (DataSource) context.getAttribute("DataSource");

        if (dataSource == null) {
            throw new ServletException("DataSource non trovato nel contesto");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Imposta il tipo di contenuto della risposta
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            // Inizia la pagina HTML
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>" + escapeHtml(message) + "</title>");
            out.println("<style>");
            out.println("table { border-collapse: collapse; width: 60%; margin: 20px auto; }");
            out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: center; }");
            out.println("th { background-color: #f2f2f2; }");
            out.println("h1 { text-align: center; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>" + escapeHtml(message) + "</h1>");
            out.println("<table>");
            out.println("<tr><th>Email</th><th>Ruolo</th></tr>");

            // Query SQL per recuperare gli utenti
            String sql = "SELECT email, ruolo FROM utente";

            // Utilizza try-with-resources per garantire la chiusura delle risorse
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    String email = rs.getString("email");
                    String ruolo = rs.getString("ruolo");

                    out.println("<tr>");
                    out.println("<td>" + escapeHtml(email) + "</td>");
                    out.println("<td>" + escapeHtml(ruolo) + "</td>");
                    out.println("</tr>");
                }

            } catch (SQLException e) {
                // Gestione degli errori SQL
                out.println("<tr><td colspan='2'>Errore nel recupero dei dati: " + escapeHtml(e.getMessage()) + "</td></tr>");
                e.printStackTrace();
            }

            // Chiude la tabella e la pagina HTML
            out.println("</table>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    /**
     * Metodo semplice per prevenire attacchi XSS.
     */
    private String escapeHtml(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}
