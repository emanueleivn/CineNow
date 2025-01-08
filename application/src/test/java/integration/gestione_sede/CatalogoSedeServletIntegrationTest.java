package integration.gestione_sede;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.sottosistemi.gestione_sede.view.CatalogoSedeServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unit.test_DAO.DatabaseSetupForTest;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Statement;

import static org.mockito.Mockito.*;

public class CatalogoSedeServletIntegrationTest {
    private CatalogoSedeServlet servlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private RequestDispatcher dispatcherMock;

    @BeforeAll
    static void setupDatabase() {
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        servlet = new CatalogoSedeServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        dispatcherMock = mock(RequestDispatcher.class);
        populateDatabase();
    }

    private void populateDatabase() {
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             Statement statement = connection.createStatement()) {
            String dataInsertScript = """
            DELETE FROM film;
            DELETE FROM sede;

            INSERT INTO sede (id, nome, via, città, cap) VALUES 
            (1, 'Mercogliano', 'Via Roma', 'Avellino', '83013'),
            (2, 'Laquila', 'Via Abruzzo', 'L''Aquila', '67100');

            INSERT INTO film (id, titolo, genere, classificazione, durata, descrizione, is_proiettato) VALUES
            (1, 'Avatar', 'Sci-fi', 'T', 180, 'Film di fantascienza', TRUE),
            (2, 'Inception', 'Thriller', 'T', 148, 'Film sui sogni', TRUE);
        """;
            statement.execute(dataInsertScript);
            System.out.println("Database popolato con i dati iniziali.");
        } catch (Exception e) {
            throw new RuntimeException("Errore durante il popolamento del database di test", e);
        }
    }

    private void invokeDoGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Method doGetMethod = CatalogoSedeServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(servlet, request, response);
    }

    @Test
    void testDoGetWithValidSedeMercogliano() throws Exception {
        when(requestMock.getParameter("sede")).thenReturn("Mercogliano");
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/catalogoSede.jsp")).thenReturn(dispatcherMock);

        invokeDoGet(requestMock, responseMock);

        verify(requestMock).setAttribute(eq("sede"), eq("Mercogliano"));
        verify(requestMock).setAttribute(eq("sedeId"), eq(1));
        verify(requestMock).setAttribute(eq("catalogo"), anyList());
        verify(dispatcherMock).forward(requestMock, responseMock);

        System.out.println("Test completato: catalogo caricato correttamente per sede 'Mercogliano'.");
    }

    @Test
    void testDoGetWithValidSedeLaquila() throws Exception {
        System.out.println("Sede di test: idSede=1");
        when(requestMock.getParameter("sede")).thenReturn("Laquila");
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/catalogoSede.jsp")).thenReturn(dispatcherMock);
        invokeDoGet(requestMock, responseMock);
        verify(requestMock).setAttribute(eq("sede"), eq("L'Aquila"));
        verify(requestMock).setAttribute(eq("sedeId"), eq(2));
        verify(requestMock).setAttribute(eq("catalogo"), anyList());
        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("Catalogo caricato correttamente per sede 'L'Aquila'.");
    }

    @Test
    void testDoGetWithInvalidSede() throws Exception {
        System.out.println("Sede di test: idSede=invalid");
        when(requestMock.getParameter("sede")).thenReturn("InvalidSede");
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/error.jsp")).thenReturn(dispatcherMock);
        invokeDoGet(requestMock, responseMock);
        verify(requestMock).setAttribute(eq("errorMessage"), eq("Errore caricamento catalogo"));
        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("Forward a pagina di errore.");
    }

    @Test
    void testDoGetWithNullSede() throws Exception {
        System.out.println("Sede di test: idSede=null");
        when(requestMock.getParameter("sede")).thenReturn(null);
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/error.jsp")).thenReturn(dispatcherMock);
        invokeDoGet(requestMock, responseMock);
        verify(requestMock).setAttribute(eq("errorMessage"), eq("Errore caricamento catalogo: sede non specificata"));
        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("Forward a pagina di errore");
    }

}
