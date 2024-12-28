package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.PostoProiezione;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostoProiezioneDAO {

    private static final String UPDATE_STATO_POSTO = "UPDATE posto_proiezione SET stato = ? WHERE id_sala = ? AND fila = ? AND numero = ? AND id_proiezione = ?";
    private static final String SELECT_POSTI_BY_PROIEZIONE = "SELECT * FROM posto_proiezione WHERE id_proiezione = ?";

    // Aggiorna lo stato di un posto
    public boolean aggiornaStatoPosto(int idSala, char fila, int numero, int idProiezione, boolean stato) {
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_STATO_POSTO)) {

            ps.setBoolean(1, stato);
            ps.setInt(2, idSala);
            ps.setString(3, String.valueOf(fila));
            ps.setInt(4, numero);
            ps.setInt(5, idProiezione);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Ottiene i posti di una proiezione
    public List<PostoProiezione> getPostiByProiezione(int idProiezione) {
        List<PostoProiezione> posti = new ArrayList<>();
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_POSTI_BY_PROIEZIONE)) {

            ps.setInt(1, idProiezione);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PostoProiezione posto = new PostoProiezione();
                posto.setIdSala(rs.getInt("id_sala"));
                posto.setFila(rs.getString("fila").charAt(0));
                posto.setNumero(rs.getInt("numero"));
                posto.setIdProiezione(rs.getInt("id_proiezione"));
                posto.setStato(rs.getBoolean("stato"));
                posti.add(posto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posti;
    }
}
