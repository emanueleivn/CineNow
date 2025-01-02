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
import java.util.ArrayList;
import java.util.List;

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
        String password = "ValidPassword123!";
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
    @DisplayName("Registrazione fallita - email non valida")
    void testRegistrazioneInvalidEmails() {
        List<String> invalidEmails = new ArrayList<>();
        invalidEmails.add(null);
        invalidEmails.add("");
        invalidEmails.add("invalid-email");
        invalidEmails.add("test@.com");
        invalidEmails.add("test@domain");
        invalidEmails.add("test<>@domain.com");

        for (String email : invalidEmails) {
            Cliente cliente = registrazioneService.registrazione(email, "ValidPassword123!", "Mario", "Rossi");
            assertNull(cliente, "La registrazione dovrebbe fallire con email non valida: " + email);
        }
    }

    @Test
    @DisplayName("Registrazione fallita - password non valida")
    void testRegistrazioneInvalidPasswords() {
        List<String> invalidPasswords = new ArrayList<>();
        invalidPasswords.add(null);
        invalidPasswords.add("");
        invalidPasswords.add("shorty");
        invalidPasswords.add("NoSpecialChar123");
        invalidPasswords.add("nouppercase!123");
        invalidPasswords.add("NOLOWERCASE!123");

        for (String password : invalidPasswords) {
            Cliente cliente = registrazioneService.registrazione("valid@example.com", password, "Mario", "Rossi");
            assertNull(cliente, "La registrazione dovrebbe fallire con password non valida: " + password);
        }
    }

    @Test
    @DisplayName("Registrazione fallita - nome non valido")
    void testRegistrazioneInvalidNames() {
        List<String> invalidNames = new ArrayList<>();
        invalidNames.add(null);
        invalidNames.add("");
        invalidNames.add("Mario<>");
        invalidNames.add("   ");

        for (String nome : invalidNames) {
            Cliente cliente = registrazioneService.registrazione("valid@example.com", "ValidPassword123!", nome, "Rossi");
            assertNull(cliente, "La registrazione dovrebbe fallire con nome non valido: " + nome);
        }
    }

    @Test
    @DisplayName("Registrazione fallita - cognome non valido")
    void testRegistrazioneInvalidSurnames() {
        List<String> invalidSurnames = new ArrayList<>();
        invalidSurnames.add(null);
        invalidSurnames.add("");
        invalidSurnames.add("Rossi<>");
        invalidSurnames.add("   ");

        for (String cognome : invalidSurnames) {
            Cliente cliente = registrazioneService.registrazione("valid@example.com", "ValidPassword123!", "Mario", cognome);
            assertNull(cliente, "La registrazione dovrebbe fallire con cognome non valido: " + cognome);
        }
    }

    @Test
    @DisplayName("Registrazione fallita - email già esistente")
    void testRegistrazioneEmailAlreadyExists() {
        String email = "existing@example.com";
        utenteDAO.create(new Cliente(email, PasswordHash.hash("password123"), "Existing", "User"));
        Cliente cliente = registrazioneService.registrazione(email, "ValidPassword123!", "Mario", "Rossi");
        assertNull(cliente, "La registrazione dovrebbe fallire con email già esistente");
    }
}
