package unit.test_DAO;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.dao.SalaDAO;
import it.unisa.application.model.entity.Film;
import it.unisa.application.model.entity.Sala;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SalaDAOTest {

    private static SalaDAO salaDAO;

    @BeforeAll
    static void globalSetup() {
        DatabaseSetupForTest.configureH2DataSource();
        salaDAO = new SalaDAO();
        initializeTestData();
    }

    private static void initializeTestData() {
        try (Connection conn = DataSourceSingleton.getInstance().getConnection()) {
            String insertData = """
                        INSERT INTO sede (id, nome, via, città, cap) VALUES 
                            (1, 'Sede 1', 'Via Roma', 'Napoli', '80100');
                    
                        INSERT INTO sala (id, id_sede, numero, capienza) VALUES 
                            (1, 1, 1, 100),
                            (2, 1, 2, 150);
                    
                        INSERT INTO film (id, titolo, genere, classificazione, durata, locandina, descrizione, is_proiettato) VALUES 
                            (1, 'Film 1', 'Azione', 'PG-13', 120, 'locandina1.jpg', 'Descrizione Film 1', true),
                            (2, 'Film 2', 'Commedia', 'G', 90, 'locandina2.jpg', 'Descrizione Film 2', true);
                    
                        INSERT INTO slot (id, ora_inizio) VALUES 
                            (1, '18:00:00');
                    
                        INSERT INTO proiezione (id, data, id_film, id_sala, id_orario) VALUES 
                            (1, '2025-01-01', 1, 1, 1),
                            (2, '2025-01-02', 2, 2, 1);
                    """;
            try (PreparedStatement ps = conn.prepareStatement(insertData)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore nell'inizializzazione dei dati di test", e);
        }
    }


    @Test
    void testRetrieveAll() {
        try {
            List<Sala> sale = salaDAO.retrieveAll();
            assertNotNull(sale);
            assertEquals(2, sale.size(), "Il numero di sale dovrebbe essere 2");

            Sala sala1 = sale.getFirst();
            assertEquals(1, sala1.getId());
            assertEquals(1, sala1.getId());
            assertEquals(1, sala1.getNumeroSala());
            assertEquals(100, sala1.getCapienza());
        } catch (SQLException e) {
            fail("Errore durante il test di retrieveAll: " + e.getMessage());
        }
    }


}
