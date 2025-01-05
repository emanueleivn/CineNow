package it.unisa.application.model.dao;

import it.unisa.application.database_connection.DataSourceSingleton;
import it.unisa.application.model.entity.*;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProiezioneDAO {
    private final DataSource ds;

    public ProiezioneDAO() {
        this.ds = DataSourceSingleton.getInstance();
    }

    public boolean create(Proiezione proiezione) {
        String insertProiezioneSql = "INSERT INTO proiezione (data, id_film, id_sala, id_orario) VALUES (?, ?, ?, ?)";
        String insertPostiProiezioneSql = """
                INSERT INTO posto_proiezione (id_sala, fila, numero, id_proiezione, stato)
                SELECT posto.id_sala, posto.fila, posto.numero, ?, TRUE
                FROM posto
                WHERE posto.id_sala = ?
                """;

        try (Connection connection = ds.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement psProiezione = connection.prepareStatement(insertProiezioneSql, Statement.RETURN_GENERATED_KEYS)) {
                psProiezione.setDate(1, Date.valueOf(proiezione.getDataProiezione()));
                psProiezione.setInt(2, proiezione.getFilmProiezione().getId());
                psProiezione.setInt(3, proiezione.getSalaProiezione().getId());
                psProiezione.setInt(4, proiezione.getOrarioProiezione().getId());
                int affectedRows = psProiezione.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet rs = psProiezione.getGeneratedKeys()) {
                        if (rs.next()) {
                            int idProiezione = rs.getInt(1);
                            proiezione.setId(idProiezione);
                            try (PreparedStatement psPostiProiezione = connection.prepareStatement(insertPostiProiezioneSql)) {
                                psPostiProiezione.setInt(1, idProiezione);
                                psPostiProiezione.setInt(2, proiezione.getSalaProiezione().getId());
                                psPostiProiezione.executeUpdate();
                            }
                            connection.commit();
                            return true;
                        }
                    }
                }
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public Proiezione retrieveById(int id) {
        String sql = "SELECT * FROM proiezione WHERE id = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Proiezione proiezione = new Proiezione();
                proiezione.setId(rs.getInt("id"));
                Film film = new Film();
                film.setId(rs.getInt("id_film"));
                Sala sala = new Sala();
                sala.setId(rs.getInt("id_sala"));
                Slot slotOrario = new Slot();
                slotOrario.setId(rs.getInt("id_orario"));
                proiezione.setFilmProiezione(film);
                proiezione.setDataProiezione(rs.getDate("data").toLocalDate());
                proiezione.setSalaProiezione(sala);
                proiezione.setOrarioProiezione(slotOrario);
                return proiezione;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Proiezione> retrieveByFilm(Film film, Sede sede) {
        String sql = """
            SELECT p.*, s.numero AS numero_sala, f.titolo AS titolo_film, f.durata AS durata_film, sl.ora_inizio AS orario
            FROM proiezione p
            JOIN sala s ON p.id_sala = s.id
            JOIN film f ON p.id_film = f.id
            JOIN slot sl ON p.id_orario = sl.id
            WHERE p.id_film = ? AND s.id_sede = ?
            ORDER BY p.data ASC, sl.ora_inizio ASC
            """;

        List<Proiezione> proiezioni = new ArrayList<>();
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, film.getId());
            ps.setInt(2, sede.getId());
            ResultSet rs = ps.executeQuery();

            // Mappa per gestire le proiezioni uniche
            Map<String, List<Proiezione>> uniqueProiezioni = new HashMap<>();

            while (rs.next()) {
                Proiezione proiezione = new Proiezione();
                proiezione.setId(rs.getInt("id"));
                proiezione.setDataProiezione(rs.getDate("data").toLocalDate());

                Sala sala = new Sala();
                sala.setId(rs.getInt("id_sala"));
                sala.setNumeroSala(rs.getInt("numero_sala"));
                proiezione.setSalaProiezione(sala);

                Film filmDetails = new Film();
                filmDetails.setId(rs.getInt("id_film"));
                filmDetails.setTitolo(rs.getString("titolo_film"));
                filmDetails.setDurata(rs.getInt("durata_film")); // Durata in minuti
                proiezione.setFilmProiezione(filmDetails);

                Slot slot = new Slot();
                slot.setId(rs.getInt("id_orario"));
                slot.setOraInizio(rs.getTime("orario"));
                proiezione.setOrarioProiezione(slot);

                // Chiave unica basata su titolo, sala e giorno della proiezione
                String uniqueKey = proiezione.getFilmProiezione().getTitolo() + "|" +
                        proiezione.getSalaProiezione().getId() + "|" +
                        proiezione.getDataProiezione().toString();

                // Recupera la lista di proiezioni uniche per questa chiave
                List<Proiezione> proiezioniPerChiave = uniqueProiezioni.getOrDefault(uniqueKey, new ArrayList<>());

                boolean aggiungiProiezione = true;
                for (Proiezione existingProiezione : proiezioniPerChiave) {
                    int existingEndMinute = existingProiezione.getOrarioProiezione().getOraInizio().toLocalTime().toSecondOfDay() / 60
                            + existingProiezione.getFilmProiezione().getDurata();
                    int currentStartMinute = proiezione.getOrarioProiezione().getOraInizio().toLocalTime().toSecondOfDay() / 60;

                    // Controlla se la nuova proiezione è distinta rispetto a quelle già aggiunte
                    if (currentStartMinute < existingEndMinute) {
                        aggiungiProiezione = false;
                        break;
                    }
                }

                if (aggiungiProiezione) {
                    proiezioniPerChiave.add(proiezione);
                    uniqueProiezioni.put(uniqueKey, proiezioniPerChiave);
                    proiezioni.add(proiezione);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return proiezioni;
    }

    public List<Proiezione> retrieveAllBySede(int sedeId) {
        List<Proiezione> proiezioni = new ArrayList<>();
        String sql = """
            SELECT p.*, s.numero AS numero_sala, f.titolo AS titolo_film, f.durata AS durata_film, sl.ora_inizio AS orario
            FROM proiezione p
            JOIN sala s ON p.id_sala = s.id
            JOIN film f ON p.id_film = f.id
            JOIN slot sl ON p.id_orario = sl.id
            WHERE s.id_sede = ?
            ORDER BY p.data ASC, sl.ora_inizio ASC
            """;

        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, sedeId);
            ResultSet rs = ps.executeQuery();

            // Mappa per tracciare le proiezioni uniche per chiave
            Map<String, List<Proiezione>> uniqueProiezioni = new HashMap<>();

            while (rs.next()) {
                Proiezione proiezione = new Proiezione();
                proiezione.setId(rs.getInt("id"));
                Sala sala = new Sala();
                sala.setId(rs.getInt("id_sala"));
                sala.setNumeroSala(rs.getInt("numero_sala"));
                proiezione.setSalaProiezione(sala);
                Film film = new Film();
                film.setId(rs.getInt("id_film"));
                film.setTitolo(rs.getString("titolo_film"));
                film.setDurata(rs.getInt("durata_film")); // Durata del film in minuti
                proiezione.setFilmProiezione(film);
                Slot slot = new Slot();
                slot.setId(rs.getInt("id_orario"));
                slot.setOraInizio(rs.getTime("orario"));
                proiezione.setOrarioProiezione(slot);
                proiezione.setDataProiezione(rs.getDate("data").toLocalDate());

                // Chiave unica per giorno, sala e film
                String uniqueKey = proiezione.getFilmProiezione().getTitolo() + "|" +
                        proiezione.getSalaProiezione().getId() + "|" +
                        proiezione.getDataProiezione().toString();

                // Recupera tutte le proiezioni esistenti per la chiave
                List<Proiezione> proiezioniPerChiave = uniqueProiezioni.getOrDefault(uniqueKey, new ArrayList<>());

                boolean aggiungiProiezione = true;
                for (Proiezione existingProiezione : proiezioniPerChiave) {
                    int existingEndMinute = existingProiezione.getOrarioProiezione().getOraInizio().toLocalTime().toSecondOfDay() / 60
                            + existingProiezione.getFilmProiezione().getDurata();
                    int currentStartMinute = proiezione.getOrarioProiezione().getOraInizio().toLocalTime().toSecondOfDay() / 60;

                    // Verifica se si sovrappone con una proiezione esistente
                    if (currentStartMinute < existingEndMinute) {
                        aggiungiProiezione = false;
                        break;
                    }
                }

                if (aggiungiProiezione) {
                    proiezioniPerChiave.add(proiezione);
                    uniqueProiezioni.put(uniqueKey, proiezioniPerChiave);
                    proiezioni.add(proiezione);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return proiezioni;
    }


    public Proiezione retrieveProiezioneBySalaSlotAndData(int salaId, int slotId, LocalDate data) {
        String sql = "SELECT * FROM proiezione WHERE id_sala = ? AND id_orario = ? AND data = ?";
        try (Connection connection = ds.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, salaId);
            ps.setInt(2, slotId);
            ps.setDate(3, Date.valueOf(data));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Proiezione p = new Proiezione();
                p.setId(rs.getInt("id"));
                Film f = new Film();
                f.setId(rs.getInt("id_film"));
                p.setFilmProiezione(f);
                Sala s = new Sala();
                s.setId(rs.getInt("id_sala"));
                Slot sl = new Slot();
                sl.setId(rs.getInt("id_orario"));
                p.setSalaProiezione(s);
                p.setOrarioProiezione(sl);
                p.setDataProiezione(rs.getDate("data").toLocalDate());
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
