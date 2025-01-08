package integration.gestione_sala;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.dao.ProiezioneDAO;
import it.unisa.application.sottosistemi.gestione_sala.service.ProgrammazioneService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unit.test_DAO.DatabaseSetupForTest;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProgrammazioneServiceIntegrationTest {
    private ProgrammazioneService programmazioneService;

    @BeforeAll
    static void globalSetup() {
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        try (Connection conn = DataSourceSingleton.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM proiezione;");
            stmt.execute("DELETE FROM slot;");
            stmt.execute("DELETE FROM sala;");
            stmt.execute("DELETE FROM sede;");
            stmt.execute("DELETE FROM film;");
            stmt.execute("INSERT INTO sede (id, nome, via, città, cap) VALUES (1, 'Moviplex', 'Via Roma', 'Napoli', '80100');");
            stmt.execute("INSERT INTO film (id, titolo, durata, genere, classificazione, descrizione, is_proiettato) VALUES (1, 'Film Test', 120, 'Azione', 'T', 'Descrizione di test', true);");
            stmt.execute("INSERT INTO sala (id, numero, capienza, id_sede) VALUES (1, 1, 100, 1);");
            stmt.execute("INSERT INTO slot (id, ora_inizio) VALUES (1, '10:00:00');");
            stmt.execute("INSERT INTO slot (id, ora_inizio) VALUES (2, '12:00:00');");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        programmazioneService = new ProgrammazioneService(new ProiezioneDAO());
    }

    @Test
    void testFilmNonSelezionato() {
        System.out.println("Dati di test: filmId = null, salaId=1, slotIds=[1, 2], data=2025-01-10");
        boolean result = programmazioneService.aggiungiProiezione(0, 1, List.of(1, 2), LocalDate.of(2025, 1, 10));
        assertFalse(result);
    }

    @Test
    void testDataNonInserita() {
        System.out.println("Dati di test: filmId=1, salaId=1, slotIds=[1, 2], data=null");
        boolean result = programmazioneService.aggiungiProiezione(1, 1, List.of(1, 2), null);
        assertFalse(result);
    }

    @Test
    void testDataPassata() {
        System.out.println("Dati di test: filmId=1, salaId=1, slotIds=[1, 2], data=2023-01-01");
        boolean result = programmazioneService.aggiungiProiezione(1, 1, List.of(1, 2), LocalDate.of(2023, 1, 1));
        assertFalse(result);
    }

    @Test
    void testSlotNonDisponibili() {
        System.out.println("Dati di test: filmId=1, salaId=1, slotIds=[10, 20], data=2025-01-10");
        boolean result = programmazioneService.aggiungiProiezione(1, 1, List.of(10, 20), LocalDate.of(2025, 1, 10));
        assertFalse(result);
    }

    @Test
    void testProiezioneAggiuntaConSuccesso() {
        System.out.println("Dati di test: filmId=1, salaId=1, slotIds=[1, 2], data=2025-01-10");
        boolean result = programmazioneService.aggiungiProiezione(1, 1, List.of(1, 2), LocalDate.of(2025, 1, 10));
        assertTrue(result);
    }
}
