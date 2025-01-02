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
        Film film = new Film(0, "Titanic", "Drammatico", "PG-13", 195, "titanic.jpg", "Un dramma romantico epico", true);
        boolean result = filmDAO.create(film);
        assertTrue(result, "La creazione del film dovrebbe avere successo");
        assertNotEquals(0, film.getId(), "L'ID del film dovrebbe essere generato automaticamente");
    }

    @Test
    @DisplayName("Recupero di un film tramite ID")
    void testRetrieveById() {
        Film film = new Film(0, "Titanic", "Drammatico", "PG-13", 195, "titanic.jpg", "Un dramma romantico epico", true);
        filmDAO.create(film);
        Film retrievedFilm = filmDAO.retrieveById(film.getId());
        assertNotNull(retrievedFilm, "Il film dovrebbe essere trovato");
        assertEquals(film.getId(), retrievedFilm.getId(), "L'ID del film dovrebbe corrispondere");
        assertEquals(film.getTitolo(), retrievedFilm.getTitolo(), "Il titolo dovrebbe corrispondere");
        assertEquals(film.getGenere(), retrievedFilm.getGenere(), "Il genere dovrebbe corrispondere");
        assertEquals(film.getClassificazione(), retrievedFilm.getClassificazione(), "La classificazione dovrebbe corrispondere");
        assertEquals(film.getDurata(), retrievedFilm.getDurata(), "La durata dovrebbe corrispondere");
        assertEquals(film.getLocandina(), retrievedFilm.getLocandina(), "La locandina dovrebbe corrispondere");
        assertEquals(film.getDescrizione(), retrievedFilm.getDescrizione(), "La descrizione dovrebbe corrispondere");
        assertEquals(film.isProiettato(), retrievedFilm.isProiettato(), "Lo stato 'isProiettato' dovrebbe corrispondere");
    }

    @Test
    @DisplayName("Recupero di tutti i film")
    void testRetrieveAll() {
        Film film1 = new Film(0, "Titanic", "Drammatico", "PG-13", 195, "titanic.jpg", "Un dramma romantico", true);
        Film film2 = new Film(1, "Inception", "Fantascienza", "PG-13", 148, "inception.jpg", "Un thriller psicologico", false);
        filmDAO.create(film1);
        filmDAO.create(film2);
        List<Film> films = filmDAO.retrieveAll();
        assertNotNull(films, "La lista di film non dovrebbe essere nulla");
        assertEquals(2, films.size(), "Dovrebbero esserci due film nella lista");
        Film retrievedFilm1 = films.get(0);
        assertEquals(film1.getTitolo(), retrievedFilm1.getTitolo(), "Il titolo del primo film dovrebbe corrispondere");
        Film retrievedFilm2 = films.get(1);
        assertEquals(film2.getTitolo(), retrievedFilm2.getTitolo(), "Il titolo del secondo film dovrebbe corrispondere");
    }
}
