package it.unisa.application.sottosistemi.gestione_utente.service;

import it.unisa.application.model.dao.ClienteDAO;
import it.unisa.application.model.dao.UtenteDAO;
import it.unisa.application.model.entity.Cliente;
import it.unisa.application.model.entity.Utente;
import it.unisa.application.utilities.EmailValidator;
import it.unisa.application.utilities.PasswordValidator;
import it.unisa.application.utilities.ValidateStrategyManager;
import jakarta.servlet.http.HttpSession;

import java.util.Map;

public class AutenticazioneService {
    private ValidateStrategyManager validationManager;
    private UtenteDAO utenteDAO;
    private ClienteDAO clienteDAO;

    public AutenticazioneService() {
        this.validationManager = new ValidateStrategyManager();
        this.utenteDAO = new UtenteDAO();
        this.clienteDAO = new ClienteDAO();

        validationManager.addValidator("email", new EmailValidator());
        validationManager.addValidator("password", new PasswordValidator());
    }

    public Cliente login(String email, String password) {
        if (!validationManager.validate(Map.of("email", email, "password", password))) {
            return null;
        }
        Utente utente = utenteDAO.retrieveByEmail(email);
        if (utente != null && utente.getPassword().equals(password)) {
            if ("cliente".equalsIgnoreCase(utente.getRuolo())) {
                return clienteDAO.retrieveByEmail(email, password);
            }
        }
        return null;
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }
}

