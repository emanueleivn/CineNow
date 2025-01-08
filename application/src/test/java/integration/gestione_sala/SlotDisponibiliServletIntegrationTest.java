package integration.gestione_sala;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.sottosistemi.gestione_sala.view.SlotDisponibiliServlet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unit.test_DAO.DatabaseSetupForTest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Statement;

import static org.mockito.Mockito.*;

public class SlotDisponibiliServletIntegrationTest {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private SlotDisponibiliServlet servlet;
    @BeforeAll
    static void globalSetup() {
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        servlet = new SlotDisponibiliServlet();
    }
    private void invokeDoGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Method doPost = SlotDisponibiliServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doPost.setAccessible(true);
        doPost.invoke(servlet,request, response);
    }
    @Test
    void testSlotDisponibiliServletDoGet() throws Exception {
        try (Connection conn = DataSourceSingleton.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM film;");
            stmt.execute("DELETE FROM sala;");
            stmt.execute("DELETE FROM slot;");
            stmt.execute("DELETE FROM sede;");

            stmt.execute("INSERT INTO sede (id, nome, via, città, cap) VALUES (1, 'Sede Test', 'Via Roma', 'Napoli', '80100');");
            stmt.execute("INSERT INTO film (id, titolo, durata, genere, classificazione, descrizione, is_proiettato) VALUES (1, 'Film Test', 120, 'Azione', 'PG', 'Test Description', true);");
            stmt.execute("INSERT INTO sala (id, numero, capienza, id_sede) VALUES (1, 1, 100, 1);");
            stmt.execute("INSERT INTO slot (id, ora_inizio) VALUES (1, '18:00:00');");
            stmt.execute("INSERT INTO slot (id, ora_inizio) VALUES (2, '20:00:00');");
        }
        System.out.println("Dati di Test: filmId=1, salaId=1, dataInizio=2025-01-01, dataFine=2025-01-02");
        when(request.getParameter("filmId")).thenReturn("1");
        when(request.getParameter("salaId")).thenReturn("1");
        when(request.getParameter("dataInizio")).thenReturn("2025-01-01");
        when(request.getParameter("dataFine")).thenReturn("2025-01-02");
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
        invokeDoGet(request, response);
        verify(response).setContentType("application/json");
        verify(writer).print(anyString());
    }
}
