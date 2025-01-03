package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.Film;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilmDAO {
    private final DataSource ds;

    public FilmDAO() {
        this.ds = DataSourceSingleton.getInstance();
    }

    public boolean create(Film film) {
        String sql = "INSERT INTO film (titolo, genere, classificazione, durata, locandina, descrizione, is_proiettato) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, film.getTitolo());
            ps.setString(2, film.getGenere());
            ps.setString(3, film.getClassificazione());
            ps.setInt(4, film.getDurata());
            ps.setBytes(5, film.getLocandina());
            ps.setString(6, film.getDescrizione());
            ps.setBoolean(7, film.isProiettato());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    film.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Film retrieveById(int id) {
        String sql = "SELECT * FROM film WHERE id = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Film(
                        rs.getInt("id"),
                        rs.getString("titolo"),
                        rs.getString("genere"),
                        rs.getString("classificazione"),
                        rs.getInt("durata"),
                        rs.getBytes("locandina"),
                        rs.getString("descrizione"),
                        rs.getBoolean("is_proiettato")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Film> retrieveAll() {
        List<Film> films = new ArrayList<>();
        String sql = "SELECT * FROM film";
        try (Connection connection = ds.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                films.add(new Film(
                        rs.getInt("id"),
                        rs.getString("titolo"),
                        rs.getString("genere"),
                        rs.getString("classificazione"),
                        rs.getInt("durata"),
                        rs.getBytes("locandina"), // Cambiato da getString a getBytes
                        rs.getString("descrizione"),
                        rs.getBoolean("is_proiettato")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return films;
    }

}
