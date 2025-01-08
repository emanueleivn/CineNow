package integration.gestione_prenotazione;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.Cliente;
import it.unisa.application.sottosistemi.gestione_prenotazione.view.AggiungiOrdineServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import unit.test_DAO.DatabaseSetupForTest;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Statement;

import static org.mockito.Mockito.*;

public class AggiungiOrdineServletIntegrationTest {
    private AggiungiOrdineServlet servlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private HttpSession sessionMock;
    private RequestDispatcher dispatcherMock;

    @BeforeAll
    static void setupDatabase() {
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        servlet = new AggiungiOrdineServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
        dispatcherMock = mock(RequestDispatcher.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(requestMock.getRequestDispatcher(anyString())).thenReturn(dispatcherMock);

        populateDatabase();
    }

    private void invokeDoPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Method doPost = AggiungiOrdineServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPost.setAccessible(true);
        doPost.invoke(servlet, request, response);
    }

    private void populateDatabase() {
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             Statement statement = connection.createStatement()) {
            String dataInsertScript = """
                        DELETE FROM occupa;
                        DELETE FROM prenotazione;
                        DELETE FROM posto_proiezione;
                        DELETE FROM proiezione;
                        DELETE FROM posto;
                        DELETE FROM sala;
                        DELETE FROM film;
                        DELETE FROM slot;
                        DELETE FROM cliente;
                        DELETE FROM utente;
                        DELETE FROM sede;
                    
                        INSERT INTO utente (email, password, ruolo) VALUES ('test@example.com', 'password', 'cliente');
                        INSERT INTO cliente (email, nome, cognome) VALUES ('test@example.com', 'Mario', 'Rossi');
                    
                        INSERT INTO sede (id, nome, via, città, cap) VALUES (1, 'Cinema Centrale','Via Roma', 'Napoli', '80100');
                        INSERT INTO sala (id, id_sede,numero, capienza) VALUES (1, 1, 1, 100);
                        INSERT INTO film (id, titolo, genere,classificazione, durata, descrizione, is_proiettato)
                        VALUES(1, 'Avatar', 'Sci-fi', 'T', 180, 'Film di fantascienza',TRUE);
                        INSERT INTO slot (id, ora_inizio) VALUES (1, '15:00:00');
                        INSERT INTO proiezione (id, data, id_film, id_sala, id_orario)
                        VALUES (1,'2025-01-10', 1, 1, 1);
                        INSERT INTO posto (id_sala, fila, numero) VALUES (1, 'A', 1), (1, 'A', 2);
                        INSERT INTO posto_proiezione (id_sala, fila, numero, id_proiezione, stato) VALUES (1, 'A', 1, 1, TRUE), (1, 'A', 2, 1, TRUE);
                    """;
            statement.execute(dataInsertScript);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante il popolamento del database di test", e);
        }
    }

    @Test
    void testDoPostSuccess() throws Exception {
        when(requestMock.getParameter("proiezioneId")).thenReturn("1");
        when(requestMock.getParameter("posti")).thenReturn("A-1,A-2");
        when(requestMock.getParameter("nomeCarta")).thenReturn("Mario Rossi");
        when(requestMock.getParameter("numeroCarta")).thenReturn("4111111111111111");
        when(requestMock.getParameter("scadenzaCarta")).thenReturn("12/25");
        when(requestMock.getParameter("cvv")).thenReturn("123");
        Cliente cliente = new Cliente("test@example.com", "password", "Mario", "Rossi");
        when(sessionMock.getAttribute("cliente")).thenReturn(cliente);
        invokeDoPost(requestMock, responseMock);
        verify(responseMock).sendRedirect(contains("/storicoOrdini"));
    }

    @Test
    void testDoPostInvalidProiezione() throws Exception {
        System.out.println("Test proiezione non valida. proiezioneId=999");
        when(requestMock.getParameter("proiezioneId")).thenReturn("999");
        when(requestMock.getParameter("posti")).thenReturn("A-1,A-2");
        when(requestMock.getParameter("nomeCarta")).thenReturn("Mario Rossi");
        when(requestMock.getParameter("numeroCarta")).thenReturn("4111111111111111");
        when(requestMock.getParameter("scadenzaCarta")).thenReturn("12/25");
        when(requestMock.getParameter("cvv")).thenReturn("123");
        Cliente cliente = new Cliente("test@example.com", "password", "Mario", "Rossi");
        when(sessionMock.getAttribute("cliente")).thenReturn(cliente);
        invokeDoPost(requestMock, responseMock);
        verify(requestMock).setAttribute(eq("errorMessage"), eq("Errore: Proiezione non trovata."));
        verify(dispatcherMock).forward(requestMock, responseMock);
    }

    @Test
    void testDoPostNoSessionCliente() throws Exception {
        System.out.println("Test cliente non presente nella sessione. session.getAttribute(\"cliente\")=null");
        when(requestMock.getParameter("proiezioneId")).thenReturn("1");
        when(requestMock.getParameter("posti")).thenReturn("A-1,A-2");
        when(requestMock.getParameter("nomeCarta")).thenReturn("Mario Rossi");
        when(requestMock.getParameter("numeroCarta")).thenReturn("4111111111111111");
        when(requestMock.getParameter("scadenzaCarta")).thenReturn("12/25");
        when(requestMock.getParameter("cvv")).thenReturn("123");
        when(sessionMock.getAttribute("cliente")).thenReturn(null);
        invokeDoPost(requestMock, responseMock);
        verify(requestMock).setAttribute(eq("errorMessage"), eq("Errore generico"));
        verify(dispatcherMock).forward(requestMock, responseMock);
        verifyNoMoreInteractions(responseMock);
    }
}
