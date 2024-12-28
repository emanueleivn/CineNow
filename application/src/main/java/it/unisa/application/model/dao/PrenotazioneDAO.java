package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.Prenotazione;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrenotazioneDAO {

    private static final String INSERT_PRENOTAZIONE = "INSERT INTO prenotazione (email_cliente, id_proiezione) VALUES (?, ?)";
    private static final String SELECT_PRENOTAZIONI_BY_CLIENTE = "SELECT * FROM prenotazione WHERE email_cliente = ?";

    // Aggiunge una prenotazione al database
    public boolean create(Prenotazione prenotazione) {
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_PRENOTAZIONE, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, prenotazione.getEmailCliente());
            ps.setInt(2, prenotazione.getIdProiezione());
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    prenotazione.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Ottiene tutte le prenotazioni di un cliente
    public List<Prenotazione> retrieveAllByCliente(String emailCliente) {
        System.out.println("Esecuzione query per email: " + emailCliente); // Log
        List<Prenotazione> prenotazioni = new ArrayList<>();
        String query = "SELECT * FROM prenotazione WHERE email_cliente = ?";
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, emailCliente);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                prenotazioni.add(new Prenotazione(
                        rs.getInt("id"),
                        rs.getString("email_cliente"),
                        rs.getInt("id_proiezione")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prenotazioni;
    }

}
