package integration.gestione_catena;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.sottosistemi.gestione_catena.service.CatalogoService;
import it.unisa.application.sottosistemi.gestione_catena.view.AddFilmServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import unit.test_DAO.DatabaseSetupForTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

class AddFilmServletIntegrationTest {

    private AddFilmServlet addFilmServlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private RequestDispatcher dispatcherMock;
    private Part filePartMock;
    private CatalogoService catalogoService;

    @BeforeAll
    static void setupDatabase() {
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        addFilmServlet = new AddFilmServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        dispatcherMock = mock(RequestDispatcher.class);
        filePartMock = mock(Part.class);
        catalogoService = new CatalogoService();

        try {
            var field = AddFilmServlet.class.getDeclaredField("catalogoService");
            field.setAccessible(true);
            field.set(addFilmServlet, catalogoService);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante l'iniezione del CatalogoService", e);
        }

        try (Connection conn = DataSourceSingleton.getInstance().getConnection()) {
            conn.createStatement().execute("DELETE FROM film;");
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la configurazione iniziale del database", e);
        }
    }

    @Test
    void testDoGet() throws ServletException, IOException {
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/aggiungiFilm.jsp")).thenReturn(dispatcherMock);
        addFilmServlet.doGet(requestMock, responseMock);
        verify(dispatcherMock).forward(requestMock, responseMock);
    }

    @Test
    void testDoPostSuccess() throws ServletException, IOException {
        when(requestMock.getParameter("titolo")).thenReturn("Film Test");
        when(requestMock.getParameter("durata")).thenReturn("120");
        when(requestMock.getParameter("descrizione")).thenReturn("Descrizione Test");
        when(requestMock.getParameter("genere")).thenReturn("Azione");
        when(requestMock.getParameter("classificazione")).thenReturn("PG-13");

        when(requestMock.getPart("locandina")).thenReturn(filePartMock);
        byte[] fileContent = "file content".getBytes();
        when(filePartMock.getSize()).thenReturn((long) fileContent.length);
        when(filePartMock.getInputStream()).thenReturn(new ByteArrayInputStream(fileContent));
        addFilmServlet.doPost(requestMock, responseMock);
        verify(responseMock).sendRedirect(requestMock.getContextPath() + "/catalogo");
    }

    @Test
    void testDoPostFailureMissingFile() throws ServletException, IOException {
        when(requestMock.getParameter("titolo")).thenReturn("");
        when(requestMock.getParameter("durata")).thenReturn("120");
        when(requestMock.getParameter("descrizione")).thenReturn("Descrizione Test");
        when(requestMock.getParameter("genere")).thenReturn("Azione");
        when(requestMock.getParameter("classificazione")).thenReturn("PG-13");
        when(requestMock.getPart("locandina")).thenReturn(null);
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/error.jsp")).thenReturn(dispatcherMock);
        addFilmServlet.doPost(requestMock, responseMock);
        verify(requestMock).setAttribute(eq("errorMessage"), anyString());
        verify(dispatcherMock).forward(requestMock, responseMock);
    }

    @Test
    void testDoPostFailureInvalidData() throws ServletException, IOException {
        when(requestMock.getParameter("titolo")).thenReturn("");
        when(requestMock.getParameter("durata")).thenReturn("-10");
        when(requestMock.getParameter("descrizione")).thenReturn("");
        when(requestMock.getParameter("genere")).thenReturn("Non valido");
        when(requestMock.getParameter("classificazione")).thenReturn("");
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/error.jsp")).thenReturn(dispatcherMock);
        addFilmServlet.doPost(requestMock, responseMock);
        verify(requestMock).setAttribute(eq("errorMessage"), anyString());
        verify(dispatcherMock).forward(requestMock, responseMock);
    }

    @Test
    void testDoPostFailureInvalidDuration() throws ServletException, IOException {
        when(requestMock.getParameter("titolo")).thenReturn("Film Test");
        when(requestMock.getParameter("durata")).thenReturn("invalid");
        when(requestMock.getParameter("descrizione")).thenReturn("Descrizione Test");
        when(requestMock.getParameter("genere")).thenReturn("Azione");
        when(requestMock.getParameter("classificazione")).thenReturn("PG-13");

        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/error.jsp")).thenReturn(dispatcherMock);

        addFilmServlet.doPost(requestMock, responseMock);

        verify(requestMock).setAttribute(eq("errorMessage"), anyString());
        verify(dispatcherMock).forward(requestMock, responseMock);
    }
}
