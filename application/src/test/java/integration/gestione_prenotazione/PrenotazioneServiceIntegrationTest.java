package integration.gestione_prenotazione;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.dao.PostoProiezioneDAO;
import it.unisa.application.model.dao.PrenotazioneDAO;
import it.unisa.application.model.entity.*;
import it.unisa.application.sottosistemi.gestione_prenotazione.service.PrenotazioneService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unit.test_DAO.DatabaseSetupForTest;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrenotazioneServiceIntegrationTest {
    private PrenotazioneService prenotazioneService;
    private PrenotazioneDAO prenotazioneDAO;
    private PostoProiezioneDAO postoProiezioneDAO;

    @BeforeAll
    static void setupDatabase() {
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        prenotazioneDAO = new PrenotazioneDAO();
        postoProiezioneDAO = new PostoProiezioneDAO();
        prenotazioneService = new PrenotazioneService(prenotazioneDAO, postoProiezioneDAO);
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
            INSERT INTO posto (id_sala, fila, numero)
            VALUES (1, 'A', 1),
                   (1, 'A', 2);
            INSERT INTO posto_proiezione (id_sala, fila, numero, id_proiezione, stato)
            VALUES (1, 'A', 1, 1, TRUE),
                   (1, 'A', 2, 1, TRUE);
        """;
            statement.execute(dataInsertScript);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante il popolamento del database di test", e);
        }
    }

    @Test
    void testAggiungiOrdineSuccess() {
        Cliente cliente = new Cliente("test@example.com", "password", "Mario", "Rossi");
        Proiezione proiezione = new Proiezione();
        proiezione.setId(1);
        Sala sala = new Sala();
        sala.setId(1);
        Posto posto1 = new Posto(sala, 'A', 1);
        Posto posto2 = new Posto(sala, 'A', 2);
        PostoProiezione postoProiezione1 = new PostoProiezione(posto1, proiezione);
        PostoProiezione postoProiezione2 = new PostoProiezione(posto2, proiezione);
        List<PostoProiezione> posti = Arrays.asList(postoProiezione1, postoProiezione2);
        Prenotazione result = prenotazioneService.aggiungiOrdine(cliente, posti, proiezione);
        assertNotNull(result, "La prenotazione non dovrebbe essere null.");
        assertEquals(cliente, result.getCliente(), "Il cliente della prenotazione non corrisponde.");
        assertEquals(proiezione, result.getProiezione(), "La proiezione della prenotazione non corrisponde.");
        assertEquals(2, result.getPostiPrenotazione().size(), "Il numero di posti prenotati non corrisponde.");
        System.out.println("Prenotazione creata: " + result);
    }

    @Test
    void testAggiungiOrdineFailurePostiOccupati() {
        Cliente cliente = new Cliente("test@example.com", "password", "Mario", "Rossi");
        Proiezione proiezione = new Proiezione();
        proiezione.setId(1);
        Sala sala = new Sala();
        sala.setId(1);
        Posto posto1 = new Posto(sala, 'A', 1);
        Posto posto2 = new Posto(sala, 'A', 2);
        PostoProiezione postoProiezione1 = new PostoProiezione(posto1, proiezione);
        PostoProiezione postoProiezione2 = new PostoProiezione(posto2, proiezione);
        postoProiezione1.setStato(false); // Posto già occupato
        postoProiezione2.setStato(false); // Posto già occupato
        List<PostoProiezione> posti = Arrays.asList(postoProiezione1, postoProiezione2);
        assertThrows(RuntimeException.class, () ->
                        prenotazioneService.aggiungiOrdine(cliente, posti, proiezione),
                "Dovrebbe lanciare un'eccezione per posti occupati.");
    }

    @Test
    void testOttienimentoPostiProiezione() {
        Proiezione proiezione = new Proiezione();
        proiezione.setId(1);
        List<PostoProiezione> result = prenotazioneService.ottieniPostiProiezione(proiezione);
        assertNotNull(result, "I posti non dovrebbero essere null.");
        assertEquals(2, result.size(), "Il numero di posti recuperati non corrisponde.");
        System.out.println("Posti recuperati per Proiezione ID=" + proiezione.getId());
    }
}
