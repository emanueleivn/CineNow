package unit.test_DAO;

import static org.junit.jupiter.api.Assertions.*;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.dao.ClienteDAO;
import it.unisa.application.model.entity.Cliente;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
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
        System.out.println("Email: " + uniqueEmail);
        System.out.println("Nome: " + cliente.getNome());
        System.out.println("Cognome: " + cliente.getCognome());
        System.out.println("Password: " + cliente.getPassword());
        assertTrue(clienteDAO.create(cliente), "Creazione cliente fallita");
    }

    @Test
    @DisplayName("Recupero cliente tramite email e password")
    void testRetrieveByEmail() {
        String uniqueEmail = "cliente_" + System.currentTimeMillis() + "@example.com";
        Cliente cliente = new Cliente(uniqueEmail, "hashedPassword", "Mario", "Rossi");
        clienteDAO.create(cliente);
        System.out.println("Cliente creato:");
        System.out.println("Email utilizzata: " + uniqueEmail);
        System.out.println("Password utilizzata: hashedPassword");
        Cliente retrieved = clienteDAO.retrieveByEmail(uniqueEmail, "hashedPassword");
        assertNotNull(retrieved, "Il cliente non è stato trovato");
        System.out.println("Dati recuperati:");
        System.out.println("Email: " + retrieved.getEmail());
        System.out.println("Nome: " + retrieved.getNome());
        System.out.println("Cognome: " + retrieved.getCognome());
        assertEquals(uniqueEmail, retrieved.getEmail(), "Email non corrispondente");
        assertEquals("Mario", retrieved.getNome(), "Nome non corrispondente");
        assertEquals("Rossi", retrieved.getCognome(), "Cognome non corrispondente");
    }
}

