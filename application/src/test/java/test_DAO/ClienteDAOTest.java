package test_DAO;

import static org.junit.jupiter.api.Assertions.*;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.dao.ClienteDAO;
import it.unisa.application.model.entity.Cliente;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

public class ClienteDAOTest {
    private ClienteDAO clienteDAO;

    @BeforeAll
    static void globalSetup() {
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        clienteDAO = new ClienteDAO();
    }

    @AfterEach
    void tearDown() {
        try (Connection conn = DataSourceSingleton.getInstance().getConnection()) {
            conn.createStatement().execute("DELETE FROM cliente;");
            conn.createStatement().execute("DELETE FROM utente;");
            conn.createStatement().execute("DELETE FROM prenotazione;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Creazione di un cliente")
    void testCreateCliente() {
        String uniqueEmail = "cliente_" + System.currentTimeMillis() + "@example.com";
        Cliente cliente = new Cliente(uniqueEmail, "hashedPassword", "Mario", "Rossi");
        assertTrue(clienteDAO.create(cliente), "Creazione cliente fallita");
        System.out.println("Cliente creato" + cliente);
    }

    @Test
    @DisplayName("Recupero cliente tramite email e password")
    void testRetrieveByEmail() {
        String uniqueEmail = "cliente_" + System.currentTimeMillis() + "@example.com";
        Cliente cliente = new Cliente(uniqueEmail, "hashedPassword", "Mario", "Rossi");
        clienteDAO.create(cliente);

        Cliente retrieved = clienteDAO.retrieveByEmail(uniqueEmail, "hashedPassword");
        assertNotNull(retrieved, "Il cliente non è stato trovato");
        assertEquals(uniqueEmail, retrieved.getEmail(), "Email non corrispondente");
        assertEquals("Mario", retrieved.getNome(), "Nome non corrispondente");
        assertEquals("Rossi", retrieved.getCognome(), "Cognome non corrispondente");
        System.out.println("Cliente recuperato" + retrieved);
    }
}
