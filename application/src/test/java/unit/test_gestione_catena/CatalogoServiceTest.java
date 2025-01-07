package unit.test_gestione_catena;

import static org.junit.jupiter.api.Assertions.*;
import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.Film;
import it.unisa.application.sottosistemi.gestione_catena.service.CatalogoService;
import org.junit.jupiter.api.*;
import unit.test_DAO.DatabaseSetupForTest;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CatalogoServiceTest {
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
    void testTitoloNonFornito() {
        String titolo = null;
        System.out.println("Titolo: " + titolo);
        byte[] locandina = "Esempio di locandina".getBytes();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            catalogoService.addFilmCatalogo(titolo, 120, "Descrizione valida", locandina, "Fantascienza", "PG-13");
        });
        assertEquals("Parametri non validi per l'aggiunta del film.", exception.getMessage());
    }

    @Test
    void testTitoloVuoto() {
        String titolo = "";
        System.out.println("Titolo: " + titolo);
        byte[] locandina = "Esempio di locandina".getBytes();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            catalogoService.addFilmCatalogo(titolo, 120, "Descrizione valida", locandina, "Fantascienza", "PG-13");
        });
        assertEquals("Parametri non validi per l'aggiunta del film.", exception.getMessage());
    }

    @Test
    void testTitoloInvalido() {
        String titolo = "invalid<>title";
        System.out.println("Titolo: " + titolo);
        byte[] locandina = "Esempio di locandina".getBytes();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            catalogoService.addFilmCatalogo(titolo, 120, "Descrizione valida", locandina, "Fantascienza", "PG-13");
        });
        assertEquals("Parametri non validi per l'aggiunta del film.", exception.getMessage());
    }

    @Test
    void testDescrizioneVuota() {
        String descrizione = "";
        System.out.println("Descrizione: " + descrizione);
        byte[] locandina = "Esempio di locandina".getBytes();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            catalogoService.addFilmCatalogo("Titolo valido", 120, descrizione, locandina, "Fantascienza", "PG-13");
        });
        assertEquals("Parametri non validi per l'aggiunta del film.", exception.getMessage());
    }

    @Test
    void testDescrizioneInvalida() {
        String descrizione = "descrizione<>nonvalida";
        System.out.println("Descrizione: " + descrizione);
        byte[] locandina = "Esempio di locandina".getBytes();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            catalogoService.addFilmCatalogo("Titolo valido", 120, descrizione, locandina, "Fantascienza", "PG-13");
        });
        assertEquals("Parametri non validi per l'aggiunta del film.", exception.getMessage());
    }

    @Test
    void testDurataNonValida() {
        int durata = 0;
        System.out.println("Durata: " + durata);
        byte[] locandina = "Esempio di locandina".getBytes();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            catalogoService.addFilmCatalogo("Titolo valido", durata, "Descrizione valida", locandina, "Fantascienza", "PG-13");
        });
        assertEquals("Parametri non validi per l'aggiunta del film.", exception.getMessage());
    }

    @Test
    void testGenereNonSelezionato() {
        String genere = "";
        System.out.println("Genere: " + genere);
        byte[] locandina = "Esempio di locandina".getBytes();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            catalogoService.addFilmCatalogo("Titolo valido", 120, "Descrizione valida", locandina, genere, "PG-13");
        });
        assertEquals("Parametri non validi per l'aggiunta del film.", exception.getMessage());
    }

    @Test
    void testClassificazioneNonSelezionata() {
        String classificazione = "";
        System.out.println("Classificazione: " + classificazione);
        byte[] locandina = "Esempio di locandina".getBytes();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            catalogoService.addFilmCatalogo("Titolo valido", 120, "Descrizione valida", locandina, "Fantascienza", classificazione);
        });
        assertEquals("Parametri non validi per l'aggiunta del film.", exception.getMessage());
    }

    @Test
    void testFilmAggiuntoConSuccesso() {
        String titolo = "Titolo valido";
        int durata = 120;
        String descrizione = "Descrizione valida";
        byte[] locandina = "Esempio di locandina".getBytes();
        String genere = "Fantascienza";
        String classificazione = "PG-13";
        System.out.println("Titolo: " + titolo + ", Durata: " + durata + ", Locandina: locandina.jpg "+", Descrizione: " + descrizione + ", Genere: " + genere + ", Classificazione: " + classificazione);
        catalogoService.addFilmCatalogo(titolo, durata, descrizione, locandina, genere, classificazione);
        List<Film> catalogo = catalogoService.getCatalogo();
        assertNotNull(catalogo, "Il catalogo non dovrebbe essere null");
        assertEquals(1, catalogo.size(), "Il catalogo dovrebbe contenere un film");
        Film film = catalogo.getFirst();
        assertEquals(titolo, film.getTitolo(), "Il titolo del film non corrisponde");
        assertEquals(durata, film.getDurata(), "La durata del film non corrisponde");
        assertEquals(descrizione, film.getDescrizione(), "La descrizione del film non corrisponde");
        assertArrayEquals(locandina, film.getLocandina(), "La locandina del film non corrisponde");
        assertEquals(genere, film.getGenere(), "Il genere del film non corrisponde");
        assertEquals(classificazione, film.getClassificazione(), "La classificazione del film non corrisponde");
    }
}
