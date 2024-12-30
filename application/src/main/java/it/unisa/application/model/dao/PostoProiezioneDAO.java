package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.Posto;
import it.unisa.application.model.entity.PostoProiezione;
import it.unisa.application.model.entity.Proiezione;
import it.unisa.application.model.entity.Sala;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostoProiezioneDAO {
    private final DataSource ds;

    public PostoProiezioneDAO() {
        this.ds = DataSourceSingleton.getInstance();
    }

    public boolean create(PostoProiezione postoProiezione) {
        String sql = "INSERT INTO posto_proiezione (id_sala, fila, numero, id_proiezione, stato) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, postoProiezione.getPosto().getSala().getId());
            ps.setString(2, String.valueOf(postoProiezione.getPosto().getFila()));
            ps.setInt(3, postoProiezione.getPosto().getNumero());
            ps.setInt(4, postoProiezione.getProiezione().getId());
            ps.setBoolean(5, postoProiezione.isStato());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<PostoProiezione> retrieveAllByProiezione(Proiezione proiezione) {
        List<PostoProiezione> postiProiezione = new ArrayList<>();
        String sql = "SELECT * FROM posto_proiezione WHERE id_proiezione = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, proiezione.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PostoProiezione postoProiezione = new PostoProiezione();
                Posto posto = new Posto();
                posto.setSala(new Sala());
                posto.getSala().setId(rs.getInt("id_sala"));
                posto.setFila(rs.getString("fila").charAt(0));
                posto.setNumero(rs.getInt("numero"));
                postoProiezione.setPosto(posto);
                postoProiezione.setProiezione(proiezione);
                postoProiezione.setStato(rs.getBoolean("stato"));
                postiProiezione.add(postoProiezione);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return postiProiezione;
    }

    public boolean occupaPosto(PostoProiezione postoProiezione) {
        String sql = "UPDATE posto_proiezione SET stato = false WHERE id_sala = ? AND fila = ? AND numero = ? AND id_proiezione = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, postoProiezione.getPosto().getSala().getId());
            ps.setString(2, String.valueOf(postoProiezione.getPosto().getFila()));
            ps.setInt(3, postoProiezione.getPosto().getNumero());
            ps.setInt(4, postoProiezione.getProiezione().getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
