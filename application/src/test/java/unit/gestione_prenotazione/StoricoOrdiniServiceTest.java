package unit.gestione_prenotazione;

import it.unisa.application.model.dao.PrenotazioneDAO;
import it.unisa.application.model.entity.Cliente;
import it.unisa.application.model.entity.Prenotazione;
import it.unisa.application.sottosistemi.gestione_prenotazione.service.StoricoOrdiniService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StoricoOrdiniServiceTest {

    private StoricoOrdiniService storicoOrdiniService;
    private PrenotazioneDAO prenotazioneDAOMock;

    @BeforeEach
    void setUp() {
        prenotazioneDAOMock = mock(PrenotazioneDAO.class);
        storicoOrdiniService = new StoricoOrdiniService(prenotazioneDAOMock);
    }

    @Test
    void testStoricoOrdiniSuccess() {
        Cliente cliente = new Cliente("test@example.com", "password", "Mario", "Rossi");

        Prenotazione prenotazione1 = new Prenotazione();
        prenotazione1.setId(1);
        Prenotazione prenotazione2 = new Prenotazione();
        prenotazione2.setId(2);

        List<Prenotazione> expectedPrenotazioni = Arrays.asList(prenotazione1, prenotazione2);
        when(prenotazioneDAOMock.retrieveAllByCliente(cliente)).thenReturn(expectedPrenotazioni);

        // Call the method under test
        List<Prenotazione> result = storicoOrdiniService.storicoOrdini(cliente);

        // Assertions
        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "The size of the result should match expected");
        assertEquals(expectedPrenotazioni, result, "The returned list should match the expected list");

        // Verify interaction with the mock
        verify(prenotazioneDAOMock).retrieveAllByCliente(cliente);
        verifyNoMoreInteractions(prenotazioneDAOMock);
    }


    @Test
    void testStoricoOrdiniEmpty() {
        Cliente cliente = new Cliente("test@example.com", "password", "Mario", "Rossi");

        when(prenotazioneDAOMock.retrieveAllByCliente(cliente)).thenReturn(Collections.emptyList());

        List<Prenotazione> result = storicoOrdiniService.storicoOrdini(cliente);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(prenotazioneDAOMock).retrieveAllByCliente(cliente);
    }

    @Test
    void testStoricoOrdiniNullCliente() {
        assertThrows(IllegalArgumentException.class, () -> storicoOrdiniService.storicoOrdini(null));
        verifyNoInteractions(prenotazioneDAOMock);
    }
}
