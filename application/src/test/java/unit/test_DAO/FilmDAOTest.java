package unit.test_DAO;

import static org.junit.jupiter.api.Assertions.*;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.dao.FilmDAO;
import it.unisa.application.model.entity.Film;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class FilmDAOTest {

    private FilmDAO filmDAO;

    @BeforeAll
    static void globalSetup() {
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        filmDAO = new FilmDAO();
        try (Connection conn = DataSourceSingleton.getInstance().getConnection()) {
            conn.createStatement().execute("DELETE FROM film;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        try (Connection conn = DataSourceSingleton.getInstance().getConnection()) {
            conn.createStatement().execute("DELETE FROM film;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Creazione di un film")
    void testCreateFilm() {
        byte[] locandina = "Esempio di locandina".getBytes(); // Locandina mock come byte[]
        Film film = new Film(0, "Titanic", "Drammatico", "PG-13", 195, locandina, "Un dramma romantico epico", true);
        boolean result = filmDAO.create(film);
        assertTrue(result, "La creazione del film dovrebbe avere successo");
        assertNotEquals(0, film.getId(), "L'ID del film dovrebbe essere generato automaticamente");
    }


    @Test
    @DisplayName("Recupero di un film tramite ID")
    void testRetrieveById() {
        byte[] locandina = "Esempio di locandina".getBytes(); // Locandina mock come byte[]
        Film film = new Film(0, "Titanic", "Drammatico", "PG-13", 195, locandina, "Un dramma romantico epico", true);
        filmDAO.create(film);
        Film retrievedFilm = filmDAO.retrieveById(film.getId());
        assertNotNull(retrievedFilm, "Il film dovrebbe essere trovato");
        assertEquals(film.getId(), retrievedFilm.getId(), "L'ID del film dovrebbe corrispondere");
        assertEquals(film.getTitolo(), retrievedFilm.getTitolo(), "Il titolo dovrebbe corrispondere");
        assertEquals(film.getGenere(), retrievedFilm.getGenere(), "Il genere dovrebbe corrispondere");
        assertEquals(film.getClassificazione(), retrievedFilm.getClassificazione(), "La classificazione dovrebbe corrispondere");
        assertEquals(film.getDurata(), retrievedFilm.getDurata(), "La durata dovrebbe corrispondere");
        assertArrayEquals(film.getLocandina(), retrievedFilm.getLocandina(), "La locandina dovrebbe corrispondere"); // Confronto degli array di byte
        assertEquals(film.getDescrizione(), retrievedFilm.getDescrizione(), "La descrizione dovrebbe corrispondere");
        assertEquals(film.isProiettato(), retrievedFilm.isProiettato(), "Lo stato 'isProiettato' dovrebbe corrispondere");
    }

    @Test
    @DisplayName("Recupero di tutti i film")
    void testRetrieveAll() {
        byte[] locandina1 = "Esempio di locandina 1".getBytes(); // Locandina mock 1 come byte[]
        byte[] locandina2 = "Esempio di locandina 2".getBytes(); // Locandina mock 2 come byte[]
        Film film1 = new Film(0, "Titanic", "Drammatico", "PG-13", 195, locandina1, "Un dramma romantico", true);
        Film film2 = new Film(0, "Inception", "Fantascienza", "PG-13", 148, locandina2, "Un thriller psicologico", false);
        filmDAO.create(film1);
        filmDAO.create(film2);
        List<Film> films = filmDAO.retrieveAll();
        assertNotNull(films, "La lista di film non dovrebbe essere nulla");
        assertEquals(2, films.size(), "Dovrebbero esserci due film nella lista");

        Film retrievedFilm1 = films.stream().filter(f -> "Titanic".equals(f.getTitolo())).findFirst().orElse(null);
        assertNotNull(retrievedFilm1, "Il primo film dovrebbe essere trovato");
        assertArrayEquals(locandina1, retrievedFilm1.getLocandina(), "La locandina del primo film dovrebbe corrispondere");

        Film retrievedFilm2 = films.stream().filter(f -> "Inception".equals(f.getTitolo())).findFirst().orElse(null);
        assertNotNull(retrievedFilm2, "Il secondo film dovrebbe essere trovato");
        assertArrayEquals(locandina2, retrievedFilm2.getLocandina(), "La locandina del secondo film dovrebbe corrispondere");
    }

}
