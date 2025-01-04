package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.*;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrenotazioneDAO {
    private final DataSource ds;

    public PrenotazioneDAO() {
        this.ds = DataSourceSingleton.getInstance();
    }

    // Creazione di una nuova prenotazione
    public boolean create(Prenotazione prenotazione) {
        String sql = "INSERT INTO prenotazione (email_cliente, id_proiezione) VALUES (?, ?)";
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Preparazione dei parametri
            ps.setString(1, prenotazione.getCliente().getEmail());
            ps.setInt(2, prenotazione.getProiezione().getId());

            // Esecuzione e recupero della chiave generata
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        prenotazione.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Recupera una prenotazione tramite ID
    public Prenotazione retrieveById(int id) {
        String sql = "SELECT * FROM prenotazione WHERE id = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Prenotazione prenotazione = new Prenotazione();
                    prenotazione.setId(rs.getInt("id"));

                    Cliente cliente = new Cliente();
                    cliente.setEmail(rs.getString("email_cliente"));
                    prenotazione.setCliente(cliente);

                    Proiezione proiezione = new Proiezione();
                    proiezione.setId(rs.getInt("id_proiezione"));
                    prenotazione.setProiezione(proiezione);

                    return prenotazione;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Prenotazione> retrieveAllByCliente(Cliente cliente) {
        if (cliente == null || cliente.getEmail() == null) {
            throw new IllegalArgumentException("Cliente o email non può essere null.");
        }

        List<Prenotazione> prenotazioni = new ArrayList<>();
        String sql = """
        SELECT 
            p.id AS prenotazione_id,
            pr.id AS proiezione_id,
            pr.data AS data_proiezione,
            sl.ora_inizio,
            f.id AS film_id,
            f.titolo AS film_titolo,
            f.durata,
            s.id AS sala_id,
            s.numero AS numero_sala,
            pp.fila AS fila_posto,
            pp.numero AS numero_posto
        FROM prenotazione p
        JOIN proiezione pr ON p.id_proiezione = pr.id
        JOIN film f ON pr.id_film = f.id
        JOIN sala s ON pr.id_sala = s.id
        JOIN slot sl ON EXISTS (
            SELECT 1 
            FROM proiezione_slot ps
            WHERE ps.id_proiezione = pr.id AND ps.id_slot = sl.id
        )
        LEFT JOIN occupa o ON o.id_prenotazione = p.id
        LEFT JOIN posto_proiezione pp ON pp.id_sala = o.id_sala 
                                      AND pp.fila = o.fila 
                                      AND pp.numero = o.numero 
                                      AND pp.id_proiezione = pr.id
        WHERE p.email_cliente = ?
        ORDER BY p.id, pp.fila, pp.numero
        """;

        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cliente.getEmail());
            try (ResultSet rs = ps.executeQuery()) {
                Map<Integer, Prenotazione> prenotazioneMap = new HashMap<>();

                while (rs.next()) {
                    int prenotazioneId = rs.getInt("prenotazione_id");
                    Prenotazione prenotazione = prenotazioneMap.computeIfAbsent(prenotazioneId, id -> {
                        try {
                            // Crea la nuova prenotazione
                            Film film = new Film(
                                    rs.getInt("film_id"),
                                    rs.getString("film_titolo"),
                                    null, null,
                                    rs.getInt("durata"),
                                    null, null,
                                    false
                            );

                            Sala sala = new Sala();
                            sala.setId(rs.getInt("sala_id"));
                            sala.setNumeroSala(rs.getInt("numero_sala"));

                            Slot slot = new Slot();
                            slot.setOraInizio(rs.getTime("ora_inizio"));

                            Proiezione proiezione = new Proiezione();
                            proiezione.setId(rs.getInt("proiezione_id"));
                            proiezione.setDataProiezione(rs.getDate("data_proiezione").toLocalDate());
                            proiezione.setFilmProiezione(film);
                            proiezione.setSalaProiezione(sala);
                            proiezione.setOrarioProiezione(slot);

                            Prenotazione newPrenotazione = new Prenotazione();
                            newPrenotazione.setId(prenotazioneId);
                            newPrenotazione.setCliente(cliente);
                            newPrenotazione.setProiezione(proiezione);
                            newPrenotazione.setPostiPrenotazione(new ArrayList<>());
                            return newPrenotazione;
                        } catch (SQLException e) {
                            throw new RuntimeException("Errore durante il mapping della prenotazione", e);
                        }
                    });

                    // Aggiungi i posti se presenti
                    if (rs.getString("fila_posto") != null && rs.getInt("numero_posto") != 0) {
                        Posto posto = new Posto();
                        posto.setFila(rs.getString("fila_posto").charAt(0));
                        posto.setNumero(rs.getInt("numero_posto"));

                        PostoProiezione postoProiezione = new PostoProiezione();
                        postoProiezione.setPosto(posto);
                        postoProiezione.setProiezione(prenotazione.getProiezione());

                        // Evita duplicati nei posti
                        if (!prenotazione.getPostiPrenotazione().contains(postoProiezione)) {
                            prenotazione.getPostiPrenotazione().add(postoProiezione);
                        }
                    }
                }

                prenotazioni.addAll(prenotazioneMap.values());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prenotazioni;
    }

}
