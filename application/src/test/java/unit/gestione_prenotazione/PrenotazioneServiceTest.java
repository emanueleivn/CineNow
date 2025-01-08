package unit.gestione_prenotazione;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.dao.PostoProiezioneDAO;
import it.unisa.application.model.dao.PrenotazioneDAO;
import it.unisa.application.model.entity.Cliente;
import it.unisa.application.model.entity.Posto;
import it.unisa.application.model.entity.PostoProiezione;
import it.unisa.application.model.entity.Prenotazione;
import it.unisa.application.model.entity.Proiezione;
import it.unisa.application.model.entity.Sala;
import it.unisa.application.sottosistemi.gestione_prenotazione.service.PrenotazioneService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unit.test_DAO.DatabaseSetupForTest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PrenotazioneServiceTest {
    private PrenotazioneService prenotazioneService;
    private PrenotazioneDAO prenotazioneDAOMock;
    private PostoProiezioneDAO postoProiezioneDAOMock;

    @BeforeAll
    static void setupDatabase() {
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setUp() {
        prenotazioneDAOMock = mock(PrenotazioneDAO.class);
        postoProiezioneDAOMock = mock(PostoProiezioneDAO.class);
        prenotazioneService = new PrenotazioneService(prenotazioneDAOMock, postoProiezioneDAOMock);
        try (Connection conn = DataSourceSingleton.getInstance().getConnection()) {
            conn.createStatement().execute("DELETE FROM prenotazione;");
            conn.createStatement().execute("DELETE FROM posto_proiezione;");
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la configurazione iniziale del database", e);
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
        when(prenotazioneDAOMock.create(any(Prenotazione.class))).thenAnswer(invocation -> {
            Prenotazione prenotazioneArg = invocation.getArgument(0);
            prenotazioneArg.setId(100);
            return true;
        });
        when(postoProiezioneDAOMock.occupaPosto(any(PostoProiezione.class), eq(100))).thenReturn(true);
        Prenotazione result = prenotazioneService.aggiungiOrdine(cliente, posti, proiezione);
        assertNotNull(result);
        assertEquals(cliente, result.getCliente());
        assertEquals(proiezione, result.getProiezione());
        assertEquals(posti, result.getPostiPrenotazione());
        System.out.println("Prenotazione creata: Cliente=" + cliente.getEmail() + ", Proiezione ID=" + proiezione.getId() + ", ID Prenotazione=" + result.getId());
        posti.forEach(p -> System.out.println("Posto prenotato: Sala ID=" + p.getPosto().getSala().getId() + ", Fila=" + p.getPosto().getFila() + ", Numero=" + p.getPosto().getNumero()));
        verify(prenotazioneDAOMock).create(any(Prenotazione.class));
        verify(postoProiezioneDAOMock, times(2)).occupaPosto(any(PostoProiezione.class), eq(100));
    }

    @Test
    void testAggiungiOrdineFallimento() {
        System.out.println("Test fallimento causa proiezione");
        Cliente cliente = new Cliente("test@example.com", "password", "Mario", "Rossi");
        Proiezione proiezione = new Proiezione();
        proiezione.setId(1);
        Sala sala = new Sala();
        sala.setId(1);
        Posto posto = new Posto(sala, 'A', 1);
        PostoProiezione postoProiezione = new PostoProiezione(posto, proiezione);
        List<PostoProiezione> posti = Collections.singletonList(postoProiezione);
        when(prenotazioneDAOMock.create(any(Prenotazione.class))).thenReturn(false);
        assertThrows(RuntimeException.class, () ->
                prenotazioneService.aggiungiOrdine(cliente, posti, proiezione)
        );
        System.out.println("Tentativo di creare prenotazione fallito: Cliente=" + cliente.getEmail() + ", Proiezione=" + null);
        verify(prenotazioneDAOMock).create(any(Prenotazione.class));
        verifyNoInteractions(postoProiezioneDAOMock);
    }

    @Test
    void testAggiungiOrdineFailure() {
        System.out.println("Test fallimento causa posti non selezionati");
        Cliente cliente = new Cliente("test@example.com", "password", "Mario", "Rossi");
        Proiezione proiezione = new Proiezione();
        proiezione.setId(1);
        Sala sala = new Sala();
        sala.setId(1);
        Posto posto = new Posto(sala, 'A', 1);
        PostoProiezione postoProiezione = new PostoProiezione(posto, proiezione);
        List<PostoProiezione> posti = Collections.singletonList(postoProiezione);
        when(prenotazioneDAOMock.create(any(Prenotazione.class))).thenAnswer(invocation -> {
            Prenotazione prenotazioneArg = invocation.getArgument(0);
            prenotazioneArg.setId(100);
            return true;
        });
        when(postoProiezioneDAOMock.occupaPosto(any(PostoProiezione.class), eq(100))).thenReturn(false);
        assertThrows(RuntimeException.class, () ->
                prenotazioneService.aggiungiOrdine(cliente, posti, proiezione)
        );
        System.out.println("Tentativo di occupare posto fallito: Sala ID=" + posto.getSala().getId() + ", Fila=" + null + ", Numero=" + null);
        verify(prenotazioneDAOMock).create(any(Prenotazione.class));
        verify(postoProiezioneDAOMock).occupaPosto(any(PostoProiezione.class), eq(100));
    }

    @Test
    void testAggiungiOrdineFailurePostiOccupati() {
        Cliente cliente = new Cliente("test@example.com", "password", "Mario", "Rossi");
        Proiezione proiezione = new Proiezione();
        proiezione.setId(1);
        Sala sala = new Sala();
        sala.setId(1);
        System.out.println("Posti occupati per il testing: T1,T2");
        Posto posto1 = new Posto(sala, 'A', 1);
        Posto posto2 = new Posto(sala, 'A', 2);
        PostoProiezione postoProiezione1 = new PostoProiezione(posto1, proiezione);
        PostoProiezione postoProiezione2 = new PostoProiezione(posto2, proiezione);
        postoProiezione1.setStato(false);
        postoProiezione2.setStato(false);
        List<PostoProiezione> posti = Arrays.asList(postoProiezione1, postoProiezione2);
        when(postoProiezioneDAOMock.retrieveAllByProiezione(proiezione)).thenReturn(posti);
        assertThrows(RuntimeException.class, () ->
                prenotazioneService.aggiungiOrdine(cliente, posti, proiezione)
        );

        System.out.println("Tentativo di prenotazione fallito: Posti già occupati per Proiezione ID=" + proiezione.getId());
        posti.forEach(p -> System.out.println("Posto occupato: Sala ID=" + p.getPosto().getSala().getId() +
                ", Fila=" + p.getPosto().getFila() +
                ", Numero=" + p.getPosto().getNumero()));

        verify(prenotazioneDAOMock, never()).create(any(Prenotazione.class));
        verifyNoInteractions(postoProiezioneDAOMock);
    }

    @Test
    void testOttienimentoPostiProiezione() {
        Proiezione proiezione = new Proiezione();
        proiezione.setId(1);
        Sala sala = new Sala();
        sala.setId(1);
        Posto posto1 = new Posto(sala, 'A', 1);
        Posto posto2 = new Posto(sala, 'A', 2);
        PostoProiezione postoProiezione1 = new PostoProiezione(posto1, proiezione);
        PostoProiezione postoProiezione2 = new PostoProiezione(posto2, proiezione);
        List<PostoProiezione> posti = Arrays.asList(postoProiezione1, postoProiezione2);
        when(postoProiezioneDAOMock.retrieveAllByProiezione(proiezione)).thenReturn(posti);
        List<PostoProiezione> result = prenotazioneService.ottieniPostiProiezione(proiezione);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(posti, result);
        System.out.println("Posti recuperati per Proiezione ID=" + proiezione.getId());
        posti.forEach(p -> System.out.println("Posto: Sala ID=" + p.getPosto().getSala().getId() + ", Fila=" + p.getPosto().getFila() + ", Numero=" + p.getPosto().getNumero()));
        verify(postoProiezioneDAOMock).retrieveAllByProiezione(proiezione);
    }
}
