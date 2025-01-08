package it.unisa.application.sottosistemi.gestione_catena.view;

import it.unisa.application.sottosistemi.gestione_catena.service.CatalogoService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@WebServlet("/addFilm")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50) // 50MB
public class AddFilmServlet extends HttpServlet {
    private static final String UPLOAD_DIR = "images";
    private final CatalogoService catalogoService = new CatalogoService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/aggiungiFilm.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String titolo = request.getParameter("titolo");
            int durata = Integer.parseInt(request.getParameter("durata"));
            String descrizione = request.getParameter("descrizione");
            String genere = request.getParameter("genere");
            String classificazione = request.getParameter("classificazione");
            Part filePart = request.getPart("locandina");

            if (filePart == null || filePart.getSize() == 0) {
                throw new IllegalArgumentException("La locandina è obbligatoria.");
            }


            byte[] locandinaBytes;
            try (InputStream fileContent = filePart.getInputStream()) {
                locandinaBytes = fileContent.readAllBytes();
            }


            catalogoService.addFilmCatalogo(titolo, durata, descrizione, locandinaBytes, genere, classificazione);

            response.sendRedirect(request.getContextPath() + "/catalogo");
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        }
    }


}
