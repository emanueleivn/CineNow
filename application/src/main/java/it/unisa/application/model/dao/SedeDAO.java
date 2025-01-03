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
    public Sede retriveById(int id) {
        String sql = "SELECT s.id, s.nome, s.via, s.città, s.cap FROM sede s WHERE s.id = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String indirizzo = rs.getString("via") + ", " + rs.getString("città") + ", " + rs.getString("cap");
                return new Sede(rs.getInt("id"), rs.getString("nome"), indirizzo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Sede> retirveAll(){
        String sql = "SELECT * FROM sede";
        List<Sede> sedi = new ArrayList<>();
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String indirizzo = rs.getString("via") + ", " + rs.getString("città") + ", " + rs.getString("cap");
                sedi.add(new Sede(rs.getInt("id"), rs.getString("nome"), indirizzo));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return sedi;
    }
    public List<Sala> retrieveSaleBySede(int sedeId) {
        List<Sala> sale = new ArrayList<>();
        String sql = "SELECT * FROM sala WHERE id_sede = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, sedeId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Sala s = new Sala();
                s.setId(rs.getInt("id"));
                s.setNumeroSala(rs.getInt("numero"));
                s.setCapienza(rs.getInt("capienza"));
                sale.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sale;
    }
    public Sala retrieveSalaById(int salaId) {
        String sql = "SELECT * FROM sala WHERE id = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, salaId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Sala s = new Sala();
                s.setId(rs.getInt("id"));
                s.setNumeroSala(rs.getInt("numero"));
                s.setCapienza(rs.getInt("capienza"));
                return s;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Sede retrieveByGestoreEmail(String email) {
        String sql = "SELECT s.id, s.nome, s.via, s.città, s.cap FROM sede s JOIN gest_sede gs ON s.id = gs.id_sede WHERE gs.email = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String indirizzo = rs.getString("via") + ", " + rs.getString("città") + ", " + rs.getString("cap");
                return new Sede(rs.getInt("id"), rs.getString("nome"), indirizzo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
