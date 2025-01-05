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
        String sql = "INSERT INTO proiezione (data, id_film, id_sala, id_orario) VALUES (?, ?, ?, ?)";
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(proiezione.getDataProiezione()));
            ps.setInt(2, proiezione.getFilmProiezione().getId());
            ps.setInt(3, proiezione.getSalaProiezione().getId());
            ps.setInt(4, proiezione.getOrarioProiezione().getId());
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
                Slot slotOrario = new Slot();
                slotOrario.setId(rs.getInt("id_orario"));
                proiezione.setFilmProiezione(film);
                proiezione.setDataProiezione(rs.getDate("data").toLocalDate());
                proiezione.setSalaProiezione(sala);
                proiezione.setOrarioProiezione(slotOrario);
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
        FilmDAO filmDao = new FilmDAO(); // Assumendo un'istanza o accesso al DAO
        SalaDAO salaDAO = new SalaDAO(); // DAO per recuperare i dati della sala
        PostoProiezioneDAO postiProiezioneDAO = new PostoProiezioneDAO(); // DAO per i posti proiezione
        SlotDAO slotDAO = new SlotDAO(); // DAO per gli slot

        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, film.getId());
            ps.setInt(2, sede.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Proiezione p = new Proiezione();
                p.setId(rs.getInt("id"));

                // Recupera i dettagli completi del film
                Film filmDetails = filmDao.retrieveById(rs.getInt("id_film"));
                p.setFilmProiezione(filmDetails);

                p.setDataProiezione(rs.getDate("data").toLocalDate());

                // Recupera i dettagli completi della sala
                Sala salaDetails = salaDAO.retrieveById(rs.getInt("id_sala"));
                p.setSalaProiezione(salaDetails);

                // Recupera i posti della proiezione
                List<PostoProiezione> postiProiezione = postiProiezioneDAO.retrieveAllByProiezione(p);
                p.setPostiProiezione(postiProiezione);

                // Recupera i dettagli dello slot orario
                Slot slotDetails = slotDAO.retrieveById(rs.getInt("id_orario"));
                p.setOrarioProiezione(slotDetails);

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

                SELECT p.*, s.numero AS numero_sala, f.titolo AS titolo_film, sl.ora_inizio AS orario
                FROM proiezione p
                JOIN sala s ON p.id_sala = s.id
                JOIN film f ON p.id_film = f.id
                JOIN slot sl ON p.id_orario = sl.id
                WHERE s.id_sede = ?
                ORDER BY p.data ASC, sl.ora_inizio ASC
                """;


        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, sedeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Proiezione proiezione = new Proiezione();
                proiezione.setId(rs.getInt("id"));
                Sala sala = new Sala();
                sala.setId(rs.getInt("id_sala"));
                sala.setNumeroSala(rs.getInt("numero_sala"));
                proiezione.setSalaProiezione(sala);
                Film film = new Film();
                film.setId(rs.getInt("id_film"));
                film.setTitolo(rs.getString("titolo_film"));
                proiezione.setFilmProiezione(film);
                Slot slot = new Slot();
                slot.setId(rs.getInt("id_orario"));
                slot.setOraInizio(rs.getTime("orario"));
                proiezione.setOrarioProiezione(slot);
                proiezione.setDataProiezione(rs.getDate("data").toLocalDate());
                proiezioni.add(proiezione);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return proiezioni;
    }

    public Proiezione retrieveProiezioneBySalaSlotAndData(int salaId, int slotId, LocalDate data) {
        String sql = "SELECT * FROM proiezione WHERE id_sala = ? AND id_orario = ? AND data = ?";
        try (Connection connection = ds.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, salaId);
            ps.setInt(2, slotId);
            ps.setDate(3, Date.valueOf(data));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Proiezione p = new Proiezione();
                p.setId(rs.getInt("id"));
                Film f = new Film();
                f.setId(rs.getInt("id_film"));
                p.setFilmProiezione(f);
                Sala s = new Sala();
                s.setId(rs.getInt("id_sala"));
                Slot sl = new Slot();
                sl.setId(rs.getInt("id_orario"));
                p.setSalaProiezione(s);
                p.setOrarioProiezione(sl);
                p.setDataProiezione(rs.getDate("data").toLocalDate());
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
