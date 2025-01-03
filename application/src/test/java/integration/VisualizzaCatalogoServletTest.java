package integration;

import it.unisa.application.sottosistemi.gestione_catena.view.VisualizzaCatalogoServlet;
import it.unisa.application.sottosistemi.gestione_catena.service.CatalogoService;
import it.unisa.application.model.entity.Film;
import unit.test_DAO.DatabaseSetupForTest;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VisualizzaCatalogoServletTest {

    private VisualizzaCatalogoServlet servletTest;

    @BeforeAll
    static void setUpAll() {
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        servletTest = new VisualizzaCatalogoServlet();
    }

    @Test
    void testDoGetNoFilms() throws ServletException, IOException {
        CatalogoService service = new CatalogoService();
        service.getCatalogo().forEach(f -> {});
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/WEB-INF/jsp/error.jsp")).thenReturn(rd);
        servletTest.doGet(request, response);
        verify(rd).forward(request, response);
        verify(request).setAttribute(eq("errorMessage"), eq("Nessun film trovato."));
    }

    @Test
    void testDoGetWithFilms() throws ServletException, IOException {
        CatalogoService service = new CatalogoService();
        byte[] locandina = "Esempio di locandina".getBytes(); // Mock della locandina come byte[]
        service.addFilmCatalogo("Inception", 148, "Un thriller spettacolare", locandina, "thriller", "VM14");

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher rd = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher("/WEB-INF/jsp/catalogo.jsp")).thenReturn(rd);

        servletTest.doGet(request, response);

        verify(rd).forward(request, response);

        ArgumentCaptor<String> attrNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> attrValueCaptor = ArgumentCaptor.forClass(Object.class);

        verify(request).setAttribute(attrNameCaptor.capture(), attrValueCaptor.capture());

        assertEquals("catalogo", attrNameCaptor.getValue());

        List<Film> films = (List<Film>) attrValueCaptor.getValue();

        assertTrue(films.stream().anyMatch(f -> "Inception".equals(f.getTitolo())));
        assertTrue(films.stream().anyMatch(f -> Arrays.equals(locandina, f.getLocandina())));
    }

}
