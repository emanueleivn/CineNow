package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.Sala;
import it.unisa.application.model.entity.Sede;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalaDAO {
    private final DataSource ds;

    public SalaDAO() {
        this.ds = DataSourceSingleton.getInstance();
    }

    // Metodo per recuperare una sala per ID
    public Sala retrieveById(int id) {
        String sql = """
            SELECT sala.id, sala.numero, sala.capienza, sede.id AS sede_id, sede.nome, sede.via, sede.città, sede.cap
            FROM sala
            JOIN sede ON sala.id_sede = sede.id
            WHERE sala.id = ?
        """;

        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapSala(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero della sala con ID: " + id, e);
        }
        return null;
    }

    // Metodo per recuperare tutte le sale
    public List<Sala> retrieveAll() {
        List<Sala> sale = new ArrayList<>();
        String sql = """
            SELECT sala.id, sala.numero, sala.capienza, sede.id AS sede_id, sede.nome, sede.via, sede.città, sede.cap
            FROM sala
            JOIN sede ON sala.id_sede = sede.id
        """;

        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                sale.add(mapSala(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero di tutte le sale.", e);
        }
        return sale;
    }

    // Metodo helper per mappare una sala dal ResultSet
    private Sala mapSala(ResultSet rs) throws SQLException {
        Sala sala = new Sala();
        sala.setId(rs.getInt("id"));
        sala.setNumeroSala(rs.getInt("numero"));
        sala.setCapienza(rs.getInt("capienza"));

        Sede sede = new Sede();
        sede.setId(rs.getInt("sede_id"));
        sede.setNome(rs.getString("nome"));
        sede.setIndirizzo(rs.getString("via") + ", " + rs.getString("città") + ", " + rs.getString("cap"));
        sala.setSede(sede);

        return sala;
    }
}
