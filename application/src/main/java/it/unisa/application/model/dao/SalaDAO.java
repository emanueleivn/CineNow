package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.Film;
import it.unisa.application.model.entity.Sala;
import it.unisa.application.model.entity.Sede;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalaDAO {
    private DataSource ds;

    public SalaDAO() {
        this.ds = DataSourceSingleton.getInstance();
    }
    public Sala retrieveById(int id) {
        String sql = "SELECT * FROM sala WHERE id = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Sala sala = new Sala();
                sala.setId(rs.getInt("id"));
                sala.setNumeroSala(rs.getInt("numero"));
                sala.setCapienza(rs.getInt("capienza"));
                Sede sede = new Sede();
                sede.setId(rs.getInt("id_sede"));
                sala.setSede(sede);
                return sala;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Sala> retrieveAll() throws SQLException {
        List<Sala> sale = new ArrayList<>();
        String query = "SELECT * FROM sala";

        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Sala sala = new Sala();
                sala.setId(rs.getInt("id"));
                sala.setId(rs.getInt("id_sede"));
                sala.setNumeroSala(rs.getInt("numero"));
                sala.setCapienza(rs.getInt("capienza"));
                sale.add(sala);
            }
        }
        return sale;
    }
}
