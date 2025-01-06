package unit.test_gestione_accesso;

import it.unisa.application.model.entity.Cliente;
import it.unisa.application.sottosistemi.gestione_utente.service.RegistrazioneService;
import org.junit.jupiter.api.*;
import unit.test_DAO.DatabaseSetupForTest;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegistrazioneServiceTest {

    private RegistrazioneService registrazioneService;

    @BeforeAll
    void setupDatabase() {
        DatabaseSetupForTest.configureH2DataSource();
    }

    @BeforeEach
    void setupService() {
        registrazioneService = new RegistrazioneService();
    }

    @Test
    void testInvalidEmailFormat() {
        Cliente result = registrazioneService.registrazione("example@<>", "Password123!", "Mario", "Rossi");
        assertNull(result, "Registrazione dovrebbe fallire per email in formato errato");
    }

    @Test
    void testEmailAlreadyExists() {
        // Simula l'inserimento di un cliente esistente
        registrazioneService.registrazione("mariorossi@gmail.com", "Password123!", "Mario", "Rossi");

        Cliente result = registrazioneService.registrazione("mariorossi@gmail.com", "Password123!", "Mario", "Rossi");
        assertNull(result, "Registrazione dovrebbe fallire per email già registrata");
    }

    @Test
    void testEmailNotProvided() {
        Cliente result = registrazioneService.registrazione(null, "Password123!", "Mario", "Rossi");
        assertNull(result, "Registrazione dovrebbe fallire per email mancante");
    }

    @Test
    void testInvalidPasswordFormat() {
        Cliente result = registrazioneService.registrazione("test@example.com", "short", "Mario", "Rossi");
        assertNull(result, "Registrazione dovrebbe fallire per password in formato errato");
    }

    @Test
    void testPasswordNotProvided() {
        Cliente result = registrazioneService.registrazione("test@example.com", null, "Mario", "Rossi");
        assertNull(result, "Registrazione dovrebbe fallire per password mancante");
    }

    @Test
    void testInvalidNameFormat() {
        Cliente result = registrazioneService.registrazione("test@example.com", "Password123!", "<", "Rossi");
        assertNull(result, "Registrazione dovrebbe fallire per nome in formato errato");
    }

    @Test
    void testNameNotProvided() {
        Cliente result = registrazioneService.registrazione("test@example.com", "Password123!", null, "Rossi");
        assertNull(result, "Registrazione dovrebbe fallire per nome mancante");
    }

    @Test
    void testInvalidSurnameFormat() {
        Cliente result = registrazioneService.registrazione("test@example.com", "Password123!", "Mario", " ");
        assertNull(result, "Registrazione dovrebbe fallire per cognome in formato errato");
    }

    @Test
    void testSurnameNotProvided() {
        Cliente result = registrazioneService.registrazione("test@example.com", "Password123!", "Mario", null);
        assertNull(result, "Registrazione dovrebbe fallire per cognome mancante");
    }

    @Test
    void testSuccessfulRegistration() {
        Cliente result = registrazioneService.registrazione("test@example.com", "ValidPassword123!", "Mario", "Rossi");
        assertNotNull(result, "Registrazione dovrebbe avere successo");
        assertEquals("test@example.com", result.getEmail(), "L'email registrata non corrisponde");
        assertEquals("Mario", result.getNome(), "Il nome registrato non corrisponde");
        assertEquals("Rossi", result.getCognome(), "Il cognome registrato non corrisponde");
    }
}
