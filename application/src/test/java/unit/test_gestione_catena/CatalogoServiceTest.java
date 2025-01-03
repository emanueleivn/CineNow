package unit.test_gestione_catena;

import static org.junit.jupiter.api.Assertions.*;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.Film;
import it.unisa.application.sottosistemi.gestione_catena.service.CatalogoService;
import org.junit.jupiter.api.*;
import unit.test_DAO.DatabaseSetupForTest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
    @DisplayName("TC04.1 - Titolo non valido")
    void testTitoloNonValido() {
        byte[] locandina = "Esempio di locandina".getBytes(); // Mock della locandina come byte[]
        List<String> invalidTitles = new ArrayList<>();
        invalidTitles.add(null);
        invalidTitles.add("");
        invalidTitles.add("invalid<>title");
        for (String titolo : invalidTitles) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                catalogoService.addFilmCatalogo(titolo, 120, "Descrizione valida", locandina, "Fantascienza", "PG-13");
            });
            assertEquals("Parametri non validi per l'aggiunta del film.", exception.getMessage(),
                    "Il test con titolo non valido '" + titolo + "' dovrebbe fallire");
        }
    }


    @Test
    @DisplayName("TC04.2 - Descrizione non valida")
    void testDescrizioneNonValida() {
        byte[] locandina = "Esempio di locandina".getBytes(); // Mock della locandina come byte[]
        List<String> invalidDescriptions = new ArrayList<>();
        invalidDescriptions.add(null);
        invalidDescriptions.add("");
        invalidDescriptions.add("descrizione<>nonvalida");

        for (String descrizione : invalidDescriptions) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                catalogoService.addFilmCatalogo("Titolo valido", 120, descrizione, locandina, "Fantascienza", "PG-13");
            });
            assertEquals("Parametri non validi per l'aggiunta del film.", exception.getMessage(),
                    "Il test con descrizione non valida '" + descrizione + "' dovrebbe fallire");
        }
    }


    @Test
    @DisplayName("TC04.3 - Durata non inserita")
    void testDurataNonInserita() {
        byte[] locandina = "Esempio di locandina".getBytes(); // Mock della locandina come byte[]
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            catalogoService.addFilmCatalogo("Titolo valido", 0, "Descrizione valida", locandina, "Fantascienza", "PG-13");
        });
        assertEquals("Parametri non validi per l'aggiunta del film.", exception.getMessage());
    }


    @Test
    @DisplayName("TC04.4 - Durata <= 0 minuti")
    void testDurataMinimaNonValida() {
        byte[] locandina = "Esempio di locandina".getBytes(); // Mock della locandina come byte[]
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            catalogoService.addFilmCatalogo("Titolo valido", -1, "Descrizione valida", locandina, "Fantascienza", "PG-13");
        });
        assertEquals("Parametri non validi per l'aggiunta del film.", exception.getMessage());
    }


    @Test
    @DisplayName("TC04.5 - Genere non selezionato")
    void testGenereNonSelezionato() {
        byte[] locandina = "Esempio di locandina".getBytes(); // Mock della locandina come byte[]
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            catalogoService.addFilmCatalogo("Titolo valido", 120, "Descrizione valida", locandina, "", "PG-13");
        });
        assertEquals("Parametri non validi per l'aggiunta del film.", exception.getMessage());
    }

    @Test
    @DisplayName("TC04.6 - Classificazione non selezionata")
    void testClassificazioneNonSelezionata() {
        byte[] locandina = "Esempio di locandina".getBytes(); // Mock della locandina come byte[]
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            catalogoService.addFilmCatalogo("Titolo valido", 120, "Descrizione valida", locandina, "Fantascienza", "");
        });
        assertEquals("Parametri non validi per l'aggiunta del film.", exception.getMessage());
    }

    @Test
    @DisplayName("TC04.7 - Film aggiunto con successo")
    void testFilmAggiuntoConSuccesso() {
        byte[] locandina = "Esempio di locandina".getBytes(); // Locandina simulata come byte[]
        catalogoService.addFilmCatalogo("Titolo valido", 120, "Descrizione valida", locandina, "Fantascienza", "PG-13");
        List<Film> catalogo = catalogoService.getCatalogo();
        assertNotNull(catalogo, "Il catalogo non dovrebbe essere null");
        assertEquals(1, catalogo.size(), "Il catalogo dovrebbe contenere un film");
        Film film = catalogo.getFirst();
        assertEquals("Titolo valido", film.getTitolo(), "Il titolo del film non corrisponde");
        assertEquals(120, film.getDurata(), "La durata del film non corrisponde");
        assertEquals("Descrizione valida", film.getDescrizione(), "La descrizione del film non corrisponde");
        assertArrayEquals(locandina, film.getLocandina(), "La locandina del film non corrisponde");
        assertEquals("Fantascienza", film.getGenere(), "Il genere del film non corrisponde");
        assertEquals("PG-13", film.getClassificazione(), "La classificazione del film non corrisponde");
    }
}
