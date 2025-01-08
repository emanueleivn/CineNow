package integration.gestione_sede;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.sottosistemi.gestione_sede.view.CatalogoSedeServlet;
import it.unisa.application.sottosistemi.gestione_sede.view.GestioneProgrammazioneServlet;
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

public class GestioneProgrammazioneServletIntegrationTest {
    private GestioneProgrammazioneServlet servlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private RequestDispatcher dispatcherMock;

    @BeforeAll
    static void setupDatabase() {
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        servlet = new GestioneProgrammazioneServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        dispatcherMock = mock(RequestDispatcher.class);
        populateDatabase();
    }
    private void invokeDoGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Method doGetMethod = GestioneProgrammazioneServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(servlet, request, response);
    }
    private void populateDatabase() {
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             Statement statement = connection.createStatement()) {
            String dataInsertScript = """
                DELETE FROM proiezione;
                DELETE FROM film;
                DELETE FROM sala;
                DELETE FROM sede;
                DELETE FROM slot;

                INSERT INTO sede (id, nome, via, città, cap) VALUES 
                (1, 'Cinema Centrale', 'Via Roma', 'Napoli', '80100');

                INSERT INTO sala (id, id_sede, numero, capienza) VALUES 
                (1, 1, 1, 100);

                INSERT INTO film (id, titolo, genere, classificazione, durata, descrizione, is_proiettato) VALUES 
                (1, 'Avatar', 'Sci-fi', 'T', 180, 'Film di fantascienza', TRUE);

                INSERT INTO slot (id, ora_inizio) VALUES 
                (1, '15:00:00');

                INSERT INTO proiezione (id, data, id_film, id_sala, id_orario) VALUES 
                (1, '2025-01-10', 1, 1, 1);
            """;
            statement.execute(dataInsertScript);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante il popolamento del database di test", e);
        }
    }

    @Test
    void testDoGetWithValidSedeId() throws Exception {
        when(requestMock.getParameter("sedeId")).thenReturn("1");
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/gestioneProgrammazione.jsp")).thenReturn(dispatcherMock);
        invokeDoGet(requestMock, responseMock);
        verify(requestMock).setAttribute(eq("programmazioni"), anyList());
        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("Programmazione caricata correttamente per sede ID=1.");
    }

    @Test
    void testDoGetWithInvalidSedeId() throws Exception {
        System.out.println("Sede di test. sedeId=abc.");
        when(requestMock.getParameter("sedeId")).thenReturn("abc");
        invokeDoGet(requestMock, responseMock);
        verify(responseMock).sendError(HttpServletResponse.SC_BAD_REQUEST, "Parametro sedeId non valido.");
        System.out.println("Gestione corretta di sedeId non valido.");
    }

    @Test
    void testDoGetWithException() throws Exception {
        when(requestMock.getParameter("sedeId")).thenReturn("1");
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/gestioneProgrammazione.jsp")).thenThrow(new RuntimeException("Errore simulato"));
        invokeDoGet(requestMock, responseMock);
        verify(responseMock).sendError(eq(HttpServletResponse.SC_INTERNAL_SERVER_ERROR), anyString());
    }
}
