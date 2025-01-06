package it.unisa.application.sottosistemi.gestione_utente.service;

import it.unisa.application.model.dao.ClienteDAO;
import it.unisa.application.model.dao.UtenteDAO;
import it.unisa.application.model.entity.Cliente;
import it.unisa.application.utilities.*;

import java.util.HashMap;
import java.util.Map;

public class RegistrazioneService {
    private ValidateStrategyManager validationManager;
    private UtenteDAO utenteDAO;
    private ClienteDAO clienteDAO;

    public RegistrazioneService() {
        this.validationManager = new ValidateStrategyManager();
        this.utenteDAO = new UtenteDAO();
        this.clienteDAO = new ClienteDAO();

        validationManager.addValidator("email", new EmailValidator());
        validationManager.addValidator("password", new PasswordValidator());
        validationManager.addValidator("nome", new CampoValidator());
        validationManager.addValidator("cognome", new CampoValidator());
    }
/*Costruttore per il testing*/
    public RegistrazioneService(UtenteDAO utenteDAOMock, ClienteDAO clienteDAOMock) {
        this.validationManager = new ValidateStrategyManager();
        this.utenteDAO = utenteDAOMock;
        this.clienteDAO = clienteDAOMock;
        validationManager.addValidator("email", new EmailValidator());
        validationManager.addValidator("password", new PasswordValidator());
        validationManager.addValidator("nome", new CampoValidator());
        validationManager.addValidator("cognome", new CampoValidator());
    }


    public Cliente registrazione(String email, String password, String nome, String cognome) {
        Map<String, String> inputs = new HashMap<>();
        inputs.put("email", email);
        inputs.put("password", password);
        inputs.put("nome", nome);
        inputs.put("cognome", cognome);

        if (!validationManager.validate(inputs) || utenteDAO.retrieveByEmail(email) != null) {
            return null;
        }

        String passHash = PasswordHash.hash(password);
        Cliente cliente = new Cliente(email, passHash, nome, cognome);
        clienteDAO.create(cliente);
        return cliente;
    }
}
