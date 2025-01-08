package integration.gestione_sala;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.dao.FilmDAO;
import it.unisa.application.model.dao.ProiezioneDAO;
import it.unisa.application.model.dao.SlotDAO;
import it.unisa.application.model.entity.Film;
import it.unisa.application.model.entity.Slot;
import it.unisa.application.sottosistemi.gestione_sala.service.SlotService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unit.test_DAO.DatabaseSetupForTest;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SlotServiceIntegrationTest {
    private SlotService slotService;

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
            stmt.execute("INSERT INTO sede (id, nome, via, città, cap) VALUES (1, 'CineNow Napoli', 'Via Roma', 'Napoli', '80100');");
            stmt.execute("INSERT INTO film (id, titolo, durata, genere, classificazione, descrizione, is_proiettato) VALUES (1, 'Film Test', 120, 'Azione', 'PG-13', 'Descrizione di test', true);");
            stmt.execute("INSERT INTO sala (id, numero, capienza, id_sede) VALUES (1, 1, 100, 1);");
            stmt.execute("INSERT INTO slot (id, ora_inizio) VALUES (1, '18:00:00');");
            stmt.execute("INSERT INTO slot (id, ora_inizio) VALUES (2, '18:30:00');");
            stmt.execute("INSERT INTO slot (id, ora_inizio) VALUES (3, '19:00:00');");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        slotService = new SlotService();
    }

    @Test
    void testFilmNonEsistente() {
        int filmId = 2;
        int salaId = 1;
        LocalDate dataInizio = LocalDate.of(2025, 1, 1);
        LocalDate dataFine = LocalDate.of(2025, 1, 7);
        System.out.println("Dati di test utilizzati: filmId=" + filmId + ", salaId=" + salaId);
        Exception exception = assertThrows(RuntimeException.class, () ->
                slotService.slotDisponibili(filmId, salaId, dataInizio, dataFine));
        assertEquals("Film non esistente.", exception.getMessage());
    }

    @Test
    void testSlotDisponibili() throws Exception {
        int filmId = 1;
        int salaId = 1;
        LocalDate dataInizio = LocalDate.of(2025, 1, 1);
        LocalDate dataFine = LocalDate.of(2025, 1, 1);
        System.out.println("Dati di test utilizzati: filmId=" + filmId + ", salaId=" + salaId + ", dataInizio=" + dataInizio + ", dataFine=" + dataFine);
        try (Connection conn = DataSourceSingleton.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM film WHERE id = 1;");
            stmt.execute("INSERT INTO film (id, titolo, durata, genere, classificazione, descrizione, is_proiettato) VALUES (1, 'Film Test', 120, 'Azione', 'PG-13', 'Descrizione di test', true);");
            stmt.execute("DELETE FROM sala WHERE id = 1;");
            stmt.execute("INSERT INTO sala (id, numero, capienza, id_sede) VALUES (1, 1, 100, 1);");
            stmt.execute("DELETE FROM slot WHERE id IN (1, 2, 3, 4);");
            stmt.execute("INSERT INTO slot (id, ora_inizio) VALUES (1, '18:00:00');");
            stmt.execute("INSERT INTO slot (id, ora_inizio) VALUES (2, '18:30:00');");
            stmt.execute("INSERT INTO slot (id, ora_inizio) VALUES (3, '19:00:00');");
            stmt.execute("INSERT INTO slot (id, ora_inizio) VALUES (4, '19:30:00');");
        }
        Map<String, Object> result = slotService.slotDisponibili(filmId, salaId, dataInizio, dataFine);
        System.out.println("Risultato: " + result + "\n");
        assertNotNull(result);
        assertEquals(120, result.get("durataFilm"));
        List<Map<String, Object>> calendar = (List<Map<String, Object>>) result.get("calendar");
        assertEquals(1, calendar.size());
        assertEquals("2025-01-01", calendar.getFirst().get("data"));
        List<Map<String, Object>> slots = (List<Map<String, Object>>) calendar.get(0).get("slots");
        assertEquals(5, slots.size());
        assertFalse((Boolean) slots.get(0).get("occupato"));
        assertFalse((Boolean) slots.get(1).get("occupato"));
        assertFalse((Boolean) slots.get(2).get("occupato"));
        assertFalse((Boolean) slots.get(3).get("occupato"));
    }
}
