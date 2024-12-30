package it.unisa.application.sottosistemi.gestione_utente.service;

import it.unisa.application.model.dao.ClienteDAO;
import it.unisa.application.model.dao.UtenteDAO;
import it.unisa.application.model.entity.Utente;
import jakarta.servlet.http.HttpSession;

public class AutenticazioneService {
    private final UtenteDAO utenteDAO;
    private final ClienteDAO clienteDAO;

    public AutenticazioneService() {
        this.utenteDAO = new UtenteDAO();
        this.clienteDAO = new ClienteDAO();
    }

    public Utente login(String email, String password) {
        Utente utente = utenteDAO.retrieveByEmail(email);
        if (utente != null && utente.getPassword().equals(password)) {
            if(utente.getRuolo().equalsIgnoreCase("cliente"))
                return clienteDAO.retrieveByEmail(email, password);
            else
                return utente;
        }
        return null;
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }
}

