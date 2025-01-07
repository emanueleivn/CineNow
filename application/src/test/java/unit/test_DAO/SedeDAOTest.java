package unit.test_DAO;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.entity.Film;
import it.unisa.application.model.entity.Sala;
import it.unisa.application.model.entity.Sede;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SedeDAOTest {
    private SedeDAO sedeDAO;

    @BeforeAll
    static void globalSetup() {
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setup() {
        sedeDAO = new SedeDAO();
        populateDatabase();
    }

    private void populateDatabase() {
        try (Connection conn = DataSourceSingleton.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            String cleanupScript = """
                        DELETE FROM occupa;
                        DELETE FROM prenotazione;
                        DELETE FROM posto_proiezione;
                        DELETE FROM proiezione;
                        DELETE FROM posto;
                        DELETE FROM sala;
                        DELETE FROM sede;
                        DELETE FROM slot;
                        DELETE FROM film;
                        DELETE FROM gest_sede;
                        DELETE FROM utente;
                    """;
            stmt.execute(cleanupScript);
            String setupScript = """
                        INSERT INTO utente (email, password, ruolo) VALUES
                        ('gestore1@example.com', 'password1', 'gestore'),
                        ('gestore2@example.com', 'password2', 'gestore');
                    
                        INSERT INTO sede (id, nome, via, città, cap) VALUES
                        (1, 'CineNow Napoli', 'Via Roma', 'Napoli', '80100'),
                        (2, 'CineNow Milano', 'Corso Buenos Aires', 'Milano', '20100');
                    
                        INSERT INTO gest_sede (email, id_sede) VALUES
                        ('gestore1@example.com', 1),
                        ('gestore2@example.com', 2);
                    
                        INSERT INTO sala (id, id_sede, numero, capienza) VALUES
                        (1, 1, 1, 100),
                        (2, 1, 2, 150),
                        (3, 2, 1, 120);
                    
                        INSERT INTO slot (id, ora_inizio) VALUES
                        (1, '10:00:00'),
                        (2, '12:00:00');
                    
                        INSERT INTO film (id, titolo, genere, classificazione, durata, locandina, descrizione, is_proiettato) VALUES
                        (1, 'Film A', 'Azione', 'PG-13', 120, 'locandinaA.jpg', 'Descrizione A', true),
                        (2, 'Film B', 'Commedia', 'G', 90, 'locandinaB.jpg', 'Descrizione B', true);
                    
                        INSERT INTO proiezione (id, data, id_film, id_sala, id_orario) VALUES
                        (1, '2025-01-01', 1, 1, 1),
                        (2, '2025-01-01', 2, 2, 2);
                    """;
            stmt.execute(setupScript);
        } catch (Exception e) {
            throw new RuntimeException("Errore nel setup del database per i test", e);
        }
    }

    @Test
    void testRetrieveById() {
        Sede sede = sedeDAO.retrieveById(1);
        assertNotNull(sede, "La sede non dovrebbe essere null");
        assertEquals(1, sede.getId(), "L'ID della sede dovrebbe essere 1");
        assertEquals("CineNow Napoli", sede.getNome(), "Il nome della sede dovrebbe essere 'CineNow Napoli'");
        assertEquals("Via Roma, Napoli, 80100", sede.getIndirizzo(), "L'indirizzo della sede dovrebbe essere completo");
        System.out.println("Sede recuperata: ID=" + sede.getId() + ", Nome=" + sede.getNome() + ", Indirizzo=" + sede.getIndirizzo());
    }

    @Test
    void testRetrieveAll() {
        List<Sede> sedi = sedeDAO.retrieveAll();
        assertEquals(2, sedi.size(), "Dovrebbero esserci 2 sedi");
        for (Sede sede : sedi) {
            System.out.println("Sede recuperata: ID=" + sede.getId() + ", Nome=" + sede.getNome() + ", Indirizzo=" + sede.getIndirizzo());
        }
    }

    @Test
    void testRetrieveSaleBySede() {
        List<Sala> sale = sedeDAO.retrieveSaleBySede(1);
        assertEquals(2, sale.size(), "Dovrebbero esserci 2 sale per la sede 1");
        for (Sala sala : sale) {
            System.out.println("Sala recuperata: ID=" + sala.getId() + ", Numero=" + sala.getNumeroSala() + ", Capienza=" + sala.getCapienza());
        }
    }

    @Test
    void testRetrieveByGestoreEmail() {
        Sede sede = sedeDAO.retrieveByGestoreEmail("gestore1@example.com");
        assertNotNull(sede, "La sede non dovrebbe essere null");
        assertEquals(1, sede.getId());
        assertEquals("CineNow Napoli", sede.getNome());
        System.out.println("Sede recuperata per gestore: ID=" + sede.getId() + ", Nome=" + sede.getNome());
    }

    @Test
    void testRetrieveFilm() throws SQLException {
        List<Film> filmList = sedeDAO.retrieveFilm(1);
        assertEquals(2, filmList.size(), "Dovrebbero esserci 2 film associati alla sede 1");
        for (Film film : filmList) {
            System.out.println("Film recuperato: ID=" + film.getId() + ", Titolo=" + film.getTitolo() + ", Genere=" + film.getGenere());
        }
    }
}
