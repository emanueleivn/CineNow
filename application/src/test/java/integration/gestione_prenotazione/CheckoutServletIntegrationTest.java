package integration.gestione_prenotazione;

import it.unisa.application.sottosistemi.gestione_prenotazione.view.CheckoutServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

 public class CheckoutServletIntegrationTest {
    private CheckoutServlet servlet;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private RequestDispatcher dispatcherMock;

    @BeforeEach
    void setUp() {
        servlet = new CheckoutServlet();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        dispatcherMock = mock(RequestDispatcher.class);
        when(requestMock.getRequestDispatcher(anyString())).thenReturn(dispatcherMock);
    }
     private void invokeDoGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
         Method doPost = CheckoutServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
         doPost.setAccessible(true);
         doPost.invoke(servlet, request, response);
     }
    @Test
    void testDoGetSuccess() throws Exception {
        String proiezioneId = "1";
        String posti = "A-1,A-2";
        String totale = "20.00";
        when(requestMock.getParameter("proiezioneId")).thenReturn(proiezioneId);
        when(requestMock.getParameter("posti")).thenReturn(posti);
        when(requestMock.getParameter("totale")).thenReturn(totale);
        invokeDoGet(requestMock, responseMock);
        verify(requestMock).setAttribute("proiezioneId", proiezioneId);
        verify(requestMock).setAttribute("posti", posti);
        verify(requestMock).setAttribute("totale", totale);
        verify(requestMock).getRequestDispatcher("/WEB-INF/jsp/checkout.jsp");
        verify(dispatcherMock).forward(requestMock, responseMock);
        System.out.println("doGet corretto e forward verso la pagina di checkout");
    }

    @Test
    void testDoGetMissingParameters() throws Exception {
        System.out.println("Test valori nulli. proiezioneId=null");
        when(requestMock.getParameter("proiezioneId")).thenReturn(null);
        when(requestMock.getParameter("posti")).thenReturn(null);
        when(requestMock.getParameter("totale")).thenReturn(null);
        invokeDoGet(requestMock, responseMock);
        verify(requestMock).setAttribute("proiezioneId", null);
        verify(requestMock).setAttribute("posti", null);
        verify(requestMock).setAttribute("totale", null);
        verify(requestMock).getRequestDispatcher("/WEB-INF/jsp/checkout.jsp");
        verify(dispatcherMock).forward(requestMock, responseMock);
        verifyNoMoreInteractions(responseMock);
    }
}

