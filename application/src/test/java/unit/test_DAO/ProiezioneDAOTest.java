package unit.test_DAO;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.dao.ProiezioneDAO;
import it.unisa.application.model.entity.*;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ProiezioneDAOTest {
    private ProiezioneDAO proiezioneDAO;

    @BeforeAll
    static void globalSetup() {
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        proiezioneDAO = new ProiezioneDAO();
        try (Connection conn = DataSourceSingleton.getInstance().getConnection()) {
            conn.createStatement().execute("DELETE FROM posto;");
            conn.createStatement().execute("DELETE FROM slot;");
            conn.createStatement().execute("DELETE FROM sala;");
            conn.createStatement().execute("DELETE FROM sede;");
            conn.createStatement().execute("DELETE FROM film;");
            conn.createStatement().execute("DELETE FROM proiezione;");
            conn.createStatement().execute("DELETE FROM posto_proiezione;");

            conn.createStatement().executeUpdate("INSERT INTO film (id, titolo, durata, genere, classificazione, descrizione) VALUES (1, 'Test Film', 120, 'Commedia', 'T', 'Film di test');");
            conn.createStatement().executeUpdate("INSERT INTO sede (id, nome, via, città, cap) VALUES (1, 'Test Sede', 'Via Test', 'Test City', '12345');");
            conn.createStatement().executeUpdate("INSERT INTO sala (id, numero, capienza, id_sede) VALUES (1, 1, 100, 1);");
            conn.createStatement().executeUpdate("INSERT INTO slot (id, ora_inizio) VALUES (1, '14:00:00');");
            conn.createStatement().executeUpdate("INSERT INTO slot (id, ora_inizio) VALUES (2, '16:00:00');");
            conn.createStatement().executeUpdate("INSERT INTO posto (id_sala, fila, numero) VALUES (1, 'A', 1);");
        } catch (SQLException e) {
            fail("Setup iniziale fallito: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        try (Connection conn = DataSourceSingleton.getInstance().getConnection()) {
            conn.createStatement().execute("DELETE FROM posto_proiezione;");
            conn.createStatement().execute("DELETE FROM proiezione;");
            conn.createStatement().execute("DELETE FROM sala;");
            conn.createStatement().execute("DELETE FROM film;");
            conn.createStatement().execute("DELETE FROM slot;");
            conn.createStatement().execute("DELETE FROM posto;");
            conn.createStatement().execute("DELETE FROM sede;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Creazione di una proiezione")
    void testCreateProiezione() {
        Film film = new Film();
        film.setId(1);
        Sala sala = new Sala();
        sala.setId(1);
        Slot slot = new Slot();
        slot.setId(1);
        Proiezione proiezione = new Proiezione();
        proiezione.setFilmProiezione(film);
        proiezione.setSalaProiezione(sala);
        proiezione.setOrarioProiezione(slot);
        proiezione.setDataProiezione(LocalDate.now());
        boolean result = proiezioneDAO.create(proiezione);
        assertTrue(result, "Creazione della proiezione fallita");
        System.out.println("Proiezione creata: Film ID=" + film.getId() + ", Sala ID=" + sala.getId() + ", Slot ID=" + slot.getId() + ", Data=" + proiezione.getDataProiezione());
    }

    @Test
    @DisplayName("Recupero di una proiezione tramite ID")
    void testRetrieveById() {
        try (Connection conn = DataSourceSingleton.getInstance().getConnection()) {
            conn.createStatement().executeUpdate("INSERT INTO proiezione (id, data, id_film, id_sala, id_orario) VALUES (1, '2025-01-01', 1, 1, 1);");
        } catch (SQLException e) {
            fail("Setup dati test fallito: " + e.getMessage());
        }
        Proiezione retrieved = proiezioneDAO.retrieveById(1);
        assertNotNull(retrieved, "Proiezione non trovata");
        assertEquals(1, retrieved.getId(), "ID della proiezione non corrispondente");
        System.out.println("Proiezione recuperata: ID=" + retrieved.getId() + ", Film ID=" + retrieved.getFilmProiezione().getId() + ", Sala ID=" + retrieved.getSalaProiezione().getId() + ", Slot ID=" + retrieved.getOrarioProiezione().getId());
    }

    @Test
    @DisplayName("Recupero proiezioni per film e sede")
    void testRetrieveByFilm() {
        try (Connection conn = DataSourceSingleton.getInstance().getConnection()) {
            conn.createStatement().executeUpdate("INSERT INTO proiezione (id, data, id_film, id_sala, id_orario) VALUES (1, '2025-01-01', 1, 1, 1);");
        } catch (SQLException e) {
            fail("Setup dati test fallito: " + e.getMessage());
        }

        Film film = new Film();
        film.setId(1);
        Sede sede = new Sede();
        sede.setId(1);
        List<Proiezione> proiezioni = proiezioneDAO.retrieveByFilm(film, sede);
        assertNotNull(proiezioni, "La lista di proiezioni è null");
        assertFalse(proiezioni.isEmpty(), "Nessuna proiezione trovata");
        for (Proiezione p : proiezioni) {
            System.out.println("Proiezione recuperata: ID=" + p.getId() + ", Film ID=" + p.getFilmProiezione().getId() + ", Sala ID=" + p.getSalaProiezione().getId() + ", Data=" + p.getDataProiezione());
        }
    }

    @Test
    @DisplayName("Recupero tutte le proiezioni per sede")
    void testRetrieveAllBySede() {
        try (Connection conn = DataSourceSingleton.getInstance().getConnection()) {
            conn.createStatement().executeUpdate("INSERT INTO proiezione (id, data, id_film, id_sala, id_orario) VALUES (1, '2025-01-01', 1, 1, 1);");
        } catch (SQLException e) {
            fail("Setup dati test fallito: " + e.getMessage());
        }
        List<Proiezione> proiezioni = proiezioneDAO.retrieveAllBySede(1);
        assertNotNull(proiezioni, "La lista di proiezioni è null");
        assertFalse(proiezioni.isEmpty(), "Nessuna proiezione trovata");
        for (Proiezione p : proiezioni) {
            System.out.println("Proiezione recuperata: ID=" + p.getId() + ", Film ID=" + p.getFilmProiezione().getId() + ", Sala ID=" + p.getSalaProiezione().getId() + ", Data=" + p.getDataProiezione());
        }
    }
}
