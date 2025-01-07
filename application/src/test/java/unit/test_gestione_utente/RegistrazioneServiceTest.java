package unit.test_gestione_utente;

import it.unisa.application.model.entity.Cliente;
import it.unisa.application.sottosistemi.gestione_utente.service.RegistrazioneService;
import it.unisa.application.model.dao.ClienteDAO;
import it.unisa.application.model.dao.UtenteDAO;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegistrazioneServiceTest {

    private RegistrazioneService registrazioneService;
    private ClienteDAO clienteDAOMock;
    private UtenteDAO utenteDAOMock;

    @BeforeEach
    void setupService() {
        clienteDAOMock = Mockito.mock(ClienteDAO.class);
        utenteDAOMock = Mockito.mock(UtenteDAO.class);
        registrazioneService = new RegistrazioneService(utenteDAOMock, clienteDAOMock);
    }

    @Test
    void testEmailFormatoNonValido() {
        Cliente result = registrazioneService.registrazione("example@it", "Password123!", "Mario", "Rossi");
        assertNull(result, "Registrazione dovrebbe fallire per email in formato errato");
        System.out.println("Email: example@<> , password: Password123!, nome: Mario, cognome: Rossi");
    }

    @Test
    void testEmailEsistente() {
        Mockito.when(utenteDAOMock.retrieveByEmail("mariorossi@gmail.com")).thenReturn(new Cliente());
        Cliente result = registrazioneService.registrazione("mariorossi@gmail.com", "Password123!", "Mario", "Rossi");
        assertNull(result, "Registrazione dovrebbe fallire per email già registrata");
        System.out.println("Email: mariorossi@gmail.com, password: Password123!, nome: Mario, cognome: Rossi");
    }

    @Test
    void testEmailNonFornita() {
        Cliente result = registrazioneService.registrazione(null, "Password123!", "Mario", "Rossi");
        Cliente result1 = registrazioneService.registrazione("", "Password123!", "Mario", "Rossi");
        assertNull(result, "Registrazione dovrebbe fallire per email mancante");
        assertNull(result1, "Registrazione dovrebbe fallire per email mancante");
        System.out.println("Email: , password: Password123!, nome: Mario, cognome: Rossi");
        System.out.println("Email: \" \", password: Password123!, nome: Mario, cognome: Rossi");
    }

    @Test
    void testPasswordFormatoNonValido() {
        Cliente result = registrazioneService.registrazione("test@example.com", "pass<", "Mario", "Rossi");
        assertNull(result, "Registrazione dovrebbe fallire per password in formato errato");
        System.out.println("Email: test@example.com, password: pass<, nome: Mario, cognome: Rossi");
    }

    @Test
    void testPasswordNonFornita() {
        Cliente result = registrazioneService.registrazione("test@example.com", null, "Mario", "Rossi");
        Cliente result1 = registrazioneService.registrazione("test@example.com", "", "Mario", "Rossi");
        assertNull(result, "Registrazione dovrebbe fallire per password mancante");
        assertNull(result1, "Registrazione dovrebbe fallire per password mancante");
        System.out.println("Email: test@example.com, password: pass<, nome: Mario, cognome: Rossi");
        System.out.println("Email: test@example.com, password: \"\", nome: Mario, cognome: Rossi");
    }

    @Test
    void testNomeFormatoNonValido() {
        Cliente result = registrazioneService.registrazione("test@example.com", "Password123!", "Mario<", "Rossi");
        assertNull(result, "Registrazione dovrebbe fallire per nome in formato errato");
        System.out.println("Email: test@example.com, password: Password123!, nome: <, cognome: Rossi");
    }

    @Test
    void testNomeNonFornito() {
        Cliente result = registrazioneService.registrazione("test@example.com", "Password123!", null, "Rossi");
        Cliente result1 = registrazioneService.registrazione("test@example.com", "Password123!", "", "Rossi");
        assertNull(result, "Registrazione dovrebbe fallire per nome mancante");
        assertNull(result1, "Registrazione dovrebbe fallire per nome mancante");
        System.out.println("Email: test@example.com, password: Password123!, nome: null, cognome: Rossi");
        System.out.println("Email: test@example.com, password: Password123!, nome: \" \", cognome: Rossi");
    }

    @Test
    void testCognomeFormatoNonValido() {
        Cliente result = registrazioneService.registrazione("test@example.com", "Password123!", "Mario", "Rossi<");
        assertNull(result, "Registrazione dovrebbe fallire per cognome in formato errato");
        System.out.println("Email: test@example.com, password: Password123!, nome: Mario, cognome: Rossi<");
    }

    @Test
    void testCognomeNonFornito() {
        Cliente result = registrazioneService.registrazione("test@example.com", "Password123!", "Mario", null);
        Cliente result1 = registrazioneService.registrazione("test@example.com", "Password123!", "Mario", "");
        assertNull(result, "Registrazione dovrebbe fallire per cognome mancante");
        assertNull(result1, "Registrazione dovrebbe fallire per cognome mancante");
        System.out.println("Email: test@example.com, password: Password123!, nome: Mario, cognome: null");
        System.out.println("Email: test@example.com, password: Password123!, nome: Mario, cognome: \" \"");
    }

    @Test
    void testRegistrazioneEffettuata() {
        Mockito.when(utenteDAOMock.retrieveByEmail("test@example.com")).thenReturn(null);
        Mockito.when(clienteDAOMock.create(Mockito.any(Cliente.class))).thenReturn(true);
        Cliente result = registrazioneService.registrazione("test@example.com", "ValidPassword123!", "Mario", "Rossi");
        assertNotNull(result, "Registrazione dovrebbe avere successo");
        assertEquals("test@example.com", result.getEmail(), "L'email registrata non corrisponde");
        assertEquals("Mario", result.getNome(), "Il nome registrato non corrisponde");
        assertEquals("Rossi", result.getCognome(), "Il cognome registrato non corrisponde");
        System.out.println("Email: test@example.com, password: ValidPassword123!, nome: Mario, cognome: Rossi");
    }
}
