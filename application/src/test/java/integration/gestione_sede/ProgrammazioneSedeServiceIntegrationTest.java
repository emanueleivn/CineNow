package integration.gestione_sede;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.Film;
import it.unisa.application.model.entity.Proiezione;
import it.unisa.application.model.entity.Sede;
import it.unisa.application.sottosistemi.gestione_sede.service.ProgrammazioneSedeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unit.test_DAO.DatabaseSetupForTest;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProgrammazioneSedeServiceIntegrationTest {
    private static ProgrammazioneSedeService service;

    @BeforeAll
    static void setUpDatabase() {
        DatabaseSetupForTest.configureH2DataSource();
        service = new ProgrammazioneSedeService();
    }

    @BeforeEach
    void populateDatabase() {
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             Statement statement = connection.createStatement()) {
            String dataInsertScript = """
                DELETE FROM proiezione;
                DELETE FROM film;
                DELETE FROM sala;
                DELETE FROM sede;
                DELETE FROM slot;

                INSERT INTO sede (id, nome, via, città, cap) VALUES (1, 'Cinema Centrale', 'Via Roma', 'Napoli', '80100');
                INSERT INTO sala (id, id_sede, numero, capienza) VALUES (1, 1, 1, 100);
                INSERT INTO film (id, titolo, genere, classificazione, durata, descrizione, is_proiettato)
                VALUES (1, 'Avatar', 'Sci-fi', 'T', 180, 'Film di fantascienza', TRUE),
                       (2, 'Inception', 'Thriller', 'T', 148, 'Film sui sogni', TRUE);
                INSERT INTO slot (id, ora_inizio) VALUES (1, '15:00:00'), (2, '18:00:00');
                INSERT INTO proiezione (id, data, id_film, id_sala, id_orario)
                VALUES (1, '2025-01-10', 1, 1, 1),
                       (2, '2025-01-11', 2, 1, 2);
            """;
            statement.execute(dataInsertScript);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante il popolamento del database di test", e);
        }
    }

    @Test
    void testGetProgrammazione() {
        System.out.println("Recupero programmazione per sede ID=1.");
        int sedeId = 1;
        List<Proiezione> programmazione = service.getProgrammazione(sedeId);
        System.out.println("Numero proiezioni trovate: " + programmazione.size());
        programmazione.forEach(proiezione ->
                System.out.println("Proiezione - Film: " + proiezione.getFilmProiezione().getTitolo() +
                        ", Sala: " + proiezione.getSalaProiezione().getNumeroSala() +
                        ", Data: " + proiezione.getDataProiezione())
        );

        assertNotNull(programmazione, "La programmazione non dovrebbe essere null.");
        assertEquals(2, programmazione.size(), "La programmazione dovrebbe contenere 2 proiezioni.");
        assertTrue(programmazione.stream().anyMatch(p -> p.getFilmProiezione().getTitolo().equals("Avatar")),
                "La programmazione dovrebbe includere 'Avatar'.");
    }

    @Test
    void testGetProiezioniFilm() {
        System.out.println("Recupero proiezioni del film 'Avatar' nella sede ID=1.");
        int filmId = 1;
        int sedeId = 1;
        List<Proiezione> proiezioniFilm = service.getProiezioniFilm(filmId, sedeId);
        System.out.println("Proiezioni trovate per il film: " + proiezioniFilm.size());
        proiezioniFilm.forEach(proiezione ->
                System.out.println("Proiezione - Film: " + proiezione.getFilmProiezione().getTitolo() +
                        ", Data: " + proiezione.getDataProiezione())
        );
        assertNotNull(proiezioniFilm, "Le proiezioni del film non dovrebbero essere null.");
        assertEquals(1, proiezioniFilm.size(), "Dovrebbe esserci una proiezione per il film 'Avatar'.");
        assertEquals("Avatar", proiezioniFilm.getFirst().getFilmProiezione().getTitolo(),
                "Il titolo del film dovrebbe essere 'Avatar'.");
    }

    @Test
    void testGetCatalogoSede() {
        System.out.println("Recupero catalogo per la sede 'Movieplex'.");
        Sede sede = new Sede(1, "Cinema Centrale", "Via Roma");
        List<Film> catalogo = service.getCatalogoSede(sede);
        System.out.println("Film trovati nel catalogo: " + catalogo.size());
        catalogo.forEach(film ->
                System.out.println("Film - Titolo: " + film.getTitolo() + ", Genere: " + film.getGenere())
        );
        assertNotNull(catalogo, "Il catalogo non dovrebbe essere null.");
        assertEquals(2, catalogo.size(), "Il catalogo dovrebbe contenere 2 film.");
        assertTrue(catalogo.stream().anyMatch(f -> f.getTitolo().equals("Avatar")),
                "Il catalogo dovrebbe includere 'Avatar'.");
        assertTrue(catalogo.stream().anyMatch(f -> f.getTitolo().equals("Inception")),
                "Il catalogo dovrebbe includere 'Inception'.");
    }
}

