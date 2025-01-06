package integration.gestione_utente;

import it.unisa.application.model.entity.Cliente;
import it.unisa.application.sottosistemi.gestione_utente.service.RegistrazioneService;
import it.unisa.application.sottosistemi.gestione_utente.view.RegistrazioneServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

public class RegistrazioneServletIntegrationTest {

    private RegistrazioneServlet registrazioneServlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private HttpSession sessionMock;
    private RequestDispatcher dispatcherMock;
    private RegistrazioneService registrazioneServiceMock;

    @BeforeEach
    void setUp() throws Exception {
        registrazioneServlet = new RegistrazioneServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
        dispatcherMock = mock(RequestDispatcher.class);
        registrazioneServiceMock = mock(RegistrazioneService.class);

        // Iniettiamo il service mock nella servlet
        var field = RegistrazioneServlet.class.getDeclaredField("regServ");
        field.setAccessible(true);
        field.set(registrazioneServlet, registrazioneServiceMock);
    }

    @Test
    void testDoGet() throws Exception {
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/registrazioneView.jsp")).thenReturn(dispatcherMock);
        Method doGetMethod = RegistrazioneServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(registrazioneServlet, requestMock, responseMock);
        verify(dispatcherMock).forward(requestMock, responseMock);
    }

    @Test
    void testDoPostSuccess() throws Exception {
        when(requestMock.getParameter("email")).thenReturn("test@example.com");
        when(requestMock.getParameter("password")).thenReturn("Password123!");
        when(requestMock.getParameter("nome")).thenReturn("Mario");
        when(requestMock.getParameter("cognome")).thenReturn("Rossi");
        when(requestMock.getSession()).thenReturn(sessionMock);

        Cliente mockCliente = new Cliente("test@example.com", "Password123!", "Mario", "Rossi");
        when(registrazioneServiceMock.registrazione("test@example.com", "Password123!", "Mario", "Rossi"))
                .thenReturn(mockCliente);

        Method doPostMethod = RegistrazioneServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(registrazioneServlet, requestMock, responseMock);

        verify(sessionMock).setAttribute("cliente", mockCliente);
        verify(responseMock).sendRedirect(requestMock.getContextPath() + "/Home");
    }

    @Test
    void testDoPostFailure() throws Exception {
        when(requestMock.getParameter("email")).thenReturn("invalid-email");
        when(requestMock.getParameter("password")).thenReturn("123");
        when(requestMock.getParameter("nome")).thenReturn("Mario");
        when(requestMock.getParameter("cognome")).thenReturn("Rossi");
        when(requestMock.getRequestDispatcher("/WEB-INF/jsp/error.jsp")).thenReturn(dispatcherMock);
        when(registrazioneServiceMock.registrazione("invalid-email", "123", "Mario", "Rossi"))
                .thenReturn(null);

        Method doPostMethod = RegistrazioneServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
        doPostMethod.invoke(registrazioneServlet, requestMock, responseMock);
        verify(requestMock).setAttribute(eq("errorMessage"), anyString());
        verify(dispatcherMock).forward(requestMock, responseMock);
    }
}
