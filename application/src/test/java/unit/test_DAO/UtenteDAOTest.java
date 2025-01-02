package unit.test_DAO;

import static org.junit.jupiter.api.Assertions.*;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.dao.UtenteDAO;
import it.unisa.application.model.entity.Utente;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

public class UtenteDAOTest {
    private UtenteDAO utenteDAO;

    @BeforeAll
    static void globalSetup() {
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        utenteDAO = new UtenteDAO();
    }

    @AfterEach
    void tearDown() {
        try (Connection conn = DataSourceSingleton.getInstance().getConnection()) {
            conn.createStatement().execute("DELETE FROM utente;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Creazione di un utente")
    void testCreateUtente() {
        String uniqueEmail = "test_" + System.currentTimeMillis() + "@example.com";
        Utente utente = new Utente(uniqueEmail, "hashedPassword", "cliente");
        assertTrue(utenteDAO.create(utente), "Creazione utente fallita");
        System.out.println("Utente creato: " + utente);
    }

    @Test
    @DisplayName("Recupero utente tramite email")
    void testRetrieveByEmail() {
        Utente utente = new Utente("test@example.com", "hashedPassword", "cliente");
        utenteDAO.create(utente);
        Utente retrieved = utenteDAO.retrieveByEmail("test@example.com");
        assertNotNull(retrieved, "L'utente non è stato trovato");
        assertEquals("test@example.com", retrieved.getEmail(), "Email non corrispondente");
        assertEquals("hashedPassword", retrieved.getPassword(), "Password non corrispondente");
        assertEquals("cliente", retrieved.getRuolo(), "Ruolo non corrispondente");
        System.out.println("Utente recuperato: " + retrieved);
    }
}
