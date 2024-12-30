package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.Film;
import it.unisa.application.model.entity.Sede;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


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
}
