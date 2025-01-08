package integration.gestione_utente;

import static org.junit.jupiter.api.Assertions.*;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.dao.ClienteDAO;
import it.unisa.application.model.dao.UtenteDAO;
import it.unisa.application.model.entity.Cliente;
import it.unisa.application.model.entity.Utente;
import it.unisa.application.sottosistemi.gestione_utente.service.AutenticazioneService;
import it.unisa.application.utilities.PasswordHash;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import unit.test_DAO.DatabaseSetupForTest;

import java.sql.Connection;
import java.sql.SQLException;

public class AutenticazioneServiceIntegrationTest {
    private AutenticazioneService autenticazioneService;
    private ClienteDAO clienteDAO;
    private UtenteDAO utenteDAO;

    @BeforeAll
    static void globalSetup() {
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        clienteDAO = new ClienteDAO();
        utenteDAO = new UtenteDAO();
        autenticazioneService = new AutenticazioneService(utenteDAO, clienteDAO);
        try (Connection conn = DataSourceSingleton.getInstance().getConnection()) {
            conn.createStatement().execute("DELETE FROM cliente;");
            conn.createStatement().execute("DELETE FROM utente;");
            String hashedPassword = PasswordHash.hash("Testing1!");
            conn.createStatement().executeUpdate("INSERT INTO utente (email, password, ruolo) VALUES ('test@test.com', '" + hashedPassword + "', 'cliente');");
            conn.createStatement().executeUpdate("INSERT INTO cliente (email, nome, cognome) VALUES ('test@test.com', 'Mario', 'Rossi');");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        try (Connection conn = DataSourceSingleton.getInstance().getConnection()) {
            conn.createStatement().execute("DELETE FROM cliente;");
            conn.createStatement().execute("DELETE FROM utente;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("TC01.1: Login riuscito")
    void testLoginSuccess() {
        String email = "test@test.com";
        String password = "Testing1!";
        System.out.println("Utente di test: Email=" + email + ", Password=" + password);
        Utente utente = autenticazioneService.login(email, password);
        assertNotNull(utente, "Il login dovrebbe avere successo");
        assertTrue(utente instanceof Cliente, "L'utente autenticato dovrebbe essere un Cliente");
        assertEquals(email, utente.getEmail(), "L'email non corrisponde");
        System.out.println("Test completato: Login riuscito con utente " + utente.getEmail());
    }

    @Test
    @DisplayName("TC01.2: Login fallito - password errata")
    void testLoginWrongPassword() {
        String email = "test@test.com";
        String wrongPassword = "12345678";
        System.out.println("Utente di test: Email=" + email + ", Password Errata=" + wrongPassword);
        Utente utente = autenticazioneService.login(email, wrongPassword);
        assertNull(utente, "Il login dovrebbe fallire con password errata");
        System.out.println("Test completato: Login fallito con password errata.");
    }

    @Test
    @DisplayName("TC01.3: Login fallito - utente non trovato")
    void testLoginUserNotFound() {
        String email = "pippo@pluto.com";
        String password = "12345678";
        System.out.println("Utente di test: Email=" + email + ", Password=" + password);
        Utente utente = autenticazioneService.login(email, password);
        assertNull(utente, "Il login dovrebbe fallire per utente non trovato");
        System.out.println("Test completato: Utente non trovato.");
    }

    @Test
    @DisplayName("Logout")
    void testLogout() {
        HttpSession session = Mockito.mock(HttpSession.class);
        autenticazioneService.logout(session);
        Mockito.verify(session).invalidate();
        System.out.println("Test completato: Sessione invalidata con successo.");
    }
}
