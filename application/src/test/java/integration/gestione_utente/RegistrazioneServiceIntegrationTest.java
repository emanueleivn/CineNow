package integration.gestione_utente;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.dao.ClienteDAO;
import it.unisa.application.model.dao.UtenteDAO;
import it.unisa.application.model.entity.Cliente;
import it.unisa.application.sottosistemi.gestione_utente.service.RegistrazioneService;
import it.unisa.application.utilities.PasswordHash;
import org.junit.jupiter.api.*;
import unit.test_DAO.DatabaseSetupForTest;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegistrazioneServiceIntegrationTest {

    private RegistrazioneService registrazioneService;
    private ClienteDAO clienteDAO;
    private UtenteDAO utenteDAO;

    @BeforeAll
    void setupDatabase() {
        DatabaseSetupForTest.configureH2DataSource();
        System.out.println("Setup iniziale del database completato.");
    }

    @BeforeEach
    void setupService() {
        clienteDAO = new ClienteDAO();
        utenteDAO = new UtenteDAO();
        registrazioneService = new RegistrazioneService(utenteDAO, clienteDAO);
        try (Connection conn = DataSourceSingleton.getInstance().getConnection()) {
            conn.createStatement().execute("DELETE FROM cliente;");
            conn.createStatement().execute("DELETE FROM utente;");
        } catch (SQLException e) {
            fail("Errore durante la pulizia del database: " + e.getMessage());
        }
    }

    @Test
    void testInvalidEmailFormat() {
        System.out.println("Email di test = example@<>");
        Cliente result = registrazioneService.registrazione("example@<>", "Password123!", "Mario", "Rossi");
        assertNull(result, "Registrazione dovrebbe fallire per email in formato errato.");
        System.out.println("Registrazione fallita come previsto.");
    }

    @Test
    void testEmailAlreadyExists() {
        String email = "mariorossi@gmail.com";
        String passwordHash = PasswordHash.hash("Password123!");
        try (Connection conn = DataSourceSingleton.getInstance().getConnection()) {
            conn.createStatement().executeUpdate("INSERT INTO utente (email, password, ruolo) VALUES ('" + email + "', '" + passwordHash + "', 'cliente');");
            conn.createStatement().executeUpdate("INSERT INTO cliente (email, nome, cognome) VALUES ('" + email + "', 'Mario', 'Rossi');");
        } catch (SQLException e) {
            fail("Errore durante l'inserimento dei dati di test: " + e.getMessage());
        }
        System.out.println("Inserimento utente: Email=" + email+", Password=" + "Password123!");
        System.out.println("Email di test. Email=" + email);
        Cliente result = registrazioneService.registrazione(email, "Password123!", "Mario", "Rossi");
        assertNull(result, "Registrazione dovrebbe fallire per email già registrata.");
        System.out.println("Registrazione fallita come previsto.");
    }

    @Test
    void testEmailNotProvided() {
        System.out.println("Email di test. Email=");
        Cliente result = registrazioneService.registrazione(null, "Password123!", "Mario", "Rossi");
        assertNull(result, "Registrazione dovrebbe fallire per email mancante.");
        System.out.println("Registrazione fallita come previsto.");
    }

    @Test
    void testInvalidPasswordFormat() {
        System.out.println("Password di test = pass<");
        Cliente result = registrazioneService.registrazione("test@example.com", "pass<", "Mario", "Rossi");
        assertNull(result, "Registrazione dovrebbe fallire per password in formato errato.");
        System.out.println("Registrazione fallita come previsto.");
    }

    @Test
    void testPasswordNotProvided() {
        System.out.println("PAssword di test. Password=");
        Cliente result = registrazioneService.registrazione("test@example.com", null, "Mario", "Rossi");
        assertNull(result, "Registrazione dovrebbe fallire per password mancante.");
        System.out.println("Test completato: Registrazione fallita come previsto.");
    }

    @Test
    void testInvalidNameFormat() {
        System.out.println("Nome di test. Nome=<");
        Cliente result = registrazioneService.registrazione("test@example.com", "Password123!", "<", "Rossi");
        assertNull(result, "Registrazione dovrebbe fallire per nome in formato errato.");
        System.out.println("Registrazione fallita come previsto.");
    }

    @Test
    void testNameNotProvided() {
        System.out.println("Nome di test. Nome=");
        Cliente result = registrazioneService.registrazione("test@example.com", "Password123!", null, "Rossi");
        assertNull(result, "Registrazione dovrebbe fallire per nome mancante.");
        System.out.println("Registrazione fallita come previsto.");
    }

    @Test
    void testInvalidSurnameFormat() {
        System.out.println("Cognome di test. Cognome=<");
        Cliente result = registrazioneService.registrazione("test@example.com", "Password123!", "Mario", "<");
        assertNull(result, "Registrazione dovrebbe fallire per cognome in formato errato.");
        System.out.println("Registrazione fallita come previsto.");
    }

    @Test
    void testSurnameNotProvided() {
        System.out.println("Cognome di test. Cognome=");
        Cliente result = registrazioneService.registrazione("test@example.com", "Password123!", "Mario", null);
        assertNull(result, "Registrazione dovrebbe fallire per cognome mancante.");
        System.out.println("Registrazione fallita come previsto.");
    }

    @Test
    void testSuccessfulRegistration() {
        System.out.println("Utente di test. Email=test@example.com ,Password=ValidPassword123!, Nome=Mario, Cognome=Rossi");
        Cliente result = registrazioneService.registrazione("test@example.com", "ValidPassword123!", "Mario", "Rossi");
        assertNotNull(result, "Registrazione dovrebbe avere successo.");
        assertEquals("test@example.com", result.getEmail(), "L'email registrata non corrisponde.");
        assertEquals("Mario", result.getNome(), "Il nome registrato non corrisponde.");
        assertEquals("Rossi", result.getCognome(), "Il cognome registrato non corrisponde.");
        System.out.println("Registrazione avvenuta con successo.");
    }
}
