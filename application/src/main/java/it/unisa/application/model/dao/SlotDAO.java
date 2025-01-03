package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.Proiezione;
import it.unisa.application.model.entity.Slot;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SlotDAO {
    private final DataSource ds;
    public SlotDAO() {
        this.ds = DataSourceSingleton.getInstance();
    }
    public Slot retriveById(int id) {
        String sql = "SELECT * FROM slot WHERE id = ?";
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Slot retiveByProiezione(Proiezione proiezione){
        String sql = "SELECT * FROM slot WHERE id = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, proiezione.getOrarioProiezione().getId());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Slot slot = new Slot();
                slot.setId(rs.getInt("id"));
                slot.setOraInizio(rs.getTime("ora_inizio"));
                return slot;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Slot> retriveFreeSlotPerGiorno(int salaId, LocalDate giorno) {
        List<Slot> freeSlots = new ArrayList<>();
        String sql = """
            SELECT s.*
            FROM slot s
            WHERE s.data = ?
              AND NOT EXISTS (
                SELECT 1
                FROM proiezione p
                WHERE p.id_orario = s.id
                  AND p.id_sala = ?
                  AND p.data = s.data
              )
            ORDER BY s.ora_inizio
        """;
        try (Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(giorno));
            ps.setInt(2, salaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Slot slot = new Slot();
                    slot.setId(rs.getInt("id"));
                    slot.setOraInizio(rs.getTime("ora_inizio"));
                    freeSlots.add(slot);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return freeSlots;
    }
    public List<Slot> retrieveAllSlots() {
        List<Slot> list = new ArrayList<>();
        String sql = "SELECT * FROM slot ORDER BY ora_inizio";
        try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Slot s = new Slot();
                s.setId(rs.getInt("id"));
                s.setOraInizio(rs.getTime("ora_inizio"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
