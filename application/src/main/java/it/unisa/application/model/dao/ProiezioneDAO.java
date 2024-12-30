package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.Film;
import it.unisa.application.model.entity.Prenotazione;
import it.unisa.application.model.entity.Proiezione;
import it.unisa.application.model.entity.Slot;

import javax.sql.DataSource;
import java.sql.*;

public class ProiezioneDAO {
    private final DataSource ds;

    public ProiezioneDAO() {
        this.ds = DataSourceSingleton.getInstance();
    }

    public boolean create(Proiezione proiezione){
        String INSERT_PROIEZIONE = "INSERT INTO proiezione(id, data, id_film, id_sala, id_orario) VALUES(?, ?, ?, ?, ?)";
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_PROIEZIONE, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, proiezione.getId());
            ps.setDate(2, proiezione.getDataProiezione());
            ps.setInt(3, proiezione.getFilmProiezione().getId());
            ps.setInt(4, proiezione.getSalaProiezione().getId());
            ps.setInt(5, proiezione.getOrarioProiezione().getId());

        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }


}
