package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.*;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProiezioneDAO {
    private final DataSource ds;

    public ProiezioneDAO() {
        this.ds = DataSourceSingleton.getInstance();
    }

    public boolean create(Proiezione proiezione){
        String INSERT_PROIEZIONE = "INSERT INTO proiezione(id, data, id_film, id_sala, id_orario) VALUES(?, ?, ?, ?, ?)";
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_PROIEZIONE, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, proiezione.getId());
            ps.setDate(2, Date.valueOf(proiezione.getDataProiezione()));
            ps.setInt(3, proiezione.getFilmProiezione().getId());
            ps.setInt(4, proiezione.getSalaProiezione().getId());
            ps.setInt(5, proiezione.getOrarioProiezione().getId());
            return ps.executeUpdate() > 0;

        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Proiezione retirveById(int id){
        String sql = "SELECT * FROM proiezione WHERE id = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();


            if(rs.next()){
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
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Proiezione> retriveByFilm(Film film, Sede sede){
        String sql = "SELECT * FROM proiezione WHERE id_film = ? AND id_sede = ?";
        List<Proiezione> proiezioni = new ArrayList<>();

        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, film.getId());
            ps.setInt(2, sede.getId());
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Proiezione proiezione = new Proiezione();
                proiezione.setId(rs.getInt("id"));

                proiezione.setFilmProiezione(film);
                proiezione.setDataProiezione(rs.getDate("data").toLocalDate());

                Sala sala = new Sala();
                sala.setId(rs.getInt("id_sala"));
                proiezione.setSalaProiezione(sala);

                Slot slotOrario = new Slot();
                slotOrario.setId(rs.getInt("id_orario"));
                proiezione.setOrarioProiezione(slotOrario);

                proiezioni.add(proiezione);

            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return proiezioni;

    }
}
