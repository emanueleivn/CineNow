package integration.gestione_catena;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.sottosistemi.gestione_catena.service.CatalogoService;
import it.unisa.application.sottosistemi.gestione_catena.view.VisualizzaCatalogoServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import unit.test_DAO.DatabaseSetupForTest;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

class VisualizzaCatalogoServletTest {
    private VisualizzaCatalogoServlet visualizzaCatalogoServlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private RequestDispatcher dispatcherMock;
    private CatalogoService catalogoService;

    @BeforeAll
    static void setupDatabase() {
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        visualizzaCatalogoServlet = new VisualizzaCatalogoServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        dispatcherMock = mock(RequestDispatcher.class);
        catalogoService = new CatalogoService();
        try {
            var field = VisualizzaCatalogoServlet.class.getDeclaredField("catalogoService");
            field.setAccessible(true);
            field.set(visualizzaCatalogoServlet, catalogoService);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante l'iniezione del CatalogoService", e);
        }
        try (Connection conn = DataSourceSingleton.getInstance().getConnection()) {
            conn.createStatement().execute("DELETE FROM film;");
            conn.createStatement().executeUpdate(
                    "INSERT INTO film (id, titolo, durata, descrizione, genere, classificazione, is_proiettato) VALUES " +
                            "(1, 'Film Test 1', 120, 'Descrizione Film Test 1', 'Azione', 'PG-13', FALSE), " +
                            "(2, 'Film Test 2', 100, 'Descrizione Film Test 2', 'Commedia', 'R', FALSE);"
            );

        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la configurazione iniziale del database", e);
        }
    }

    @Test
    void testDoGetWithFilms() throws ServletException, IOException {
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/catalogo.jsp")).thenReturn(dispatcherMock);
        System.out.println("Database inizializzato con 2 film.");
        visualizzaCatalogoServlet.doGet(requestMock, responseMock);
        verify(requestMock).setAttribute(eq("catalogo"), anyList());
        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("Forward corretto verso il catalogo, con due film ottenuti.");
    }

    @Test
    void testDoGetWithoutFilms() throws ServletException, IOException {
        try (Connection conn = DataSourceSingleton.getInstance().getConnection()) {
            conn.createStatement().execute("DELETE FROM film;");
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la pulizia dei dati nel database", e);
        }
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/error.jsp")).thenReturn(dispatcherMock);
        System.out.println("Database vuoto");
        visualizzaCatalogoServlet.doGet(requestMock, responseMock);
        verify(requestMock).setAttribute(eq("errorMessage"), eq("Nessun film trovato."));
        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("Forward corretto, verso la pagina di errore");
    }
}
