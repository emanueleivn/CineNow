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
        System.out.println("Test Titolo Non Fornito: Titolo=" + titolo);
        byte[] locandina = "Esempio di locandina".getBytes();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            catalogoService.addFilmCatalogo(titolo, 120, "Descrizione valida", locandina, "Fantascienza", "T");
        });
        assertEquals("Parametri non validi per l'aggiunta del film.", exception.getMessage());
    }

    @Test
    void testTitoloInvalido() {
        String titolo = "invalid<>title";
        System.out.println("Test Titolo Invalido: Titolo=" + titolo);
        byte[] locandina = "Locandina di test".getBytes();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            catalogoService.addFilmCatalogo(titolo, 120, "Descrizione valida", locandina, "Fantascienza", "T");
        });
        assertEquals("Parametri non validi per l'aggiunta del film.", exception.getMessage());
    }

    @Test
    void testDescrizioneVuota() {
        String descrizione = "";
        System.out.println("Test Descrizione Vuota: Descrizione=" + descrizione);
        byte[] locandina = "Locandina di Test".getBytes();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            catalogoService.addFilmCatalogo("Titolo valido", 120, descrizione, locandina, "Fantascienza", "T");
        });
        assertEquals("Parametri non validi per l'aggiunta del film.", exception.getMessage());
    }

    @Test
    void testDescrizioneInvalida() {
        String descrizione = "descrizione<>nonvalida";
        System.out.println("Test Descrizione Invalida: Descrizione=" + descrizione);
        byte[] locandina = "Locandina di test".getBytes();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            catalogoService.addFilmCatalogo("Titolo valido", 120, descrizione, locandina, "Fantascienza", "T");
        });
        assertEquals("Parametri non validi per l'aggiunta del film.", exception.getMessage());
    }

    @Test
    void testDurataNonValida() {
        int durata = 0;
        System.out.println("Test Durata Non Valida: Durata=" + durata);
        byte[] locandina = "Locandina di Test".getBytes();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            catalogoService.addFilmCatalogo("Titolo valido", durata, "Descrizione valida", locandina, "Fantascienza", "T");
        });
        assertEquals("Parametri non validi per l'aggiunta del film.", exception.getMessage());
    }

    @Test
    void testDurataNonSelezionata() {
        System.out.println("Test Durata Non Selezionata: Durata= ");
        byte[] locandina = "Locandina di test".getBytes();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            catalogoService.addFilmCatalogo("Titolo valido", -1, "Descrizione valida", locandina, "Fantascienza", "T");
        });
        assertEquals("Parametri non validi per l'aggiunta del film.", exception.getMessage());
    }
    @Test
    void testLocandinaNonSInserita() {
        System.out.println("Test Locandina Non Inserita: Locandina= null ");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            catalogoService.addFilmCatalogo("Titolo valido", 50, "Descrizione valida", null, "Fantascienza", "T");
        });
        assertEquals("Parametri non validi per l'aggiunta del film.", exception.getMessage());
    }

    @Test
    void testGenereNonSelezionato() {
        String genere = "";
        System.out.println("Test Genere Non Selezionato: Genere=" + genere);
        byte[] locandina = "Locandina di test".getBytes();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            catalogoService.addFilmCatalogo("Titolo valido", 120, "Descrizione valida", locandina, genere, "T");
        });
        assertEquals("Parametri non validi per l'aggiunta del film.", exception.getMessage());
    }

    @Test
    void testClassificazioneNonSelezionata() {
        String classificazione = "";
        System.out.println("Test Classificazione Non Selezionata: Classificazione=" + classificazione);
        byte[] locandina = "Esempio di locandina".getBytes();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            catalogoService.addFilmCatalogo("Titolo valido", 120, "Descrizione valida", locandina, "Fantascienza", classificazione);
        });
        assertEquals("Parametri non validi per l'aggiunta del film.", exception.getMessage());
    }

    @Test
    void testFilmAggiuntoConSuccesso() {
        String titolo = "Inception";
        int durata = 120;
        String descrizione = "Thriller psicologico";
        byte[] locandina = "Locandina di test".getBytes();
        String genere = "Fantascienza";
        String classificazione = "T";
        System.out.println("Test Film Aggiunto Con Successo: Titolo=" + titolo +", Descrizione="+descrizione+ ", Durata=" + durata + ", Genere=" + genere + ", Classificazione=" + classificazione+ ", Locandina=inception.jpg");
        catalogoService.addFilmCatalogo(titolo, durata, descrizione, locandina, genere, classificazione);
        List<Film> catalogo = catalogoService.getCatalogo();
        assertNotNull(catalogo, "Il catalogo non dovrebbe essere null");
        assertEquals(1, catalogo.size(), "Il catalogo dovrebbe contenere un film");
        Film film = catalogo.getFirst();
        System.out.println("Film aggiunto: Titolo=" + film.getTitolo() + ", Durata=" + film.getDurata() + ", Genere=" + film.getGenere() + ", Classificazione=" + film.getClassificazione());
        assertEquals(titolo, film.getTitolo(), "Il titolo del film non corrisponde");
        assertEquals(durata, film.getDurata(), "La durata del film non corrisponde");
        assertEquals(descrizione, film.getDescrizione(), "La descrizione del film non corrisponde");
        assertArrayEquals(locandina, film.getLocandina(), "La locandina del film non corrisponde");
        assertEquals(genere, film.getGenere(), "Il genere del film non corrisponde");
        assertEquals(classificazione, film.getClassificazione(), "La classificazione del film non corrisponde");
    }
}
