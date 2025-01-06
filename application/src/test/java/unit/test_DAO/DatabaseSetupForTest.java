package unit.test_DAO;

import it.unisa.application.database_connection.DataSourceSingleton;
import org.h2.jdbcx.JdbcDataSource;

import java.lang.reflect.Field;
import java.sql.Connection;

public class DatabaseSetupForTest {
    public static void configureH2DataSource() {
        try {
            JdbcDataSource ds = new JdbcDataSource();
            ds.setURL("jdbc:h2:mem:CineNow;DB_CLOSE_DELAY=-1");
            ds.setUser("sa");
            ds.setPassword("");
            Field instanceField = DataSourceSingleton.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, ds);

            try (Connection conn = ds.getConnection()) {
                String schemaCreationScript = """
                    CREATE TABLE IF NOT EXISTS utente (
                        email VARCHAR(255) PRIMARY KEY,
                        password VARCHAR(255) NOT NULL,
                        ruolo VARCHAR(50) NOT NULL
                    );

                    CREATE TABLE IF NOT EXISTS cliente (
                        email VARCHAR(255),
                        nome VARCHAR(255) NOT NULL,
                        cognome VARCHAR(255) NOT NULL,
                        PRIMARY KEY (email),
                        FOREIGN KEY (email) REFERENCES utente(email) 
                            ON DELETE CASCADE 
                            ON UPDATE CASCADE
                    );

                    CREATE TABLE IF NOT EXISTS sede (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        nome VARCHAR(255) NOT NULL,
                        via VARCHAR(255) NOT NULL,
                        città VARCHAR(255) NOT NULL,
                        cap CHAR(5) NOT NULL
                    );

                    CREATE TABLE IF NOT EXISTS gest_sede (
                        email VARCHAR(255),
                        id_sede INT,
                        PRIMARY KEY (email, id_sede),
                        FOREIGN KEY (email) REFERENCES utente(email) 
                            ON DELETE CASCADE 
                            ON UPDATE CASCADE,
                        FOREIGN KEY (id_sede) REFERENCES sede(id) 
                            ON DELETE CASCADE 
                            ON UPDATE CASCADE
                    );

                    CREATE TABLE IF NOT EXISTS sala (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        id_sede INT,
                        numero INT NOT NULL,
                        capienza INT NOT NULL,
                        FOREIGN KEY (id_sede) REFERENCES sede(id) 
                            ON DELETE CASCADE 
                            ON UPDATE CASCADE
                    );

                    CREATE TABLE IF NOT EXISTS posto (
                        id_sala INT,
                        fila CHAR(1),
                        numero INT,
                        PRIMARY KEY (id_sala, fila, numero),
                        FOREIGN KEY (id_sala) REFERENCES sala(id) 
                            ON DELETE CASCADE 
                            ON UPDATE CASCADE
                    );

                     CREATE TABLE IF NOT EXISTS film (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            titolo VARCHAR(255) NOT NULL,
                            genere VARCHAR(100) NOT NULL,
                            classificazione VARCHAR(50) NOT NULL,
                            durata INT NOT NULL,
                            locandina MEDIUMBLOB,
                            descrizione TEXT NOT NULL,
                            is_proiettato BOOLEAN DEFAULT FALSE
                        );

                    CREATE TABLE IF NOT EXISTS slot (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        ora_inizio TIME NOT NULL
                    );

                    CREATE TABLE IF NOT EXISTS proiezione (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        data DATE NOT NULL,
                        id_film INT,
                        id_sala INT,
                        id_orario INT,
                        FOREIGN KEY (id_film) REFERENCES film(id) 
                            ON DELETE CASCADE 
                            ON UPDATE CASCADE,
                        FOREIGN KEY (id_sala) REFERENCES sala(id) 
                            ON DELETE CASCADE 
                            ON UPDATE CASCADE,
                        FOREIGN KEY (id_orario) REFERENCES slot(id) 
                            ON DELETE CASCADE 
                            ON UPDATE CASCADE
                    );

                    CREATE TABLE IF NOT EXISTS posto_proiezione (
                        id_sala INT,
                        fila CHAR(1),
                        numero INT,
                        id_proiezione INT,
                        stato BOOLEAN DEFAULT TRUE,
                        PRIMARY KEY (id_sala, fila, numero, id_proiezione),
                        FOREIGN KEY (id_sala, fila, numero) REFERENCES posto(id_sala, fila, numero) 
                            ON DELETE CASCADE 
                            ON UPDATE CASCADE,
                        FOREIGN KEY (id_proiezione) REFERENCES proiezione(id) 
                            ON DELETE CASCADE 
                            ON UPDATE CASCADE
                    );

                    CREATE TABLE IF NOT EXISTS prenotazione (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        email_cliente VARCHAR(255),
                        id_proiezione INT,
                        FOREIGN KEY (email_cliente) REFERENCES cliente(email) 
                            ON DELETE CASCADE 
                            ON UPDATE CASCADE,
                        FOREIGN KEY (id_proiezione) REFERENCES proiezione(id) 
                            ON DELETE CASCADE 
                            ON UPDATE CASCADE
                    );

                    CREATE TABLE IF NOT EXISTS occupa (
                        id_sala INT,
                        fila CHAR(1),
                        numero INT,
                        id_proiezione INT,
                        id_prenotazione INT,
                        PRIMARY KEY (id_sala, fila, numero, id_proiezione, id_prenotazione),
                        FOREIGN KEY (id_sala, fila, numero) REFERENCES posto(id_sala, fila, numero) 
                            ON DELETE CASCADE 
                            ON UPDATE CASCADE,
                        FOREIGN KEY (id_proiezione) REFERENCES proiezione(id) 
                            ON DELETE CASCADE 
                            ON UPDATE CASCADE,
                        FOREIGN KEY (id_prenotazione) REFERENCES prenotazione(id) 
                            ON DELETE CASCADE 
                            ON UPDATE CASCADE
                    );
                """;
                conn.createStatement().execute(schemaCreationScript);
            }
        } catch (Exception e) {
            throw new RuntimeException("Errore nella configurazione del DataSource di test", e);
        }
    }
}
