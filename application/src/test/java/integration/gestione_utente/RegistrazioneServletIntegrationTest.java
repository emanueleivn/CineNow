package integration.gestione_utente;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.Cliente;
import it.unisa.application.sottosistemi.gestione_utente.service.RegistrazioneService;
import it.unisa.application.sottosistemi.gestione_utente.view.RegistrazioneServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unit.test_DAO.DatabaseSetupForTest;

import java.sql.Connection;

import static org.mockito.Mockito.*;

public class RegistrazioneServletIntegrationTest {
    private RegistrazioneServlet registrazioneServlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private HttpSession sessionMock;
    private RequestDispatcher dispatcherMock;
    private RegistrazioneService registrazioneService;

    @BeforeAll
    static void setupDatabase() {
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() throws Exception {
        registrazioneServlet = new RegistrazioneServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
        dispatcherMock = mock(RequestDispatcher.class);
        registrazioneService = new RegistrazioneService();
        var field = RegistrazioneServlet.class.getDeclaredField("regServ");
        field.setAccessible(true);
        field.set(registrazioneServlet, registrazioneService);
        try (Connection conn = DataSourceSingleton.getInstance().getConnection()) {
            conn.createStatement().execute("DELETE FROM cliente;");
            conn.createStatement().execute("DELETE FROM utente;");
        }
    }

    @Test
    void testDoGet() throws Exception {
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/registrazioneView.jsp")).thenReturn(dispatcherMock);
        var doGetMethod = RegistrazioneServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(registrazioneServlet, requestMock, responseMock);
        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("Forward corretto verso la pagina di registrazione");
    }

    @Test
    void testDoPostSuccess() throws Exception {
        System.out.println("Utente di test: Email=test@example.com, Password=ValidPassword123!, Nome=Mario, Cognome=Rossi");
        when(requestMock.getParameter("email")).thenReturn("test@example.com");
        when(requestMock.getParameter("password")).thenReturn("Password123!");
        when(requestMock.getParameter("nome")).thenReturn("Mario");
        when(requestMock.getParameter("cognome")).thenReturn("Rossi");
        when(requestMock.getSession()).thenReturn(sessionMock);
        var doPostMethod = RegistrazioneServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(registrazioneServlet, requestMock, responseMock);
        verify(sessionMock).setAttribute(eq("cliente"), any(Cliente.class));
        verify(responseMock).sendRedirect(requestMock.getContextPath() + "/Home");
        System.out.println("Registrazione riuscita e reindirizzamento alla Home.");
    }

    @Test
    void testDoPostFailure() throws Exception {
        System.out.println("Utente di test: Email=invalid-email, Password=123, Nome=Mario, Cognome=Rossi");
        when(requestMock.getParameter("email")).thenReturn("invalid-email");
        when(requestMock.getParameter("password")).thenReturn("123");
        when(requestMock.getParameter("nome")).thenReturn("Mario");
        when(requestMock.getParameter("cognome")).thenReturn("Rossi");
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/error.jsp")).thenReturn(dispatcherMock);
        var doPostMethod = RegistrazioneServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(registrazioneServlet, requestMock, responseMock);
        verify(requestMock).setAttribute(eq("errorMessage"), anyString());
        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("Registrazione fallita e reindirizzamento verso pagina di errore.");
    }
}
