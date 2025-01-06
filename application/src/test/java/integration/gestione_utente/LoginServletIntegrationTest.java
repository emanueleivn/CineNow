package integration.gestione_utente;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.Utente;
import it.unisa.application.sottosistemi.gestione_utente.view.LoginServlet;
import it.unisa.application.utilities.PasswordHash;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import unit.test_DAO.DatabaseSetupForTest;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class LoginServletIntegrationTest {

    private LoginServlet loginServlet;
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
        loginServlet = new LoginServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
        dispatcherMock = mock(RequestDispatcher.class);

        loginServlet.init();

        String hashedPassword = PasswordHash.hash("hashedPassword");

        try (Connection conn = DataSourceSingleton.getInstance().getConnection()) {
            conn.createStatement().execute("DELETE FROM cliente;");
            conn.createStatement().execute("DELETE FROM utente;");
            conn.createStatement().executeUpdate(
                    "INSERT INTO utente (email, password, ruolo) VALUES ('test@example.com', '" + hashedPassword + "', 'cliente');"
            );
            conn.createStatement().executeUpdate(
                    "INSERT INTO cliente (email, nome, cognome) VALUES ('test@example.com', 'Mario', 'Rossi');"
            );
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la configurazione iniziale dei dati", e);
        }
    }

    @Test
    void testLoginClienteSuccess() throws ServletException, IOException {
        when(requestMock.getParameter("email")).thenReturn("test@example.com");
        when(requestMock.getParameter("password")).thenReturn("hashedPassword");
        when(requestMock.getSession(true)).thenReturn(sessionMock);
        loginServlet.doPost(requestMock, responseMock);
        verify(sessionMock).setAttribute(eq("cliente"), any(Utente.class));
        verify(responseMock).sendRedirect(requestMock.getContextPath() + "/Home");
    }

    @Test
    void testLoginFailure() throws ServletException, IOException {
        when(requestMock.getParameter("email")).thenReturn("invalid@example.com");
        when(requestMock.getParameter("password")).thenReturn("InvalidPassword!");
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/error.jsp")).thenReturn(dispatcherMock);
        loginServlet.doPost(requestMock, responseMock);
        verify(requestMock).setAttribute(eq("errorMessage"), anyString());
        verify(dispatcherMock).forward(requestMock, responseMock);
    }

    @Test
    void testDoGet() throws ServletException, IOException {
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/loginView.jsp")).thenReturn(dispatcherMock);
        loginServlet.doGet(requestMock, responseMock);
        verify(dispatcherMock).forward(requestMock, responseMock);
    }
}


