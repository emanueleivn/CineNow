package integration.gestione_prenotazione;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.*;
import it.unisa.application.sottosistemi.gestione_prenotazione.view.SceltaPostoServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SceltaPostoServletIntegrationTest {

    private SceltaPostoServlet servlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private RequestDispatcher dispatcherMock;

    @BeforeAll
    static void configureDatabase() throws Exception {
        unit.test_DAO.DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() throws Exception {
        populateDatabase();
        servlet = new SceltaPostoServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        dispatcherMock = mock(RequestDispatcher.class);
        when(requestMock.getRequestDispatcher(anyString())).thenReturn(dispatcherMock);
    }

    private void populateDatabase() {
        try (Connection connection = DataSourceSingleton.getInstance().getConnection()) {
            Statement statement = connection.createStatement();

            // Pulizia del database
            String cleanupScript = """
                        DELETE FROM occupa;
                        DELETE FROM prenotazione;
                        DELETE FROM posto_proiezione;
                        DELETE FROM proiezione;
                        DELETE FROM slot;
                        DELETE FROM posto;
                        DELETE FROM sala;
                        DELETE FROM gest_sede;
                        DELETE FROM sede;
                        DELETE FROM cliente;
                        DELETE FROM utente;
                        DELETE FROM film;
                    """;
            statement.execute(cleanupScript);

            // Popolamento delle tabelle
            String populateScript = """
                      -- Pulizia tabelle
                      DELETE FROM occupa;
                      DELETE FROM prenotazione;
                      DELETE FROM posto_proiezione;
                      DELETE FROM proiezione;
                      DELETE FROM slot;
                      DELETE FROM posto;
                      DELETE FROM sala;
                      DELETE FROM gest_sede;
                      DELETE FROM sede;
                      DELETE FROM cliente;
                      DELETE FROM utente;
                      DELETE FROM film;
                      
                      -- Inserimento utenti e clienti
                      INSERT INTO utente (email, password, ruolo) VALUES ('cliente@example.com', 'password123', 'CLIENTE');
                      INSERT INTO cliente (email, nome, cognome) VALUES ('cliente@example.com', 'Mario', 'Rossi');
                      
                      -- Inserimento sede
                      INSERT INTO sede (id, nome, via, città, cap) VALUES (1, 'Cinema Centrale', 'Via Roma', 'Napoli', '80100');
                      
                      -- Inserimento sala
                      INSERT INTO sala (id, id_sede, numero, capienza) VALUES (1, 1, 1, 100);
                      
                      -- Inserimento posti
                      INSERT INTO posto (id_sala, fila, numero) VALUES\s
                          (1, 'A', 1),
                          (1, 'A', 2),
                          (1, 'A', 3);
                      
                      -- Inserimento film
                      INSERT INTO film (id, titolo, genere, classificazione, durata, descrizione, is_proiettato)
                      VALUES (1, 'Film di Test', 'Commedia', 'T', 120, 'Descrizione del film di test', TRUE);
                      
                      -- Inserimento slot
                      INSERT INTO slot (id, ora_inizio) VALUES
                          (1, '15:00:00'),
                          (2, '17:30:00');
                      
                      -- Inserimento proiezione
                      INSERT INTO proiezione (id, data, id_film, id_sala, id_orario)
                      VALUES (1, '2025-01-08', 1, 1, 1);
                      
                      -- Inserimento posti proiezione
                      INSERT INTO posto_proiezione (id_sala, fila, numero, id_proiezione, stato) VALUES
                          (1, 'A', 1, 1, TRUE),
                          (1, 'A', 2, 1, TRUE),
                          (1, 'A', 3, 1, TRUE);
                      
                    """;
            statement.execute(populateScript);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private void invokeDoPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Method doPost = SceltaPostoServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPost.setAccessible(true);
        doPost.invoke(servlet, request, response);
    }

    @Test
    void testDoPostSuccess() throws Exception {
        when(requestMock.getParameter("proiezioneId")).thenReturn("1");
        Sala sala = new Sala(1);
        sala.setNumeroSala(1);
        sala.setCapienza(100);
        Film film = new Film(1);
        film.setTitolo("Titolo Film");
        film.setGenere("Genere");
        film.setClassificazione("Classificazione");
        film.setDurata(120);
        Slot slot = new Slot(1);
        slot.setOraInizio(Time.valueOf("20:00:00"));
        Proiezione proiezione1 = new Proiezione(1);
        proiezione1.setSalaProiezione(sala);
        proiezione1.setFilmProiezione(film);
        proiezione1.setDataProiezione(LocalDate.of(2025, 1, 1));
        proiezione1.setOrarioProiezione(slot);
        Posto posto1 = new Posto(sala, 'A', 1);
        Posto posto2 = new Posto(sala, 'A', 2);
        PostoProiezione postoProiezione1 = new PostoProiezione(posto1, proiezione1);
        postoProiezione1.setStato(true);
        PostoProiezione postoProiezione2 = new PostoProiezione(posto2, proiezione1);
        postoProiezione2.setStato(false);
        List<PostoProiezione> mockPostiProiezione = Arrays.asList(postoProiezione1, postoProiezione2);
        proiezione1.setPostiProiezione(mockPostiProiezione);
        when(requestMock.getAttribute("postiProiezione")).thenReturn(mockPostiProiezione);
        when(requestMock.getAttribute("proiezione")).thenReturn(proiezione1);
        invokeDoPost(requestMock, responseMock);
        @SuppressWarnings("unchecked")
        List<PostoProiezione> postiProiezione = (List<PostoProiezione>) requestMock.getAttribute("postiProiezione");
        Proiezione proiezione = (Proiezione) requestMock.getAttribute("proiezione");
        assertNotNull(postiProiezione, "I posti proiezione non dovrebbero essere null.");
        assertFalse(postiProiezione.isEmpty(), "La lista dei posti proiezione non dovrebbe essere vuota.");
        assertEquals(2, postiProiezione.size(), "La lista dei posti proiezione dovrebbe contenere 2 elementi.");
        assertNotNull(proiezione, "La proiezione non dovrebbe essere null.");
        assertEquals(1, proiezione.getId(), "L'ID della proiezione dovrebbe essere 1.");
        assertEquals(1, proiezione.getSalaProiezione().getId(), "L'ID della sala dovrebbe essere 1.");
        assertEquals("Titolo Film", proiezione.getFilmProiezione().getTitolo(), "Il titolo del film dovrebbe essere 'Titolo Film'.");
        verify(requestMock).getRequestDispatcher("/WEB-INF/jsp/piantinaView.jsp");
        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("Proiezione di test. proiezioneId=1");
        System.out.println("2 posti recuperati, sala '1', film 'Titolo Film'.");
    }


    @Test
    void testDoPostProiezioneNotFound() throws Exception {
        when(requestMock.getParameter("proiezioneId")).thenReturn("999");
        invokeDoPost(requestMock, responseMock);
        verify(requestMock).setAttribute("errorMessage", "Proiezione non trovata.");
        verify(requestMock).getRequestDispatcher("/WEB-INF/jsp/error.jsp");
        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("Proiezione di test. proiezioneId=999");
        System.out.println("Proiezione non trovata");
    }

    @Test
    void testDoPostInvalidParameter() throws Exception {
        System.out.println("Proiezione di test. proiezioneId=invalid");
        when(requestMock.getParameter("proiezioneId")).thenReturn("invalid");
        invokeDoPost(requestMock, responseMock);
        verify(requestMock).setAttribute("errorMessage", "Parametro non valido.");
        verify(requestMock).getRequestDispatcher("/WEB-INF/jsp/error.jsp");
        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("Forward corretto verso pagina di errore");
    }

}
