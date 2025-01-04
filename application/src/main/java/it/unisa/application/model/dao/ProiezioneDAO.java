package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.*;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProiezioneDAO {
    private final DataSource ds;

    public ProiezioneDAO() {
        this.ds = DataSourceSingleton.getInstance();
    }

    // Metodo per creare una nuova proiezione
    public boolean create(Proiezione proiezione) {
        String sql = "INSERT INTO proiezione (data, id_film, id_sala) VALUES (?, ?, ?)";
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate(1, Date.valueOf(proiezione.getDataProiezione()));
            ps.setInt(2, proiezione.getFilmProiezione().getId());
            ps.setInt(3, proiezione.getSalaProiezione().getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        proiezione.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Metodo per recuperare una proiezione per ID
    public Proiezione retrieveById(int id) {
        String sql = """
                    SELECT p.id, p.data, p.id_film, p.id_sala, ps.id_slot
                    FROM proiezione p
                    JOIN proiezione_slot ps ON p.id = ps.id_proiezione
                    WHERE p.id = ?
                """;

        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapProiezione(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Metodo per recuperare le proiezioni per un film e una sede specifici
    public List<Proiezione> retrieveByFilm(Film film, Sede sede) {
        String sql = """
                    SELECT p.id, p.data, p.id_film, p.id_sala, ps.id_slot
                    FROM proiezione p
                    JOIN proiezione_slot ps ON p.id = ps.id_proiezione
                    JOIN sala s ON p.id_sala = s.id
                    WHERE p.id_film = ? AND s.id_sede = ?
                """;

        List<Proiezione> proiezioni = new ArrayList<>();
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, film.getId());
            ps.setInt(2, sede.getId());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    proiezioni.add(mapProiezione(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return proiezioni;
    }

    // Metodo per recuperare una proiezione per sala, slot e data
    public Proiezione retrieveProiezioneBySalaSlotAndData(int salaId, int slotId, LocalDate data) {
        String sql = """
                    SELECT p.id, p.data, p.id_film, p.id_sala, ps.id_slot
                    FROM proiezione p
                    JOIN proiezione_slot ps ON p.id = ps.id_proiezione
                    WHERE p.id_sala = ? AND ps.id_slot = ? AND p.data = ?
                """;

        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, salaId);
            ps.setInt(2, slotId);
            ps.setDate(3, Date.valueOf(data));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapProiezione(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Metodo per recuperare tutte le proiezioni in una sede
    public List<Proiezione> retrieveAllBySede(int sedeId) {
        String sql = """
                    SELECT p.id, p.data, p.id_film, p.id_sala, ps.id_slot, 
                           s.numero AS numero_sala, f.titolo AS titolo_film, sl.ora_inizio AS orario
                    FROM proiezione p
                    JOIN proiezione_slot ps ON p.id = ps.id_proiezione
                    JOIN sala s ON p.id_sala = s.id
                    JOIN film f ON p.id_film = f.id
                    JOIN slot sl ON ps.id_slot = sl.id
                    WHERE s.id_sede = ?
                    ORDER BY p.data ASC, sl.ora_inizio ASC
                """;

        List<Proiezione> proiezioni = new ArrayList<>();
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, sedeId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Proiezione proiezione = mapProiezione(rs);
                    proiezioni.add(proiezione);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return proiezioni;
    }

    // Metodo helper per mappare una proiezione dal ResultSet
    private Proiezione mapProiezione(ResultSet rs) throws SQLException {
        Proiezione proiezione = new Proiezione();
        proiezione.setId(rs.getInt("id"));
        proiezione.setDataProiezione(rs.getDate("data").toLocalDate());

        // Recupera le entità correlate
        FilmDAO filmDAO = new FilmDAO();
        SalaDAO salaDAO = new SalaDAO();
        SlotDAO slotDAO = new SlotDAO();

        Film film = filmDAO.retrieveById(rs.getInt("id_film"));
        proiezione.setFilmProiezione(film);

        Sala sala = salaDAO.retrieveById(rs.getInt("id_sala"));
        proiezione.setSalaProiezione(sala);

        Slot slot = slotDAO.retrieveById(rs.getInt("id_slot"));
        proiezione.setOrarioProiezione(slot);

        return proiezione;
    }
}
