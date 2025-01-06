package it.unisa.application.sottosistemi.gestione_utente.service;

import it.unisa.application.model.dao.ClienteDAO;
import it.unisa.application.model.dao.SedeDAO;
import it.unisa.application.model.dao.UtenteDAO;
import it.unisa.application.model.entity.Cliente;
import it.unisa.application.model.entity.GestoreSede;
import it.unisa.application.model.entity.Sede;
import it.unisa.application.model.entity.Utente;
import it.unisa.application.utilities.PasswordHash;
import jakarta.servlet.http.HttpSession;

public class AutenticazioneService {
    private UtenteDAO utenteDAO;
    private  ClienteDAO clienteDAO;

    public AutenticazioneService() {
        this.utenteDAO = new UtenteDAO();
        this.clienteDAO = new ClienteDAO();
    }
    /*Costruttore per il testing*/
    public AutenticazioneService(UtenteDAO utenteDAOMock, ClienteDAO clienteDAOMock) {
        this.utenteDAO = utenteDAOMock;
        this.clienteDAO = clienteDAOMock;
    }


    public Utente login(String email, String password) {
        Utente baseUser = utenteDAO.retrieveByEmail(email);
        if (baseUser == null) {
            return null;
        }
        String passHash = PasswordHash.hash(password);
        if (!baseUser.getPassword().equals(passHash)) {
            return null;
        }
        if (baseUser.getRuolo().equalsIgnoreCase("cliente")) {
            Cliente c = clienteDAO.retrieveByEmail(email, passHash);
            return c;
        }
        if (baseUser.getRuolo().equalsIgnoreCase("gestore_sede")) {
            GestoreSede gs = new GestoreSede();
            gs.setEmail(baseUser.getEmail());
            gs.setPassword(baseUser.getPassword());
            gs.setRuolo(baseUser.getRuolo());

            SedeDAO sedeDAO = new SedeDAO();
            Sede sede = sedeDAO.retrieveByGestoreEmail(baseUser.getEmail());
            gs.setSede(sede);

            return gs;
        }

        return baseUser;
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }
}
