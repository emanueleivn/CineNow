package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.Film;
import it.unisa.application.model.entity.Sala;
import it.unisa.application.model.entity.Sede;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SedeDAO {
    private final DataSource ds;

    public SedeDAO() {
        this.ds = DataSourceSingleton.getInstance();
    }

    // Recupera una sede per ID
    public Sede retrieveById(int id) {
        String sql = """
            SELECT s.id, s.nome, s.via, s.città, s.cap
            FROM sede s
            WHERE s.id = ?
        """;

        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapSede(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero della sede con ID: " + id, e);
        }
        return null;
    }

    // Recupera tutte le sedi
    public List<Sede> retrieveAll() {
        String sql = "SELECT * FROM sede";
        List<Sede> sedi = new ArrayList<>();

        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                sedi.add(mapSede(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero di tutte le sedi.", e);
        }
        return sedi;
    }

    // Recupera tutte le sale di una sede specifica
    public List<Sala> retrieveSaleBySede(int sedeId) {
        String sql = """
            SELECT sala.id, sala.numero, sala.capienza, sede.id AS sede_id, sede.nome, sede.via, sede.città, sede.cap
            FROM sala
            JOIN sede ON sala.id_sede = sede.id
            WHERE sala.id_sede = ?
        """;

        List<Sala> sale = new ArrayList<>();
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, sedeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Sala sala = mapSala(rs);
                    sale.add(sala);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero delle sale per la sede con ID: " + sedeId, e);
        }
        return sale;
    }

    // Recupera una sede tramite email del gestore
    public Sede retrieveByGestoreEmail(String email) {
        String sql = """
            SELECT s.id, s.nome, s.via, s.città, s.cap
            FROM sede s
            JOIN gest_sede gs ON s.id = gs.id_sede
            WHERE gs.email = ?
        """;

        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapSede(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero della sede per l'email del gestore: " + email, e);
        }
        return null;
    }

    // Recupera i film proiettati in una sede specifica
    public List<Film> retrieveFilm(int sedeId) {
        String sql = """
            SELECT DISTINCT f.id, f.titolo, f.genere, f.classificazione, f.durata, f.locandina, f.descrizione, f.is_proiettato
            FROM film f
            JOIN proiezione p ON f.id = p.id_film
            JOIN sala s ON p.id_sala = s.id
            WHERE s.id_sede = ?
        """;

        List<Film> filmList = new ArrayList<>();
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, sedeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filmList.add(mapFilm(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero dei film per la sede con ID: " + sedeId, e);
        }
        return filmList;
    }

    // Recupera una sala per ID
    public Sala retrieveSalaById(int salaId) {
        String sql = "SELECT * FROM sala WHERE id = ?";

        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, salaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapSala(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero della sala con ID: " + salaId, e);
        }
        return null;
    }

    // Mappa una sede dal ResultSet
    private Sede mapSede(ResultSet rs) throws SQLException {
        String indirizzo = rs.getString("via") + ", " + rs.getString("città") + ", " + rs.getString("cap");
        return new Sede(rs.getInt("id"), rs.getString("nome"), indirizzo);
    }

    // Mappa una sala dal ResultSet
    private Sala mapSala(ResultSet rs) throws SQLException {
        Sala sala = new Sala();
        sala.setId(rs.getInt("id"));
        sala.setNumeroSala(rs.getInt("numero"));
        sala.setCapienza(rs.getInt("capienza"));

        Sede sede = new Sede();
        sede.setId(rs.getInt("sede_id"));
        sede.setNome(rs.getString("nome"));
        sede.setIndirizzo(rs.getString("via") + ", " + rs.getString("città") + ", " + rs.getString("cap"));
        sala.setSede(sede);

        return sala;
    }

    // Mappa un film dal ResultSet
    private Film mapFilm(ResultSet rs) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("id"));
        film.setTitolo(rs.getString("titolo"));
        film.setGenere(rs.getString("genere"));
        film.setClassificazione(rs.getString("classificazione"));
        film.setDurata(rs.getInt("durata"));
        film.setLocandina(rs.getBytes("locandina"));
        film.setDescrizione(rs.getString("descrizione"));
        film.setProiettato(rs.getBoolean("is_proiettato"));
        return film;
    }
}
