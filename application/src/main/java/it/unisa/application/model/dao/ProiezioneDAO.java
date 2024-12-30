package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.Film;
import it.unisa.application.model.entity.Proiezione;
import it.unisa.application.model.entity.Slot;
import java.sql.*;

public class ProiezioneDAO {
    private static final String INSERT_PROIEZIONE = "INSERT INTO proiezione(id, data, id_film, id_sala, id_orario) VALUES(?, ?, ?, ?, ?)";

    public Proiezione aggiungiProiezione(Slot s, Date d, Film film){
        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_PROIEZIONE, Statement.RETURN_GENERATED_KEYS)) {

        }catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
