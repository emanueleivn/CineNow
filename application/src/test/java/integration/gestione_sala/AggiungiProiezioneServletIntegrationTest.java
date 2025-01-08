package integration.gestione_sala;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.sottosistemi.gestione_sala.view.AggiungiProiezioneServlet;
import it.unisa.application.sottosistemi.gestione_sede.view.ProiezioniFilmServlet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unit.test_DAO.DatabaseSetupForTest;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AggiungiProiezioneServletIntegrationTest {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;
    private AggiungiProiezioneServlet servlet;
    @BeforeAll
    static void globalSetup() {
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);
        servlet = new AggiungiProiezioneServlet();
        when(request.getSession()).thenReturn(session);
    }

    private void invokeDoGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Method doPost = AggiungiProiezioneServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doPost.setAccessible(true);
        doPost.invoke(servlet,request, response);
    }

    @Test
    void testAggiungiProiezioneServletDoGet() throws Exception {
        try (Connection conn = DataSourceSingleton.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM film;");
            stmt.execute("DELETE FROM sala;");
            stmt.execute("DELETE FROM sede;");
            stmt.execute("INSERT INTO sede (id, nome, via, città, cap) VALUES (1, 'Sede Test', 'Via Test', 'Test City', '12345');");
            stmt.execute("INSERT INTO film (id, titolo, durata, genere, classificazione, descrizione, is_proiettato) VALUES (1, 'Film Test', 120, 'Azione', 'PG', 'Test Description', true);");
            stmt.execute("INSERT INTO sala (id, numero, capienza, id_sede) VALUES (1, 1, 100, 1);");
        }
        System.out.println("Dati di Test: sedeId=1, filmId=1, salaId=1");
        when(request.getParameter("sedeId")).thenReturn("1");
        when(request.getRequestDispatcher("/WEB-INF/jsp/aggiungiProiezione.jsp")).thenReturn(dispatcher);
        invokeDoGet(request, response);
        verify(request).setAttribute(eq("sedeId"), eq(1));
        verify(dispatcher).forward(request, response);
    }
}