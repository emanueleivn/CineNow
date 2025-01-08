package integration.gestione_sede;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.sottosistemi.gestione_sede.view.ProiezioniFilmServlet;
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

public class ProiezioniFilmServletIntegrationTest {
    private ProiezioniFilmServlet servlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private RequestDispatcher dispatcherMock;

    @BeforeAll
    static void setupDatabase() {
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        servlet = new ProiezioniFilmServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        dispatcherMock = mock(RequestDispatcher.class);
        populateDatabase();
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

                INSERT INTO film (id, titolo, genere, classificazione, durata, descrizione, is_proiettato) VALUES 
                (1, 'Avatar', 'Sci-fi', 'T', 180, 'Film di fantascienza', TRUE);

                INSERT INTO sala (id, id_sede, numero, capienza) VALUES 
                (1, 1, 1, 100);

                INSERT INTO slot (id, ora_inizio) VALUES 
                (1, '15:00:00');

                INSERT INTO proiezione (id, data, id_film, id_sala, id_orario) VALUES 
                (1, '2025-01-10', 1, 1, 1);
            """;
            statement.execute(dataInsertScript);
            System.out.println("Database popolato con i dati iniziali.");
        } catch (Exception e) {
            throw new RuntimeException("Errore durante il popolamento del database di test", e);
        }
    }

    private void invokeDoPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Method doPost = ProiezioniFilmServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPost.setAccessible(true);
        doPost.invoke(servlet, request, response);
    }

    @Test
    void testDoPostWithValidFilmAndSede() throws Exception {
        System.out.println("Sede e film di test. sede ID=1 e film ID=1.");
        when(requestMock.getParameter("sedeId")).thenReturn("1");
        when(requestMock.getParameter("filmId")).thenReturn("1");
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/proiezioniFilm.jsp")).thenReturn(dispatcherMock);
        invokeDoPost(requestMock, responseMock);
        verify(requestMock).setAttribute(eq("programmazioneFilm"), anyList());
        verify(requestMock).setAttribute(eq("filmNome"), eq("Avatar"));
        verify(requestMock).setAttribute(eq("sedeNome"), eq("Cinema Centrale"));
        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("Proiezioni film caricate correttamente.");
    }

    @Test
    void testDoPostWithInvalidFilmOrSede() throws Exception {
        System.out.println("Sede e film di test. sedeId=99 e filmId=99.");
        when(requestMock.getParameter("sedeId")).thenReturn("99");
        when(requestMock.getParameter("filmId")).thenReturn("99");
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/error.jsp")).thenReturn(dispatcherMock);
        invokeDoPost(requestMock, responseMock);
        verify(requestMock).setAttribute(eq("errorMessage"), eq("Film o sede non trovati."));
        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("Gestione corretta per filmId e sedeId non presenti nel DB.");
    }

    @Test
    void testDoPostWithNoProiezioni() throws Exception {
        System.out.println("Sede e film di test. sede ID=1 e film ID=99.");
        when(requestMock.getParameter("sedeId")).thenReturn("1");
        when(requestMock.getParameter("filmId")).thenReturn("99");
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/error.jsp")).thenReturn(dispatcherMock);
        invokeDoPost(requestMock, responseMock);
        verify(requestMock).setAttribute(eq("errorMessage"), eq("Film o sede non trovati."));
        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("Gestione corretta per filmId non presente nel DB.");
    }

    @Test
    void testDoPostWithInvalidParameters() throws Exception {
        System.out.println("Sede e film di test. sedeId=abc e filmId=def.");
        when(requestMock.getParameter("sedeId")).thenReturn("abc");
        when(requestMock.getParameter("filmId")).thenReturn("def");
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/error.jsp")).thenReturn(dispatcherMock);
        invokeDoPost(requestMock, responseMock);
        verify(requestMock).setAttribute(eq("errorMessage"), eq("Parametri non validi."));
        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("Gestione corretta di parametri non validi.");
    }
}
