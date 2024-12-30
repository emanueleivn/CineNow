package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.Slot;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SlotDAO {
    private final DataSource ds;

    public SlotDAO() {
        this.ds = DataSourceSingleton.getInstance();
    }

    public Slot retriveById(int id){

        String sql = "select * from slot where id = ?";

        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                Slot slot = new Slot();
                slot.setId(rs.getInt("id"));
                slot.setOraInizio(rs.getTime("ora_inizio"));

                return slot;
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
