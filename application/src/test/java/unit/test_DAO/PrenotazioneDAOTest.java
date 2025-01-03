package unit.test_DAO;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.dao.PrenotazioneDAO;
import it.unisa.application.model.entity.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PrenotazioneDAOTest {
    private PrenotazioneDAO prenotazioneDAO;

    @BeforeAll
    public void setupDatabase() {
        DatabaseSetupForTest.configureH2DataSource();
        prenotazioneDAO = new PrenotazioneDAO();
        prepareTestData();
    }

    private void prepareTestData() {
        try {
            String cleanupSQL = """
                        DELETE FROM occupa;
                        DELETE FROM prenotazione;
                        DELETE FROM posto_proiezione;
                        DELETE FROM proiezione;
                        DELETE FROM slot;
                        DELETE FROM sala;
                        DELETE FROM sede;
                        DELETE FROM film;
                        DELETE FROM cliente;
                        DELETE FROM utente;
                    """;
            DataSourceSingleton.getInstance().getConnection().createStatement().executeUpdate(cleanupSQL);

            String setupSQL = """
                        INSERT INTO utente (email, password, ruolo) VALUES ('test@example.com', 'password', 'cliente');
                        INSERT INTO cliente (email, nome, cognome) VALUES ('test@example.com', 'Test', 'Test');
                        INSERT INTO film (id, titolo, genere, classificazione, durata, locandina, descrizione, is_proiettato) 
                        VALUES (1, 'Test Film', 'Drama', 'PerTutti', 120, 'path', 'Descrizione film', TRUE);
                        INSERT INTO sede (id, nome, via, città, cap) VALUES (1, 'Sede Test', 'Via Test', 'Città Test', '12345');
                        INSERT INTO sala (id, id_sede, numero, capienza) VALUES (1, 1, 1, 100);
                        INSERT INTO slot (id, ora_inizio) VALUES (1, '18:00:00');
                        INSERT INTO proiezione (id, data, id_film, id_sala, id_orario) 
                        VALUES (1, '2023-01-01', 1, 1, 1);
                    """;
            DataSourceSingleton.getInstance().getConnection().createStatement().executeUpdate(setupSQL);
        } catch (Exception e) {
            throw new RuntimeException("Errore nella preparazione dei dati di test", e);
        }
    }

    @Test
    public void testCreatePrenotazione() {
        Cliente cliente = new Cliente();
        cliente.setEmail("test@example.com");
        Proiezione proiezione = new Proiezione();
        proiezione.setId(1);
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setCliente(cliente);
        prenotazione.setProiezione(proiezione);
        boolean result = prenotazioneDAO.create(prenotazione);
        assertTrue(result, "La prenotazione dovrebbe essere creata correttamente");
        assertNotNull(prenotazione.getId(), "L'ID della prenotazione dovrebbe essere generato");
    }

    @Test
    public void testRetrievePrenotazioneById() {
        Cliente cliente = new Cliente();
        cliente.setEmail("test@example.com");
        Proiezione proiezione = new Proiezione();
        proiezione.setId(1);
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setCliente(cliente);
        prenotazione.setProiezione(proiezione);
        prenotazioneDAO.create(prenotazione);
        Prenotazione retrievedPrenotazione = prenotazioneDAO.retrieveById(prenotazione.getId());
        assertNotNull(retrievedPrenotazione, "La prenotazione con ID dovrebbe esistere");
        assertEquals("test@example.com", retrievedPrenotazione.getCliente().getEmail(), "L'email del cliente dovrebbe corrispondere");
        assertEquals(1, retrievedPrenotazione.getProiezione().getId(), "L'ID della proiezione dovrebbe essere 1");
    }

    @Test
    public void testRetrieveAllByCliente() {
        prepareTestData();

        Cliente cliente = new Cliente();
        cliente.setEmail("test@example.com");
        Proiezione proiezione = new Proiezione();
        proiezione.setId(1);
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setCliente(cliente);
        prenotazione.setProiezione(proiezione);
        prenotazioneDAO.create(prenotazione);
        List<Prenotazione> prenotazioni = prenotazioneDAO.retrieveAllByCliente(cliente);
        assertNotNull(prenotazioni, "La lista di prenotazioni non dovrebbe essere null");
        assertEquals(1, prenotazioni.size(), "Dovrebbe esserci una sola prenotazione per il cliente");
        assertEquals("test@example.com", prenotazioni.getFirst().getCliente().getEmail(), "L'email del cliente dovrebbe corrispondere");
    }
}
