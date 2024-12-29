package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.Film;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilmDAO {

    private static final String INSERT_FILM = "INSERT INTO film (titolo, genere, classificazione, durata, locandina, descrizione, is_proiettato) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_FILMS = "SELECT * FROM film";

    // Aggiunge un film al catalogo
    public boolean addFilm(Film film) {
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_FILM, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, film.getTitolo());
            ps.setString(2, film.getGenere());
            ps.setString(3, film.getClassificazione());
            ps.setInt(4, film.getDurata());
            ps.setString(5, film.getLocandina());
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

    // Ottiene tutti i film dal catalogo
    public List<Film> getAllFilms() {
        List<Film> films = new ArrayList<>();
        String query = "SELECT * FROM film";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                films.add(new Film(
                        resultSet.getInt("id"),
                        resultSet.getString("titolo"),
                        resultSet.getString("genere"),
                        resultSet.getString("classificazione"),
                        resultSet.getInt("durata"),
                        resultSet.getString("locandina"),
                        resultSet.getString("descrizione"),
                        resultSet.getBoolean("is_proiettato")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return films;
    }

}

