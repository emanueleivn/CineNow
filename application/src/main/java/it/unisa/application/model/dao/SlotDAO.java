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

    public Slot retrieveById(int id) {
        String sql = "SELECT * FROM slot WHERE id = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
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

    public Slot retrieveByProiezione(Proiezione proiezione) {
        if (proiezione == null || proiezione.getId() == 0) {
            throw new IllegalArgumentException("La proiezione non può essere null o avere ID 0.");
        }

        String sql = """
            SELECT s.*
            FROM slot s
            INNER JOIN proiezione_slot ps ON s.id = ps.id_slot
            WHERE ps.id_proiezione = ?
        """;
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, proiezione.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
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
            WHERE NOT EXISTS (
                SELECT 1
                FROM proiezione p
                INNER JOIN proiezione_slot ps ON p.id = ps.id_proiezione
                WHERE ps.id_slot = s.id
                  AND p.id_sala = ?
                  AND p.data = ?
            )
            ORDER BY s.ora_inizio
        """;
        try (Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, salaId);
            ps.setDate(2, Date.valueOf(giorno));
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
    public boolean associaSlot(int idProiezione, int idSlot) {
        String sql = "INSERT INTO proiezione_slot (id_proiezione, id_slot) VALUES (?, ?)";
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idProiezione);
            ps.setInt(2, idSlot);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
