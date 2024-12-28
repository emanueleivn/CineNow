package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.Utente;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtenteDAO {
    private DataSource ds;

    public UtenteDAO() {
        this.ds = DataSourceSingleton.getInstance();
    }

    public boolean create(Utente utente) {
        String sql = "INSERT INTO utente (email, password, ruolo) VALUES (?, ?, ?)";
        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, utente.getEmail());
            stmt.setString(2, utente.getPassword());
            stmt.setString(3, utente.getRuolo());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Utente retrieveByEmail(String email) {
        String sql = "SELECT email, password, ruolo " +
                     "FROM utente " +
                     "WHERE email = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Utente utente = new Utente();
                    utente.setEmail(rs.getString("email"));
                    utente.setPassword(rs.getString("password"));
                    utente.setRuolo(rs.getString("ruolo"));
                    return utente;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
