package integration.gestione_utente;

import it.unisa.application.sottosistemi.gestione_utente.view.LogoutServlet;
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

import static org.mockito.Mockito.*;

public class LogoutServletIntegrationTest {

    private LogoutServlet logoutServlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private HttpSession sessionMock;
    private RequestDispatcher dispatcherMock;

    @BeforeAll
    static void setupDatabase() {
        DatabaseSetupForTest.configureH2DataSource();
        System.out.println("Setup del database completato.");
    }

    @BeforeEach
    void setUp() throws ServletException {
        logoutServlet = new LogoutServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
        dispatcherMock = mock(RequestDispatcher.class);
        logoutServlet.init();
        System.out.println("Setup iniziale del test completato.");
    }

    @Test
    void testLogoutSuccess() throws ServletException, IOException {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(requestMock.getRequestDispatcher("/Home")).thenReturn(dispatcherMock);
        logoutServlet.doGet(requestMock, responseMock);
        verify(sessionMock).invalidate();
        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("Sessione invalidata e reindirizzato alla Home.");
    }
}
