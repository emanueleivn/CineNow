package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.Proiezione;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SedeDAO {
    /*
    * getProgrammazione() -> List<Proiezione>
    * */
/*
    public List<Proiezione> getProgrammazione(){
        List<Proiezione> proiezioni = new ArrayList<Proiezione>();
        String query = "SELECT * FROM proiezione";

        try (Connection connection = DataSourceSingleton.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Proiezione p = new Proiezione();

                p.setId(resultSet.getInt("id"));
                p.setDataProiezione(resultSet.getDate("data").toLocalDate());
                p.setIdFilm(resultSet.getInt("id_film"));
                p.setIdSala(resultSet.getInt("id_sala"));
                p.setIdOrario(resultSet.getInt("id_orario"));

                proiezioni.add(p);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }


        return proiezioni;
    }
    */

}
