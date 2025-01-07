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
        List<Prenotazione> result = storicoOrdiniService.storicoOrdini(cliente);
        assertNotNull(result, "La lista di risultati non dovrebbe essere null");
        assertEquals(2, result.size(), "La dimensione della lista dovrebbe essere 2");
        assertEquals(expectedPrenotazioni, result, "La lista restituita dovrebbe corrispondere a quella attesa");
        System.out.println("Storico ordini per Cliente=" + cliente.getEmail());
        result.forEach(p -> System.out.println("Prenotazione ID=" + p.getId()));
        verify(prenotazioneDAOMock).retrieveAllByCliente(cliente);
        verifyNoMoreInteractions(prenotazioneDAOMock);
    }

    @Test
    void testStoricoOrdiniEmpty() {
        Cliente cliente = new Cliente("test@example.com", "password", "Mario", "Rossi");
        when(prenotazioneDAOMock.retrieveAllByCliente(cliente)).thenReturn(Collections.emptyList());
        List<Prenotazione> result = storicoOrdiniService.storicoOrdini(cliente);
        assertNotNull(result, "La lista di risultati non dovrebbe essere null");
        assertTrue(result.isEmpty(), "La lista dovrebbe essere vuota");
        System.out.println("Storico ordini vuoto per Cliente=" + cliente.getEmail());
        verify(prenotazioneDAOMock).retrieveAllByCliente(cliente);
    }

    @Test
    void testStoricoOrdiniNullCliente() {
        assertThrows(IllegalArgumentException.class, () -> storicoOrdiniService.storicoOrdini(null));
        System.out.println("Tentativo di recuperare storico ordini con Cliente=null");
        verifyNoInteractions(prenotazioneDAOMock);
    }
}
