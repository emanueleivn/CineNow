package unit.test_gestione_sala;

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
import static org.mockito.Mockito.*;

class SlotServiceTest {

    private SlotService slotService;
    private FilmDAO filmDAOSpy;
    private ProiezioneDAO proiezioneDAOSpy;
    private SlotDAO slotDAOSpy;

    @BeforeAll
    static void globalSetup() {
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        filmDAOSpy = spy(new FilmDAO());
        proiezioneDAOSpy = spy(new ProiezioneDAO());
        slotDAOSpy = spy(new SlotDAO());
        slotService = new SlotService();

        try (Connection conn = DataSourceSingleton.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM proiezione;");
            stmt.execute("DELETE FROM slot;");
            stmt.execute("DELETE FROM sala;");
            stmt.execute("DELETE FROM sede;");
            stmt.execute("DELETE FROM film;");
            stmt.execute("INSERT INTO sede (id, nome, via, città, cap) VALUES (1, 'CineNow Napoli', 'Via Roma', 'Napoli', '80100');");
            stmt.execute("INSERT INTO film (id, titolo, durata, genere, classificazione, descrizione, is_proiettato) " +
                    "VALUES (1, 'Film Test', 120, 'Azione', 'PG-13', 'Descrizione di test', true);");
            stmt.execute("INSERT INTO sala (id, numero, capienza, id_sede) VALUES (1, 1, 100, 1);");
            stmt.execute("INSERT INTO slot (id, ora_inizio) VALUES (1, '18:00:00');");
            stmt.execute("INSERT INTO slot (id, ora_inizio) VALUES (2, '18:30:00');");
            stmt.execute("INSERT INTO slot (id, ora_inizio) VALUES (3, '19:00:00');");
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la configurazione del database per i test", e);
        }
    }

    @Test
    void testFilmNonEsistente() {
        int filmId = 2;
        int salaId = 1;
        LocalDate dataInizio = LocalDate.of(2025, 1, 1);
        LocalDate dataFine = LocalDate.of(2025, 1, 7);
        System.out.println("Test Film Non Esistente - Parametri: filmId=" + filmId + ", salaId=" + salaId);
        doReturn(null).when(filmDAOSpy).retrieveById(filmId);
        Exception exception = assertThrows(RuntimeException.class, () ->
                slotService.slotDisponibili(filmId, salaId, dataInizio, dataFine));

        System.out.println("Risultato: eccezione='" + exception.getMessage() + "'\n");
        assertEquals("Film non esistente.", exception.getMessage());
    }

    @Test
    void testSlotDisponibili() throws Exception {
        int filmId = 1;
        int salaId = 1;
        LocalDate dataInizio = LocalDate.of(2025, 1, 1);
        LocalDate dataFine = LocalDate.of(2025, 1, 1);
        Film film = new Film();
        film.setId(filmId);
        film.setDurata(85);
        Slot slot1 = new Slot();
        slot1.setId(1);
        slot1.setOraInizio(Time.valueOf("18:00:00"));
        Slot slot2 = new Slot();
        slot2.setId(2);
        slot2.setOraInizio(Time.valueOf("18:30:00"));
        Slot slot3 = new Slot();
        slot3.setId(3);
        slot3.setOraInizio(Time.valueOf("19:00:00"));
        System.out.println("Test Slot Disponibili - Parametri utilizzati: filmId=" + filmId + ", salaId=" + salaId + ", dataInizio=" + dataInizio + ", dataFine=" + dataFine);
        doReturn(film).when(filmDAOSpy).retrieveById(filmId);
        doReturn(List.of(slot1, slot2, slot3)).when(slotDAOSpy).retrieveAllSlots();
        doReturn(null).when(proiezioneDAOSpy).retrieveProiezioneBySalaSlotAndData(eq(salaId), eq(slot1.getId()), eq(dataInizio));
        doReturn(null).when(proiezioneDAOSpy).retrieveProiezioneBySalaSlotAndData(eq(salaId), eq(slot2.getId()), eq(dataInizio));
        doReturn(null).when(proiezioneDAOSpy).retrieveProiezioneBySalaSlotAndData(eq(salaId), eq(slot3.getId()), eq(dataInizio));
        Map<String, Object> result = slotService.slotDisponibili(filmId, salaId, dataInizio, dataFine);
        System.out.println("Risultato: " + result + "\n");
        assertNotNull(result);
        assertEquals(120, result.get("durataFilm"));
        List<Map<String, Object>> calendar = (List<Map<String, Object>>) result.get("calendar");
        assertEquals(1, calendar.size());
        assertEquals("2025-01-01", calendar.getFirst().get("data"));
        List<Map<String, Object>> slots = (List<Map<String, Object>>) calendar.get(0).get("slots");
        assertEquals(4, slots.size());
        assertFalse((Boolean) slots.get(0).get("occupato"));
        assertFalse((Boolean) slots.get(1).get("occupato"));
        assertFalse((Boolean) slots.get(2).get("occupato"));
        assertFalse((Boolean) slots.get(3).get("occupato"));
    }
}
