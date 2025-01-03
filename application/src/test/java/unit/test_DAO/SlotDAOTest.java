package unit.test_DAO;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.dao.SlotDAO;
import it.unisa.application.model.entity.Proiezione;
import it.unisa.application.model.entity.Slot;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SlotDAOTest {
    private SlotDAO slotDAO;

    @BeforeAll
    static void globalSetup() {
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setup() {
        slotDAO = new SlotDAO();
        populateDatabase();
    }

    private void populateDatabase() {
        try (Connection conn = DataSourceSingleton.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            String cleanupScript = """
                        DELETE FROM proiezione;
                        DELETE FROM slot;
                        DELETE FROM film;
                        DELETE FROM sala;
                        DELETE FROM sede;
                    """;
            stmt.execute(cleanupScript);

            String setupScript = """
                        INSERT INTO sede (id, nome, via, città, cap) 
                        VALUES (1, 'CineNow', 'Via Roma', 'Napoli', '80100');
                    
                        INSERT INTO sala (id, id_sede, numero, capienza) 
                        VALUES (1, 1, 1, 100);
                    
                        INSERT INTO film (id, titolo, genere, classificazione, durata, locandina, descrizione, is_proiettato)
                        VALUES (1, 'Film Test', 'Azione', 'PG-13', 120, 'locandina.jpg', 'Descrizione di test', false);
                    
                        INSERT INTO slot (id, ora_inizio) 
                        VALUES 
                        (1, '10:00:00'),
                        (2, '12:00:00'),
                        (3, '15:00:00'),
                        (4, '18:00:00');
                    
                        INSERT INTO proiezione (id, data, id_film, id_sala, id_orario) 
                        VALUES
                        (1, '2025-01-01', 1, 1, 2),
                        (2, '2025-01-01', 1, 1, 4);
                    """;
            stmt.execute(setupScript);
        } catch (Exception e) {
            throw new RuntimeException("Errore nel setup del database per i test", e);
        }
    }

    @Test
    void testRetrieveById() {
        Slot slot = slotDAO.retriveById(1);
        assertNotNull(slot, "Il metodo retriveById dovrebbe restituire un oggetto Slot");
        assertEquals(1, slot.getId(), "L'ID dello slot dovrebbe essere 1");
        assertEquals("10:00:00", slot.getOraInizio().toString(), "L'ora dello slot dovrebbe essere 10:00:00");
    }

    @Test
    void testRetrieveByProiezione() {
        Proiezione proiezione = new Proiezione();
        Slot slot = new Slot();
        slot.setId(2);
        proiezione.setOrarioProiezione(slot);
        Slot retrievedSlot = slotDAO.retiveByProiezione(proiezione);
        assertNotNull(retrievedSlot, "Il metodo retiveByProiezione dovrebbe restituire uno slot");
        assertEquals(2, retrievedSlot.getId(), "L'ID dello slot dovrebbe essere 2");
        assertEquals("12:00:00", retrievedSlot.getOraInizio().toString(), "L'ora dello slot dovrebbe essere 12:00:00");
    }

    @Test
    void testRetrieveFreeSlot() {
        List<Slot> freeSlots = slotDAO.retriveFreeSlot(LocalDate.of(2025, 1, 1), 1);

        assertNotNull(freeSlots, "La lista degli slot liberi non dovrebbe essere null");
        assertEquals(2, freeSlots.size(), "Dovrebbero esserci 2 slot liberi");
        assertEquals(1, freeSlots.get(0).getId(), "Il primo slot libero dovrebbe avere ID 1");
        assertEquals(3, freeSlots.get(1).getId(), "Il secondo slot libero dovrebbe avere ID 3");
    }
}
