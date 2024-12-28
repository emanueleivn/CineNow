package it.unisa.application.sottosistemi.gestione_utente.service;

import it.unisa.application.model.dao.ClienteDAO;
import it.unisa.application.model.dao.UtenteDAO;
import it.unisa.application.model.entity.Cliente;
import it.unisa.application.model.entity.Utente;
import it.unisa.application.utilities.CampoValidator;
import it.unisa.application.utilities.EmailValidator;
import it.unisa.application.utilities.PasswordValidator;
import it.unisa.application.utilities.ValidateStrategyManager;

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
    public Cliente registrazione(String email,String password,String nome,String cognome){
        if (!validationManager.validate(Map.of("email", email, "password", password, "nome", nome, "cognome", cognome))
                ||utenteDAO.retrieveByEmail(email) != null) {
            return null;
        }
        Cliente cliente = new Cliente(email, password, nome, cognome);
        clienteDAO.create(cliente);
        return cliente;
    }
}
