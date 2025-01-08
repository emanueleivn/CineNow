package integration.gestione_prenotazione;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.dao.PrenotazioneDAO;
import it.unisa.application.model.entity.Cliente;
import it.unisa.application.model.entity.Prenotazione;
import it.unisa.application.sottosistemi.gestione_prenotazione.service.StoricoOrdiniService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unit.test_DAO.DatabaseSetupForTest;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StoricoOrdiniServiceIntegrationTest {
    private StoricoOrdiniService storicoOrdiniService;

    @BeforeAll
    static void setupDatabase() {
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();
        storicoOrdiniService = new StoricoOrdiniService(prenotazioneDAO);
        populateDatabase();
    }

    private void populateDatabase() {
        try (Connection conn = DataSourceSingleton.getInstance().getConnection();
             Statement statement = conn.createStatement()) {
            String dataInsertScript = """
                DELETE FROM occupa;
                DELETE FROM prenotazione;
                DELETE FROM posto_proiezione;
                DELETE FROM proiezione;
                DELETE FROM posto;
                DELETE FROM sala;
                DELETE FROM film;
                DELETE FROM slot;
                DELETE FROM cliente;
                DELETE FROM utente;
                DELETE FROM sede;

                INSERT INTO utente (email, password, ruolo) VALUES ('test@example.com', 'password', 'cliente');
                INSERT INTO cliente (email, nome, cognome) VALUES ('test@example.com', 'Mario', 'Rossi');

                INSERT INTO sede (id, nome, via, città, cap) VALUES (1, 'Cinema Centrale', 'Via Roma', 'Napoli', '80100');
                INSERT INTO sala (id, id_sede, numero, capienza) VALUES (1, 1, 1, 100);
                INSERT INTO film (id, titolo, genere, classificazione, durata, descrizione, is_proiettato)
                VALUES (1, 'Avatar', 'Sci-fi', 'T', 180, 'Film di fantascienza', TRUE);
                INSERT INTO slot (id, ora_inizio) VALUES (1, '15:00:00');
                INSERT INTO proiezione (id, data, id_film, id_sala, id_orario)
                VALUES (1, '2025-01-10', 1, 1, 1);

                INSERT INTO prenotazione (id, email_cliente, id_proiezione) VALUES
                (1, 'test@example.com', 1),
                (2, 'test@example.com', 1);
            """;
            statement.execute(dataInsertScript);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante il popolamento del database di test", e);
        }
    }

    @Test
    void testStoricoOrdiniSuccess() {
        Cliente cliente = new Cliente("test@example.com", "password", "Mario", "Rossi");
        List<Prenotazione> result = storicoOrdiniService.storicoOrdini(cliente);
        assertNotNull(result, "La lista di risultati non dovrebbe essere null");
        assertEquals(2, result.size(), "La dimensione della lista dovrebbe essere 2");
        System.out.println("Storico ordini per Cliente=" + cliente.getEmail());
        result.forEach(p -> System.out.println("Prenotazione ID=" + p.getId()));
    }

    @Test
    void testStoricoOrdiniEmpty() {
        Cliente cliente = new Cliente("empty@example.com", "password", "Mario", "Rossi");
        List<Prenotazione> result = storicoOrdiniService.storicoOrdini(cliente);
        assertNotNull(result, "La lista di risultati non dovrebbe essere null");
        assertTrue(result.isEmpty(), "La lista dovrebbe essere vuota");
        System.out.println("Storico ordini vuoto per Cliente=" + cliente.getEmail());
    }

    @Test
    void testStoricoOrdiniNullCliente() {
        assertThrows(IllegalArgumentException.class, () -> storicoOrdiniService.storicoOrdini(null));
        System.out.println("Tentativo di recuperare storico ordini con Cliente=null");
    }
}
