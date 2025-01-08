package unit.test_gestione_sala;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.dao.*;
import it.unisa.application.model.entity.*;
import it.unisa.application.sottosistemi.gestione_sala.service.ProgrammazioneService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unit.test_DAO.DatabaseSetupForTest;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProgrammazioneServiceTest {
    private ProgrammazioneService programmazioneService;
    private ProiezioneDAO proiezioneDAOMock;
    private FilmDAO filmDAOSpy;
    private SalaDAO salaDAOSpy;
    private SlotDAO slotDAOSpy;

    @BeforeAll
    static void globalSetup() {
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        proiezioneDAOMock = mock(ProiezioneDAO.class);
        filmDAOSpy = spy(new FilmDAO());
        salaDAOSpy = spy(new SalaDAO());
        slotDAOSpy = spy(new SlotDAO());
        programmazioneService = new ProgrammazioneService(proiezioneDAOMock);

        try (Connection conn = DataSourceSingleton.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM proiezione;");
            stmt.execute("DELETE FROM slot;");
            stmt.execute("DELETE FROM sala;");
            stmt.execute("DELETE FROM sede;");
            stmt.execute("DELETE FROM film;");
            stmt.execute("INSERT INTO sede (id, nome, via, città, cap) VALUES (1, 'Movieplex', 'Via Roma', 'Napoli', '80100');");
            stmt.execute("INSERT INTO film (id, titolo, durata, genere, classificazione, descrizione, is_proiettato) " +
                    "VALUES (1, 'Sonic 3', 120, 'Azione', 'T', 'Descrizione di test', true);");
            stmt.execute("INSERT INTO sala (id, numero, capienza, id_sede) VALUES (1, 1, 100, 1);");
            stmt.execute("INSERT INTO slot (id, ora_inizio) VALUES (1, '18:00:00');");
            stmt.execute("INSERT INTO slot (id, ora_inizio) VALUES (2, '18:30:00');");

        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la configurazione del database per i test", e);
        }
    }

    @Test
    void testFilmNonSelezionato() {
        int salaId = 1;
        List<Integer> slotIds = List.of(1, 2);
        LocalDate data = LocalDate.of(2025, 1, 10);
        System.out.println("Test Film Non Selezionato - Input:Film=null, salaId=" + salaId + ", slotIds=" + slotIds + ", data=" + data);
        doReturn(null).when(filmDAOSpy).retrieveById(anyInt());
        boolean result = programmazioneService.aggiungiProiezione(0, salaId, slotIds, data);
        assertFalse(result, "Il test dovrebbe fallire perché il film non è stato selezionato.");
    }

    @Test
    void testDataNonInserita() {
        int filmId = 1;
        int salaId = 1;
        List<Integer> slotIds = List.of(1, 2);
        System.out.println("Test Data Non Inserita - Input:data=null, film=Sonic 3, salaId=" + salaId + ", slotIds=" + slotIds);
        Film film = new Film();
        film.setId(filmId);
        Sala sala = new Sala();
        sala.setId(salaId);
        doReturn(film).when(filmDAOSpy).retrieveById(filmId);
        doReturn(sala).when(salaDAOSpy).retrieveById(salaId);
        boolean result = programmazioneService.aggiungiProiezione(filmId, salaId, slotIds, null);
        assertFalse(result, "Il test dovrebbe fallire perché la data non è stata inserita.");
    }

    @Test
    void testDataPassata() {
        int filmId = 1;
        int salaId = 1;
        List<Integer> slotIds = List.of(1, 2);
        LocalDate dataPassata = LocalDate.of(2023, 1, 1);
        System.out.println("Test Data Passata - Input: filmId= Sonic 3, salaId=" + salaId + ", slotIds=" + slotIds + ", data=" + dataPassata);
        Film film = new Film();
        film.setId(filmId);
        Sala sala = new Sala();
        sala.setId(salaId);
        doReturn(film).when(filmDAOSpy).retrieveById(filmId);
        doReturn(sala).when(salaDAOSpy).retrieveById(salaId);
        boolean result = programmazioneService.aggiungiProiezione(filmId, salaId, slotIds, dataPassata);
        assertFalse(result, "Il test dovrebbe fallire perché la data scelta è passata.");
    }

    @Test
    void testSlotNonDisponibili() {
        int filmId = 1;
        int salaId = 1;
        List<Integer> slotIds = List.of(10, 20);
        LocalDate data = LocalDate.of(2025, 1, 10);
        System.out.println("Test Slot Non Disponibili - Input: film= Sonic 3" + ", salaId=" + salaId + ", slotIds=" + slotIds + ", data=" + data);
        Film film = new Film();
        film.setId(filmId);
        Sala sala = new Sala();
        sala.setId(salaId);
        doReturn(film).when(filmDAOSpy).retrieveById(filmId);
        doReturn(sala).when(salaDAOSpy).retrieveById(salaId);
        doReturn(Collections.emptyList()).when(slotDAOSpy).retrieveAllSlots();
        boolean result = programmazioneService.aggiungiProiezione(filmId, salaId, slotIds, data);
        assertFalse(result, "Il test dovrebbe fallire perché gli slot selezionati non sono disponibili.");
    }

    @Test
    void testProiezioneAggiuntaConSuccesso() {
        int filmId = 1;
        int salaId = 1;
        List<Integer> slotIds = List.of(1, 2);
        LocalDate data = LocalDate.of(2025, 1, 10);
        System.out.println("Test Proiezione Aggiunta Con Successo - Input: film=Sonic 3, salaId=" + salaId + ", slotIds=" + slotIds + ", data=" + data);
        Film film = new Film();
        film.setId(filmId);
        film.setTitolo("Test Film");
        film.setDurata(120);
        Sala sala = new Sala();
        sala.setId(salaId);
        sala.setNumeroSala(1);
        sala.setCapienza(100);
        Slot slot1 = new Slot();
        slot1.setId(1);
        slot1.setOraInizio(java.sql.Time.valueOf("19:00:00"));
        Slot slot2 = new Slot();
        slot2.setId(2);
        slot2.setOraInizio(java.sql.Time.valueOf("19:30:00"));
        doReturn(film).when(filmDAOSpy).retrieveById(filmId);
        doReturn(sala).when(salaDAOSpy).retrieveById(salaId);
        doReturn(List.of(slot1, slot2)).when(slotDAOSpy).retrieveAllSlots();
        when(proiezioneDAOMock.create(any(Proiezione.class))).thenReturn(true);
        boolean result = programmazioneService.aggiungiProiezione(filmId, salaId, slotIds, data);
        assertTrue(result, "Il test dovrebbe passare perché tutti i parametri sono validi.");
    }

    @Test
    void testSalaNull() {
        int filmId = 1;
        int salaId = 2;
        List<Integer> slotIds = List.of(1, 2);
        LocalDate data = LocalDate.of(2025, 1, 10);
        System.out.println("Test Sala Null - Input: filmId=" + filmId + ", sala=null, slots= [19:00,19:30]" + ", data=" + data);
        Film film = new Film();
        film.setId(filmId);
        doReturn(film).when(filmDAOSpy).retrieveById(filmId);
        doReturn(null).when(salaDAOSpy).retrieveById(salaId);
        boolean result = programmazioneService.aggiungiProiezione(filmId, salaId, slotIds, data);
        assertFalse(result, "Il test dovrebbe fallire perché la sala è null.");
    }
}
