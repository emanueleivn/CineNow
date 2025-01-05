// File: ProiezioneDAO.java
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

    public boolean create(Proiezione proiezione) {
        String sqlInsertProiezione = "INSERT INTO proiezione (data, id_film, id_sala) VALUES (?, ?, ?)";
        String sqlInsertProiezioneSlot = "INSERT INTO proiezione_slot (id_proiezione, id_slot) VALUES (?, ?)";

        try (Connection connection = ds.getConnection();
             PreparedStatement psProiezione = connection.prepareStatement(sqlInsertProiezione, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement psProiezioneSlot = connection.prepareStatement(sqlInsertProiezioneSlot)) {

            connection.setAutoCommit(false);

            psProiezione.setDate(1, Date.valueOf(proiezione.getDataProiezione()));
            psProiezione.setInt(2, proiezione.getFilmProiezione().getId());
            psProiezione.setInt(3, proiezione.getSalaProiezione().getId());

            int affectedRows = psProiezione.executeUpdate();
            if (affectedRows == 0) {
                connection.rollback();
                throw new SQLException("Creazione della proiezione fallita.");
            }

            try (ResultSet generatedKeys = psProiezione.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    proiezione.setId(generatedKeys.getInt(1));
                } else {
                    connection.rollback();
                    throw new SQLException("Creazione della proiezione fallita.");
                }
            }

            for (Slot slot : proiezione.getSlotsProiezione()) {
                psProiezioneSlot.setInt(1, proiezione.getId());
                psProiezioneSlot.setInt(2, slot.getId());
                psProiezioneSlot.addBatch();
            }
            psProiezioneSlot.executeBatch();

            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public Proiezione retrieveById(int id) {
        String sql = "SELECT * FROM proiezione WHERE id = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Proiezione proiezione = new Proiezione();
                proiezione.setId(rs.getInt("id"));
                Film film = new Film();
                film.setId(rs.getInt("id_film"));
                Sala sala = new Sala();
                sala.setId(rs.getInt("id_sala"));
                proiezione.setFilmProiezione(film);
                proiezione.setDataProiezione(rs.getDate("data").toLocalDate());
                proiezione.setSalaProiezione(sala);
                return proiezione;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Proiezione> retrieveByFilm(Film film, Sede sede) {
        String sql = "SELECT p.*, s.id_sede FROM proiezione p " +
                "JOIN sala s ON p.id_sala = s.id " +
                "WHERE p.id_film = ? AND s.id_sede = ?";
        List<Proiezione> proiezioni = new ArrayList<>();
        FilmDAO filmDao = new FilmDAO();
        SalaDAO salaDAO = new SalaDAO();
        PostoProiezioneDAO postiProiezioneDAO = new PostoProiezioneDAO();
        SlotDAO slotDAO = new SlotDAO();

        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, film.getId());
            ps.setInt(2, sede.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Proiezione p = new Proiezione();
                p.setId(rs.getInt("id"));

                Film filmDetails = filmDao.retrieveById(rs.getInt("id_film"));
                p.setFilmProiezione(filmDetails);

                p.setDataProiezione(rs.getDate("data").toLocalDate());

                Sala salaDetails = salaDAO.retrieveById(rs.getInt("id_sala"));
                p.setSalaProiezione(salaDetails);

                List<PostoProiezione> postiProiezione = postiProiezioneDAO.retrieveAllByProiezione(p);
                p.setPostiProiezione(postiProiezione);

                List<Slot> slots = new ArrayList<>();
                String sqlSlots = "SELECT ps.id_slot, sl.ora_inizio FROM proiezione_slot ps " +
                        "JOIN slot sl ON ps.id_slot = sl.id WHERE ps.id_proiezione = ?";
                try (PreparedStatement psSlots = connection.prepareStatement(sqlSlots)) {
                    psSlots.setInt(1, p.getId());
                    ResultSet rsSlots = psSlots.executeQuery();
                    while (rsSlots.next()) {
                        Slot slot = new Slot();
                        slot.setId(rsSlots.getInt("id_slot"));
                        slot.setOraInizio(rsSlots.getTime("ora_inizio"));
                        slots.add(slot);
                    }
                }
                p.setSlotsProiezione(slots);

                proiezioni.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return proiezioni;
    }

    public List<Proiezione> retrieveAllBySede(int sedeId) {
        List<Proiezione> proiezioni = new ArrayList<>();
        String sql = """
                SELECT p.id, p.data, p.id_film, p.id_sala, f.titolo AS titolo_film, s.numero AS numero_sala
                FROM proiezione p
                JOIN sala s ON p.id_sala = s.id
                JOIN film f ON p.id_film = f.id
                WHERE s.id_sede = ?
                ORDER BY p.data ASC
                """;

        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, sedeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Proiezione proiezione = new Proiezione();
                proiezione.setId(rs.getInt("id"));
                proiezione.setDataProiezione(rs.getDate("data").toLocalDate());

                Film film = new Film();
                film.setId(rs.getInt("id_film"));
                film.setTitolo(rs.getString("titolo_film"));
                proiezione.setFilmProiezione(film);

                Sala sala = new Sala();
                sala.setId(rs.getInt("id_sala"));
                sala.setNumeroSala(rs.getInt("numero_sala"));
                proiezione.setSalaProiezione(sala);

                List<Slot> slots = new ArrayList<>();
                String sqlSlots = "SELECT ps.id_slot, sl.ora_inizio FROM proiezione_slot ps " +
                        "JOIN slot sl ON ps.id_slot = sl.id WHERE ps.id_proiezione = ?";
                try (PreparedStatement psSlots = connection.prepareStatement(sqlSlots)) {
                    psSlots.setInt(1, proiezione.getId());
                    ResultSet rsSlots = psSlots.executeQuery();
                    while (rsSlots.next()) {
                        Slot slot = new Slot();
                        slot.setId(rsSlots.getInt("id_slot"));
                        slot.setOraInizio(rsSlots.getTime("ora_inizio"));
                        slots.add(slot);
                    }
                }
                proiezione.setSlotsProiezione(slots);
                proiezioni.add(proiezione);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return proiezioni;
    }

    public Proiezione retrieveProiezioneBySalaSlotAndData(int salaId, int slotId, LocalDate data) {
        String sql = "SELECT * FROM proiezione WHERE id_sala = ? AND data = ? AND id IN " +
                "(SELECT id_proiezione FROM proiezione_slot WHERE id_slot = ?)";
        try (Connection connection = ds.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, salaId);
            ps.setDate(2, Date.valueOf(data));
            ps.setInt(3, slotId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Proiezione p = new Proiezione();
                p.setId(rs.getInt("id"));
                Film f = new Film();
                f.setId(rs.getInt("id_film"));
                p.setFilmProiezione(f);
                Sala s = new Sala();
                s.setId(rs.getInt("id_sala"));
                p.setSalaProiezione(s);
                p.setDataProiezione(rs.getDate("data").toLocalDate());

                // Recupera gli slot associati alla proiezione
                List<Slot> slots = new ArrayList<>();
                String sqlSlots = "SELECT ps.id_slot, sl.ora_inizio FROM proiezione_slot ps " +
                        "JOIN slot sl ON ps.id_slot = sl.id WHERE ps.id_proiezione = ?";
                try (PreparedStatement psSlots = connection.prepareStatement(sqlSlots)) {
                    psSlots.setInt(1, p.getId());
                    ResultSet rsSlots = psSlots.executeQuery();
                    while (rsSlots.next()) {
                        Slot slot = new Slot();
                        slot.setId(rsSlots.getInt("id_slot"));
                        slot.setOraInizio(rsSlots.getTime("ora_inizio"));
                        slots.add(slot);
                    }
                }
                p.setSlotsProiezione(slots);
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
