package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.Film;
import it.unisa.application.model.entity.Sala;
import it.unisa.application.model.entity.Sede;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class SedeDAO {
    private final DataSource ds;

    public SedeDAO() {
        this.ds = DataSourceSingleton.getInstance();
    }


    public Sede retriveById(int id){
        String sql = "SELECT * FROM sede WHERE id = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                String indirizzo = rs.getString("via") + ", " + rs.getString("città") + ", " + rs.getString("cap");
                return new Sede(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        indirizzo
                );
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Sede> retirveAll(){
        String sql = "SELECT * FROM sede";
        List<Sede> sedi = new ArrayList<Sede>();
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {


            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                String indirizzo = rs.getString("via") + ", " + rs.getString("città") + ", " + rs.getString("cap");
                sedi.add(new Sede(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        indirizzo
                ));
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return sedi;
    }

    public List<Sala> retrieveSaleBySede(int sedeId) {
        String sql = "SELECT * FROM sala WHERE id_sede = ?";
        List<Sala> sale = new ArrayList<>();
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, sedeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Sala sala = new Sala();
                sala.setId(rs.getInt("id"));
                sala.setNumeroSala(rs.getInt("numero"));
                sale.add(sala);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sale;
    }

    public Sede retrieveByGestoreEmail(String email) {
        String sql = "SELECT s.id, s.nome, s.via, s.città, s.cap " +
                "FROM sede s JOIN gest_sede gs ON s.id = gs.id_sede " +
                "WHERE gs.email = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String indirizzo = rs.getString("via") + ", " + rs.getString("città") + ", " + rs.getString("cap");
                return new Sede(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        indirizzo
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public List<Film> retrieveFilm(int sedeId) throws SQLException {
        List<Film> filmList = new ArrayList<>();
        String query = """
                SELECT DISTINCT f.id, f.titolo, f.genere, f.classificazione, f.durata, f.locandina, f.descrizione, f.is_proiettato
                FROM film f
                JOIN proiezione p ON f.id = p.id_film
                JOIN sala s ON p.id_sala = s.id
                WHERE s.id_sede = ?
                """;

        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, sedeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Film film = new Film();
                    film.setId(rs.getInt("id"));
                    film.setTitolo(rs.getString("titolo"));
                    film.setGenere(rs.getString("genere"));
                    film.setClassificazione(rs.getString("classificazione"));
                    film.setDurata(rs.getInt("durata"));
                    film.setLocandina(rs.getString("locandina"));
                    film.setDescrizione(rs.getString("descrizione"));
                    film.setProiettato(rs.getBoolean("is_proiettato"));
                    filmList.add(film);
                }
            }
        }
        return filmList;
    }
}

