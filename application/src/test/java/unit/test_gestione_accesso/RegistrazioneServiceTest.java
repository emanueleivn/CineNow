package unit.test_gestione_accesso;

import static org.junit.jupiter.api.Assertions.*;

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

public class RegistrazioneServiceTest {

    private RegistrazioneService registrazioneService;
    private ClienteDAO clienteDAO;
    private UtenteDAO utenteDAO;

    @BeforeAll
    static void globalSetup() {
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        registrazioneService = new RegistrazioneService();
        clienteDAO = new ClienteDAO();
        utenteDAO = new UtenteDAO();
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
    @DisplayName("Registrazione riuscita")
    void testRegistrazioneSuccess() {
        String email = "test@example.com";
        String password = "Password123!";
        String nome = "Mario";
        String cognome = "Rossi";
        Cliente cliente = registrazioneService.registrazione(email, password, nome, cognome);
        assertNotNull(cliente, "La registrazione non dovrebbe restituire null");
        assertEquals(email, cliente.getEmail(), "L'email non corrisponde");
        assertEquals(PasswordHash.hash(password), cliente.getPassword(), "La password hashata non corrisponde");
        assertEquals(nome, cliente.getNome(), "Il nome non corrisponde");
        assertEquals(cognome, cliente.getCognome(), "Il cognome non corrisponde");
        Cliente retrieved = clienteDAO.retrieveByEmail(email, PasswordHash.hash(password));
        assertNotNull(retrieved, "Il cliente non è stato trovato nel database");
        assertEquals(email, retrieved.getEmail(), "L'email del cliente salvato non corrisponde");
    }

    @Test
    @DisplayName("Registrazione fallita - email non valida (null)")
    void testRegistrazioneEmailNull() {
        Cliente cliente = registrazioneService.registrazione(null, "Password123!", "Mario", "Rossi");
        assertNull(cliente, "La registrazione dovrebbe fallire con email null");
    }

    @Test
    @DisplayName("Registrazione fallita - email non valida (vuota)")
    void testRegistrazioneEmailEmpty() {
        Cliente cliente = registrazioneService.registrazione("", "Password123!", "Mario", "Rossi");
        assertNull(cliente, "La registrazione dovrebbe fallire con email vuota");
    }

    @Test
    @DisplayName("Registrazione fallita - email non valida (formato errato)")
    void testRegistrazioneEmailInvalidFormat() {
        Cliente cliente = registrazioneService.registrazione("invalid-email", "Password123!", "Mario", "Rossi");
        assertNull(cliente, "La registrazione dovrebbe fallire con email in formato errato");
    }

    @Test
    @DisplayName("Registrazione fallita - password non valida (null)")
    void testRegistrazionePasswordNull() {
        Cliente cliente = registrazioneService.registrazione("test@example.com", null, "Mario", "Rossi");
        assertNull(cliente, "La registrazione dovrebbe fallire con password null");
    }

    @Test
    @DisplayName("Registrazione fallita - password non valida (vuota)")
    void testRegistrazionePasswordEmpty() {
        Cliente cliente = registrazioneService.registrazione("test@example.com", "", "Mario", "Rossi");
        assertNull(cliente, "La registrazione dovrebbe fallire con password vuota");
    }

    @Test
    @DisplayName("Registrazione fallita - nome non valido (null)")
    void testRegistrazioneNomeNull() {
        Cliente cliente = registrazioneService.registrazione("test@example.com", "Password123!", null, "Rossi");
        assertNull(cliente, "La registrazione dovrebbe fallire con nome null");
    }

    @Test
    @DisplayName("Registrazione fallita - nome non valido (vuoto)")
    void testRegistrazioneNomeEmpty() {
        Cliente cliente = registrazioneService.registrazione("test@example.com", "Password123!", "", "Rossi");
        assertNull(cliente, "La registrazione dovrebbe fallire con nome vuoto");
    }

    @Test
    @DisplayName("Registrazione fallita - cognome non valido (null)")
    void testRegistrazioneCognomeNull() {
        Cliente cliente = registrazioneService.registrazione("test@example.com", "Password123!", "Mario", null);
        assertNull(cliente, "La registrazione dovrebbe fallire con cognome null");
    }

    @Test
    @DisplayName("Registrazione fallita - cognome non valido (vuoto)")
    void testRegistrazioneCognomeEmpty() {
        Cliente cliente = registrazioneService.registrazione("test@example.com", "Password123!", "Mario", "");
        assertNull(cliente, "La registrazione dovrebbe fallire con cognome vuoto");
    }

    @Test
    @DisplayName("Registrazione fallita - email già esistente")
    void testRegistrazioneEmailAlreadyExists() {
        String email = "test@example.com";
        utenteDAO.create(new Cliente(email, PasswordHash.hash("Password123!"), "Existing", "User"));
        Cliente cliente = registrazioneService.registrazione(email, "Password123!", "Mario", "Rossi");
        assertNull(cliente, "La registrazione dovrebbe fallire con email già esistente");
    }
}
