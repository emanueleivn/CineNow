package unit.test_DAO;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.dao.PostoProiezioneDAO;
import it.unisa.application.model.entity.Posto;
import it.unisa.application.model.entity.PostoProiezione;
import it.unisa.application.model.entity.Proiezione;
import it.unisa.application.model.entity.Sala;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PostoProiezioneDAOTest {
    private PostoProiezioneDAO postoProiezioneDAO;

    @BeforeAll
    static void globalSetup() {
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setup() {
        postoProiezioneDAO = new PostoProiezioneDAO();
        populateDatabase();
    }

    private void populateDatabase() {
        try (Connection conn = DataSourceSingleton.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {

            String cleanupScript = """
                        DELETE FROM occupa;
                        DELETE FROM posto_proiezione;
                        DELETE FROM prenotazione;
                        DELETE FROM proiezione;
                        DELETE FROM slot;
                        DELETE FROM film;
                        DELETE FROM posto;
                        DELETE FROM sala;
                        DELETE FROM gest_sede;
                        DELETE FROM sede;
                    """;
            stmt.execute(cleanupScript);

            String setupScript = """
                        INSERT INTO sede (id, nome, via, città, cap) VALUES (1, 'CineNow', 'Via Roma', 'Napoli', '80100');
                        INSERT INTO sala (id, id_sede, numero, capienza) VALUES (1, 1, 1, 100);
                        INSERT INTO posto (id_sala, fila, numero) 
                        VALUES (1, 'A', 1), (1, 'A', 2), (1, 'B', 3);
                        INSERT INTO film (id, titolo, genere, classificazione, durata, locandina, descrizione, is_proiettato)
                        VALUES (1, 'Film di Test', 'Azione', 'PG-13', 120, 'locandina.jpg', 'Descrizione', true);
                        INSERT INTO slot (id, ora_inizio) VALUES (1, '20:00:00');
                        INSERT INTO proiezione (id, data, id_film, id_sala, id_orario) VALUES (1, '2025-01-01', 1, 1, 1);
                        INSERT INTO posto_proiezione (id_sala, fila, numero, id_proiezione, stato) 
                        VALUES (1, 'A', 1, 1, true), (1, 'A', 2, 1, true);
                        INSERT INTO prenotazione (id, email_cliente, id_proiezione) VALUES (1, NULL, 1);
                    """;
            stmt.execute(setupScript);
        } catch (Exception e) {
            throw new RuntimeException("Errore nel setup del database per i test", e);
        }
    }

    @Test
    void testCreate() {
        PostoProiezione postoProiezione = new PostoProiezione();
        Posto posto = new Posto();
        Sala sala = new Sala();
        sala.setId(1);
        posto.setSala(sala);
        posto.setFila('B');
        posto.setNumero(3);
        postoProiezione.setPosto(posto);

        Proiezione proiezione = new Proiezione();
        proiezione.setId(1);
        postoProiezione.setProiezione(proiezione);
        postoProiezione.setStato(true);
        boolean result = postoProiezioneDAO.create(postoProiezione);
        assertTrue(result, "Il metodo create dovrebbe restituire true");
    }

    @Test
    void testRetrieveAllByProiezione() {
        Proiezione proiezione = new Proiezione();
        proiezione.setId(1);
        List<PostoProiezione> posti = postoProiezioneDAO.retrieveAllByProiezione(proiezione);
        assertNotNull(posti, "La lista non dovrebbe essere null");
        assertFalse(posti.isEmpty(), "La lista non dovrebbe essere vuota");
        assertEquals(2, posti.size(), "Dovrebbero esserci 2 posti associati alla proiezione");

        PostoProiezione posto1 = posti.getFirst();
        assertEquals(1, posto1.getPosto().getSala().getId());
        assertEquals('A', posto1.getPosto().getFila());
        assertEquals(1, posto1.getPosto().getNumero());

        PostoProiezione posto2 = posti.get(1);
        assertEquals(1, posto2.getPosto().getSala().getId());
        assertEquals('A', posto2.getPosto().getFila());
        assertEquals(2, posto2.getPosto().getNumero());
    }

    @Test
    void testOccupaPosto() {
        PostoProiezione postoProiezione = new PostoProiezione();
        Posto posto = new Posto();
        Sala sala = new Sala();
        sala.setId(1);
        posto.setSala(sala);
        posto.setFila('A');
        posto.setNumero(1);
        postoProiezione.setPosto(posto);
        Proiezione proiezione = new Proiezione();
        proiezione.setId(1);
        postoProiezione.setProiezione(proiezione);
        boolean result = postoProiezioneDAO.occupaPosto(postoProiezione, 1);
        assertTrue(result, "Il metodo occupaPosto dovrebbe restituire true");
    }
}
