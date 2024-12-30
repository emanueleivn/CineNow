package it.unisa.application.sottosistemi.gestione_catena.view;

import it.unisa.application.sottosistemi.gestione_catena.service.CatalogoService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@WebServlet("/addFilm")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50) // 50MB
public class AddFilmServlet extends HttpServlet {
    private static final String UPLOAD_DIR = "images";

    private final CatalogoService catalogoService = new CatalogoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/aggiungiFilm.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String applicationPath = getServletContext().getRealPath("");
            String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;

            File uploadDir = new File(uploadFilePath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String sanitizedFileName = UUID.randomUUID() + "_" + fileName.replaceAll("\\s+", "_");
            String filePath = uploadFilePath + File.separator + sanitizedFileName;
            Files.copy(filePart.getInputStream(), Paths.get(filePath));

            String relativePath = UPLOAD_DIR + "/" + sanitizedFileName;

            catalogoService.addFilmCatalogo(titolo, durata, descrizione, relativePath, genere, classificazione);
            response.sendRedirect(request.getContextPath() + "/catalogo");
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        }
    }
}
