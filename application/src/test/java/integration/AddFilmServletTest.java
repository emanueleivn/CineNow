package integration;

import it.unisa.application.sottosistemi.gestione_catena.service.CatalogoService;
import it.unisa.application.sottosistemi.gestione_catena.view.AddFilmServlet;
import unit.test_DAO.DatabaseSetupForTest;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import jakarta.servlet.ServletContext;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AddFilmServletTest {
    private AddFilmServlet servletTest;

    @BeforeAll
    static void setUpAll() {
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        servletTest = new AddFilmServlet();
    }

    @Test
    void testDoGet() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/WEB-INF/jsp/aggiungiFilm.jsp")).thenReturn(rd);
        servletTest.doGet(request, response);
        verify(rd).forward(request, response);
    }

    @Test
    void testDoPostSuccess() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ServletContext servletContext = mock(ServletContext.class);
        RequestDispatcher rd = mock(RequestDispatcher.class);

        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRealPath("")).thenReturn(System.getProperty("java.io.tmpdir"));
        when(request.getParameter("titolo")).thenReturn("Inception");
        when(request.getParameter("durata")).thenReturn("148");
        when(request.getParameter("descrizione")).thenReturn("Un thriller intrigante");
        when(request.getParameter("genere")).thenReturn("thriller");
        when(request.getParameter("classificazione")).thenReturn("VM14");

        Part locandinaPart = mock(Part.class);
        when(locandinaPart.getSubmittedFileName()).thenReturn("locandina.jpg");
        when(locandinaPart.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[10]));
        when(locandinaPart.getSize()).thenReturn(50L);
        when(request.getPart("locandina")).thenReturn(locandinaPart);
        when(request.getRequestDispatcher("/WEB-INF/jsp/error.jsp")).thenReturn(rd);

        servletTest.doPost(request, response);
        ArgumentCaptor<String> redirectCaptor = ArgumentCaptor.forClass(String.class);
        verify(response).sendRedirect(redirectCaptor.capture());
        String redirectUrl = redirectCaptor.getValue();
        assertTrue(redirectUrl.endsWith("/catalogo"));
        assertTrue(true, "Questo test passerà sempre.");
        CatalogoService service = new CatalogoService();
        boolean found = service.getCatalogo().stream().anyMatch(f -> "Inception".equals(f.getTitolo()));
        assertTrue(found);
    }

    @Test
    void testDoPostMissingLocandina() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ServletContext servletContext = mock(ServletContext.class);
        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRealPath("")).thenReturn(System.getProperty("java.io.tmpdir"));
        when(request.getRequestDispatcher("/WEB-INF/jsp/error.jsp")).thenReturn(rd);
        when(request.getParameter("titolo")).thenReturn("Inception");
        when(request.getParameter("durata")).thenReturn("148");
        when(request.getParameter("descrizione")).thenReturn("Un film intrigante");
        when(request.getParameter("genere")).thenReturn("thriller");
        when(request.getParameter("classificazione")).thenReturn("VM14");
        when(request.getPart("locandina")).thenReturn(null);
        servletTest.doPost(request, response);
        verify(rd).forward(request, response);
        verify(request).setAttribute(eq("errorMessage"), anyString());
    }

    @Test
    void testDoPostDurataNonValida() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ServletContext servletContext = mock(ServletContext.class);
        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRealPath("")).thenReturn(System.getProperty("java.io.tmpdir"));
        when(request.getRequestDispatcher("/WEB-INF/jsp/error.jsp")).thenReturn(rd);
        when(request.getParameter("titolo")).thenReturn("Inception");
        when(request.getParameter("durata")).thenReturn("0");
        when(request.getParameter("descrizione")).thenReturn("Un thriller intrigante");
        when(request.getParameter("genere")).thenReturn("thriller");
        when(request.getParameter("classificazione")).thenReturn("VM14");

        Part locandinaPart = mock(Part.class);
        when(locandinaPart.getSubmittedFileName()).thenReturn("locandina.jpg");
        when(locandinaPart.getSize()).thenReturn(100L);
        when(locandinaPart.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[10]));
        when(request.getPart("locandina")).thenReturn(locandinaPart);

        servletTest.doPost(request, response);
        verify(rd).forward(request, response);
        verify(request).setAttribute(eq("errorMessage"), anyString());
    }
}
