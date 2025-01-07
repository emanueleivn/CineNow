package unit.test_gestione_utente;

import static org.junit.jupiter.api.Assertions.*;
import it.unisa.application.model.dao.ClienteDAO;
import it.unisa.application.model.dao.UtenteDAO;
import it.unisa.application.model.entity.Cliente;
import it.unisa.application.model.entity.Utente;
import it.unisa.application.sottosistemi.gestione_utente.service.AutenticazioneService;
import it.unisa.application.utilities.PasswordHash;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

public class AutenticazioneServiceTest {
    private AutenticazioneService autenticazioneService;
    private ClienteDAO clienteDAOMock;
    private UtenteDAO utenteDAOMock;

    @BeforeEach
    void setUp() {
        clienteDAOMock = Mockito.mock(ClienteDAO.class);
        utenteDAOMock = Mockito.mock(UtenteDAO.class);
        autenticazioneService = new AutenticazioneService(utenteDAOMock, clienteDAOMock);
    }

    @Test
    @DisplayName("TC01.1: Login riuscito")
    void testLoginSuccess() {
        String email = "test@test.com";
        String password = "Testing1!";
        String hashedPassword = PasswordHash.hash(password);
        Utente mockUtente = new Utente(email, hashedPassword, "cliente");
        Cliente mockCliente = new Cliente(email, hashedPassword, "Mario", "Rossi");
        Mockito.when(utenteDAOMock.retrieveByEmail(email)).thenReturn(mockUtente);
        Mockito.when(clienteDAOMock.retrieveByEmail(email, hashedPassword)).thenReturn(mockCliente);
        Utente utente = autenticazioneService.login(email, password);
        assertNotNull(utente, "Il login dovrebbe avere successo");
        assertTrue(utente instanceof Cliente, "L'utente autenticato dovrebbe essere un Cliente");
        assertEquals(email, utente.getEmail(), "L'email non corrisponde");
        System.out.println("Email:"+email + ", password:"+password);
    }

    @Test
    @DisplayName("TC01.2: Login fallito - credenziali errate")
    void testLoginWrongPassword() {
        String email = "test@test.com";
        String wrongPassword = "12<345678>";
        String hashedPassword = PasswordHash.hash("Testing1!");
        Utente mockUtente = new Utente(email, hashedPassword, "cliente");
        Mockito.when(utenteDAOMock.retrieveByEmail(email)).thenReturn(mockUtente);
        Utente utente = autenticazioneService.login(email, wrongPassword);
        assertNull(utente, "Il login dovrebbe fallire con password errata");
        System.out.println("Email:"+email + ", password:"+wrongPassword);
    }

    @Test
    @DisplayName("TC01.3: Login fallito - utente non trovato")
    void testLoginUserNotFound() {
        String email = "pippo@pluto.com";
        String password = "Testing1!";
        Mockito.when(utenteDAOMock.retrieveByEmail(email)).thenReturn(null);
        Utente utente = autenticazioneService.login(email, password);
        assertNull(utente, "Il login dovrebbe fallire per utente non trovato");
        System.out.println("Email:"+email + ", password:"+password);
    }

    @Test
    @DisplayName("Logout")
    void testLogout() {
        HttpSession sessionMock = Mockito.mock(HttpSession.class);
        autenticazioneService.logout(sessionMock);
        Mockito.verify(sessionMock).invalidate();
    }
}
