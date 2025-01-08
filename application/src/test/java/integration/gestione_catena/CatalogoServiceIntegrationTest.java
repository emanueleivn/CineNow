package integration.gestione_catena;

import static org.junit.jupiter.api.Assertions.*;
import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.Film;
import it.unisa.application.sottosistemi.gestione_catena.service.CatalogoService;
import org.junit.jupiter.api.*;
import unit.test_DAO.DatabaseSetupForTest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CatalogoServiceIntegrationTest {
    private CatalogoService catalogoService;

    @BeforeAll
    static void globalSetup() {
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        catalogoService = new CatalogoService();
        try (Connection conn = DataSourceSingleton.getInstance().getConnection()) {
            conn.createStatement().execute("DELETE FROM film;");
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la configurazione del database", e);
        }
    }

    @AfterEach
    void tearDown() {
        try (Connection conn = DataSourceSingleton.getInstance().getConnection()) {
            conn.createStatement().execute("DELETE FROM film;");
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il reset del database", e);
        }
    }

    @Test
    void testFilmAggiuntoConSuccesso() {
        String titolo = "Inception";
        int durata = 120;
        String descrizione = "Thriller psicologico";
        byte[] locandina = "Locandina di test".getBytes();
        String genere = "Fantascienza";
        String classificazione = "T";
        System.out.println("Film di test: Titolo=" + titolo +", Descrizione="+descrizione+ ", Durata=" + durata + ", Genere=" + genere + ", Classificazione=" + classificazione+ ", Locandina=inception.jpg");
        catalogoService.addFilmCatalogo(titolo, durata, descrizione, locandina, genere, classificazione);
        List<Film> catalogo = catalogoService.getCatalogo();
        assertNotNull(catalogo, "Il catalogo non dovrebbe essere null");
        assertEquals(1, catalogo.size(), "Il catalogo dovrebbe contenere un film");
        Film film = catalogo.getFirst();
        System.out.println("Film Aggiunto: Titolo=" + film.getTitolo() +", Descrizione="+descrizione+ ", Durata=" + film.getDurata() + ", Genere=" + film.getGenere() + ", Classificazione=" + film.getClassificazione()+ ", Locandina=inception.jpg");
        assertEquals(titolo, film.getTitolo(), "Il titolo del film non corrisponde");
        assertEquals(durata, film.getDurata(), "La durata del film non corrisponde");
        assertEquals(descrizione, film.getDescrizione(), "La descrizione del film non corrisponde");
        assertArrayEquals(locandina, film.getLocandina(), "La locandina del film non corrisponde");
        assertEquals(genere, film.getGenere(), "Il genere del film non corrisponde");
        assertEquals(classificazione, film.getClassificazione(), "La classificazione del film non corrisponde");
    }

    @Test
    void testFilmNonAggiuntoTitoloNonFornito() {
        String titolo = null;
        int durata = 120;
        String descrizione = "Descrizione valida";
        byte[] locandina = "Esempio di locandina".getBytes();
        String genere = "Fantascienza";
        String classificazione = "PG-13";
        System.out.println("Test Film Non Aggiunto: Titolo=" + titolo);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            catalogoService.addFilmCatalogo(titolo, durata, descrizione, locandina, genere, classificazione);
        });
        assertEquals("Parametri non validi per l'aggiunta del film.", exception.getMessage());
        System.out.println("Test completato: Eccezione catturata con messaggio - " + exception.getMessage());
    }

    @Test
    void testGetCatalogoVuoto() {
        System.out.println("Test Catalogo Vuoto: Nessun film presente nel database.");
        List<Film> catalogo = catalogoService.getCatalogo();
        assertNotNull(catalogo, "Il catalogo non dovrebbe essere null");
        assertTrue(catalogo.isEmpty(), "Il catalogo dovrebbe essere vuoto");
        System.out.println("Test completato: Il catalogo è vuoto.");
    }
}
